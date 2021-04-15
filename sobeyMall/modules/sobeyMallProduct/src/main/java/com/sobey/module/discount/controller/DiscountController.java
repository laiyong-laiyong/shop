package com.sobey.module.discount.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.AppException;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.discount.enumeration.DiscountType;
import com.sobey.module.discount.enumeration.ProtocalType;
import com.sobey.module.discount.model.Discount;
import com.sobey.module.discount.model.DiscountRequestInfo;
import com.sobey.module.discount.model.Statement;
import com.sobey.module.discount.model.request.ProductDiscount;
import com.sobey.module.discount.service.DiscountRequstInfoService;
import com.sobey.module.discount.service.DiscountService;
import com.sobey.module.message.service.feign.MsgFeignService;
import com.sobey.module.product.enumeration.OperationType;
import com.sobey.module.account.model.response.UserDetail;
import com.sobey.module.account.service.feign.UserFeignService;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.common.ToolKit;
import com.sobey.util.common.json.JsonKit;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.FastDateFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${api.v1}/discount")
@Api(value = "折扣", description = "折扣相关接口")
public class DiscountController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DiscountService ds;
	@Autowired
	private RedisTemplate<String, String> redis;
	@Autowired
	private MsgFeignService mfs;
	@Autowired
	private UserFeignService user;
	@Autowired
	private DiscountRequstInfoService info;

	@Value("${productDomain}")
	private String domain;

	@ApiOperation(value = "折扣删除")
	@DeleteMapping("/{uuid}")
	public void remove(@PathVariable("uuid") String uuid) {

		Discount db = this.ds.selectById(uuid);
		if (db == null) {
			throw new AppException("数据不存在,请核对");
		}
		this.ds.deleteById(uuid);
		String userCode = db.getUserCode();
		String productId = db.getProductId();
		String key = this.getKey(userCode, productId);
		/**
		 * 即使key不存在,这里也不会报错，不用catch
		 * 
		 */
		redis.delete(key);

	}

	@ApiOperation(value = "分页")
	@PostMapping("/page")
	public Page<Discount> page(@RequestParam int page, @RequestParam int size,
			@RequestBody(required = false) Discount entity) {
		Page<Discount> pg = new Page<Discount>(page, size);
		ds.page(pg, entity);
		return pg;
	}


	@ApiOperation(value = "新增")
	@PostMapping
	@Transactional
	public void insert(@Validated @RequestBody Discount entity, BindingResult bindingResult) {
		
		ToolKit.validData(bindingResult);
		
		String usercode = entity.getUserCode();
		List<ProductDiscount> productDiscounts = entity.getProductDiscounts();
		
		for (ProductDiscount item : productDiscounts) {
			if (item != null) {
				Discount dis = new Discount();
				dis.setUserCode(usercode);
				List<String> productIds = item.getProductIds();
				if (CollectionUtils.isNotEmpty(productIds)) {
					for (String productId : productIds) {
						dis.setProductId(productId);
						this.ds.check(dis);
					}
				}
			}
		}
		
		/**
		 * 这里不写到上面this.ds.check(dis)后是因为,如果有报错了mysql回滚了， 但是redis没有回滚，产生了冗余数据
		 * 
		 */
		String requestUuid = IdWorker.get32UUID();
		for (ProductDiscount item : productDiscounts) {
			if (item != null) {
				List<String> productIds = item.getProductIds();
				if (CollectionUtils.isNotEmpty(productIds)) {
					for (String productId : productIds) {
						Discount et = new Discount();
						et.setUserCode(usercode);
						et.setUserSiteCode(entity.getUserSiteCode());
						et.setProductId(productId);
						et.setSalesman(entity.getSalesman());
						et.setCustomer(entity.getCustomer());
						et.setDescription(entity.getDescription());
						et.setExpirationDate(entity.getExpirationDate());
						et.setStatus(DiscountType.not_enable.getCode());
						et.setRequestUuid(requestUuid);
						et.setUserLoginName(entity.getUserLoginName());
						BigDecimal discount = item.getDiscount();
						if (discount != null) {
							discount = discount.setScale(4, RoundingMode.DOWN);
							et.setDiscount(discount);
							// C同时改变请求的折扣
							item.setDiscount(discount);
						}
						
						this.ds.save(et);
					}
				}
				
			}
		}
		
		this.saveInfoAndSendMsg(requestUuid, usercode, OperationType.insert.getName());
	}

	/**
	 * C保存请求信息并发送消息
	 * 
	 * @param entity
	 * @param usercode
	 * @param operationType
	 */
	private void saveInfoAndSendMsg(String requestUuid, String usercode, String operationType) {
		// 请求数据保存到请求信息表
		DiscountRequestInfo request = new DiscountRequestInfo();
		request.setUuid(requestUuid);
		request.setOperationType(operationType);
		info.insert(request);

		// 发送消息
		String msg = "用户您好，您所申请的折扣已生成，请查看详情，确认后生效！msg_discount_contract_id=" + requestUuid;
		try {
			String auth = HeaderUtil.getAuth();
			mfs.sendTextMsg(auth, "15", usercode, msg.toString());
		} catch (Exception e) {
			throw new AppException("消息发送失败,请联系管理员");
		}
	}

	private UserDetail queryUser(String userSiteCode,String userCode) {

		if (StringUtils.isNotEmpty(userCode)) {
			String auth = HeaderUtil.getAuth();
			UserDetail user = this.user.queryUserCrossTenant(auth, userSiteCode,userCode);
			return user;
		} else {
			return null;
		}

	}

	@ApiOperation(value = "查看协议")
	@GetMapping("/protocal/{uuid}")
	public String query(@PathVariable("uuid") String uuid) {

		DiscountRequestInfo db = this.info.selectById(uuid);
		if (db == null) {
			throw new AppException("数据不存在，请确认");
		}
		
		//C已经确认的协议不在修改
		String status = db.getStatus();
		if(ProtocalType.enable.getCode().equalsIgnoreCase(status)) {
			return db.getProtocal();
		}

		// C已经生成过协议，直接返回
//		String finalProtocal = db.getProtocal();
//		if (StrUtil.isNotBlank(finalProtocal)) {
//			return finalProtocal;
//		}


		//C生成多个商品的折扣
		String operationType = db.getOperationType();
		StringBuilder discountText = this.ds.getDisCountText(uuid, operationType);

		
		//C同一个请求的usercode,customer,ExpirationDate是相同的
		Wrapper<Discount> wp = new EntityWrapper<Discount>();
		wp.eq("requestUuid", uuid);
		List<Discount> list = this.ds.selectList(wp);
		Discount bean = null;
		if (CollectionUtils.isNotEmpty(list)) {
			bean = list.get(0);
		}else {
			throw new AppException("此协议的折扣已经不存在");
		}
			
		
		UserDetail detail = queryUser(bean.getUserSiteCode(),bean.getUserCode());
		String customerName = null;
		String registerName = null;
		String address = null;
		String telephone = null;
		String linkman = null;
		if (detail != null) {
			customerName = bean.getCustomer();
			registerName = detail.getLogin_name();
			address = "";
			telephone = detail.getPhone()==null?"":detail.getPhone();
			linkman = "";
		}

		Date date = bean.getExpirationDate();
		String expirationDate = DateUtil.format(date, FastDateFormat.getInstance("yyyy年MM月dd日 HH时mm分ss秒", TimeZone.getTimeZone("Asia/Shanghai")));
		
		String signDate = DateUtil.formatChineseDate(new Date(), false);

		// 替换协议模板
		Statement st = this.ds.selectStatementById();
		
		String protocal = this.replace(st.getText(), "customerName", customerName);
		protocal = StringUtils.replace(protocal, "telephone", telephone);
		protocal = StringUtils.replace(protocal, "registerName", registerName);
		protocal = StringUtils.replace(protocal, "address", address);
		protocal = StringUtils.replace(protocal, "linkman", linkman);
		protocal = StringUtils.replace(protocal, "discountText", discountText.toString());
		protocal = StringUtils.replace(protocal, "expirationDate", expirationDate);
		protocal = StringUtils.replace(protocal, "signDate", signDate);

		// 保存折扣协议内容
		DiscountRequestInfo request = new DiscountRequestInfo();
		request.setUuid(uuid);
		request.setProtocal(protocal);
		request.setStatus(ProtocalType.not_enable.getCode());

		String dateStr = DateUtil.formatDateTime(new Date());
		String protocalCode = "SOBEY-" + dateStr.replace("-", "").replace(":", "").replace(" ", "");
		request.setProtocalCode(protocalCode);
		this.info.updateById(request);

		return protocal;

	}
	
	
	public String replace(final String text, final String searchString, final String replacement) {
		String protocal = null;
		if (StringUtils.isBlank(replacement)) {
			protocal = StringUtils.replace(text, searchString, "");
		}else {
			protocal = StringUtils.replace(text, searchString, replacement);
		}
		return protocal;
	}

	/**
	 * desc 由于这步是在保存折扣信息之后做的，所以不做数据校验了
	 * 
	 * 
	 * @param uuid
	 */
	@ApiOperation(value = "确认协议")
	@PostMapping("/protocal/{uuid}")
	@Transactional
	public void confirm(@PathVariable("uuid") String uuid) {

		DiscountRequestInfo db = this.info.selectById(uuid);
		if (db == null) {
			throw new AppException("数据不存在，请确认");
		}
		
		String status = db.getStatus();
		if (DiscountType.enable.getCode().equalsIgnoreCase(status)) {
			throw new AppException("此协议已经确认过");
		}

		
		
		Wrapper<Discount> wp = new EntityWrapper<Discount>();
		wp.eq("requestUuid", uuid);
		List<Discount> list = this.ds.selectList(wp);
		if (CollectionUtils.isNotEmpty(list)) {
			
			for (Discount item : list) {
				String usercode = item.getUserCode();
				String productId = item.getProductId();
				// 修改折扣状态
				Wrapper<Discount> wp2 = new EntityWrapper<Discount>();
				wp2.eq("userCode", usercode);
				wp2.eq("productId", productId);
				this.ds.updateForSet("status = '2' ", wp);
				
				String key = getKey(usercode, productId);
				item.setStatus(DiscountType.enable.getName());
				redis.opsForValue().set(key, JsonKit.beanToJson(item));
			}
		}
		


		// 修改协议状态
		DiscountRequestInfo request = new DiscountRequestInfo();
		request.setUuid(uuid);
		request.setStatus(ProtocalType.enable.getCode());
		this.info.updateById(request);

	}
	
	

	@ApiOperation(value = "修改")
	@PatchMapping
	@Transactional
	public void update(@RequestBody Discount entity) {

		String uuid = entity.getUuid();
		String usercode = entity.getUserCode();
		String productId = entity.getProductId();
		if (StringUtils.isBlank(uuid) || StringUtils.isBlank(usercode)
				|| StringUtils.isBlank(productId)) {
			throw new AppException("参数uuid，userCode,productId不能为空");
		}

		Discount db = this.ds.selectById(uuid);
		if (db == null) {
			throw new AppException("数据不存在,请核对");
		}

		// 处理折扣
		BigDecimal discount = entity.getDiscount();
		if (discount != null) {
			discount = discount.setScale(4, RoundingMode.DOWN);
			entity.setDiscount(discount);
		}
		entity.setStatus(DiscountType.not_enable.getCode());
		String requestUuid = IdWorker.get32UUID();
		entity.setRequestUuid(requestUuid);
		this.ds.updateById(entity);
		

		saveInfoAndSendMsg(requestUuid, usercode, OperationType.update.getName());
	}

	@ApiOperation(value = "redis查询")
	@GetMapping
	public BigDecimal getDiscount(@RequestParam(required = true) @ApiParam("用户编码") String usercode,
			@RequestParam(required = true) @ApiParam("商品编号") String productId) {

		String key = this.getKey(usercode, productId);
		String rs = redis.opsForValue().get(key);
		Discount bean = null;
		if (StringUtils.isNotBlank(rs)) {
			bean = JsonKit.jsonToBean(rs, Discount.class);

			// 查看是否过期
			Date expirationDate = bean.getExpirationDate();
			// 过期时间小于当前时间
			if (DateUtil.compare(expirationDate, new Date()) < 0) {
				return null;
			} else {
				BigDecimal discount = bean.getDiscount();
				return discount;
			}
		} else {
			return null;
		}
	}

	private String getKey(String usercode, String productId) {
		String key = "dis-" + usercode + "-" + productId;
		return key;
	}

	@ApiOperation(value = "折扣状态,不需要token")
	@GetMapping("/discount-type")
	@PassToken
	public String getDiscountType() {

		String json = JsonKit.enumToJson(DiscountType.class);
		return json;
	}

}
