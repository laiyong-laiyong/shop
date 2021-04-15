package com.sobey.module.workOrder.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.framework.config.FileUpload;
import com.sobey.module.message.service.feign.WorkOrderMsgFeignService;
import com.sobey.module.product.model.response.Product;
import com.sobey.module.product.service.feign.ProductFeignService;
import com.sobey.module.shortMessage.service.feign.ShortMessageFeignService;
import com.sobey.module.account.model.response.UserDetail;
import com.sobey.module.account.service.feign.UserFeignService;
import com.sobey.module.workOrder.enumeration.notifyType;
import com.sobey.module.workOrder.mapper.WorkOrderMapper;
import com.sobey.module.workOrder.model.WorkOrder;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.business.identity.Identity;
import com.sobey.util.common.uuid.UUIDUtils;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class WorkOrderService extends ServiceImpl<WorkOrderMapper, WorkOrder> {

	@Autowired
	private UserFeignService user;
	@Autowired
	private WorkOrderMapper wom;
	@Autowired
	private FileUpload upload;
	@Autowired
	private ProductFeignService pfs;
	@Autowired
	private WorkOrderMsgFeignService mfs;
	@Autowired
	private ShortMessageFeignService smfs;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public Page<WorkOrder> page(Page<WorkOrder> page, WorkOrder entity) {

		List<WorkOrder> cts = wom.page(page, entity);
		handleAdditional(cts);
		page.setRecords(cts);
		return page;
	}

	public UserDetail queryUser(String siteCode,String userCode) {

		if (StringUtils.isNotEmpty(userCode)) {
			String auth = HeaderUtil.getAuth();
			UserDetail user = this.user.queryUserCrossTenant(auth, siteCode,userCode);
			return user;
		} else {
			return null;
		}

	}

	/**
	 * 设置处理时长,创建者名称，处理者名称
	 * 
	 * @param item
	 */
	public void handleAdditional(List<WorkOrder> cts) {

		if (CollectionUtils.isEmpty(cts)) {
			return;
		}

		for (WorkOrder item : cts) {
			if (item != null) {

				// 设置处理时长
				Date closeDate = item.getCloseDate();
				Date createDate = item.getCreateDate();
				if (closeDate == null) {
					closeDate = new Date();
				}

				String period = DurationFormatUtils.formatPeriod(createDate.getTime(),
						closeDate.getTime(), "y'年'M'月'd'日' H'时':m'分':s'秒'");
				item.setHandleDate(period);

				// 设置创建者名称
				String creatCode = item.getCreateUserCode();
				String siteCode = item.getSiteCode();
				UserDetail user = this.queryUser(siteCode,creatCode);
				if (user != null) {
					String userName = user.getLogin_name();
					item.setCreateUserName(userName);
				}

				// 设置处理者名称
				String handlerCode = item.getHandlerCode();
				UserDetail user2 = this.queryUser(Identity.PUBLIC_SITE_CODE.getCode(),handlerCode);
				if (user2 != null) {
					String userName = user2.getLogin_name();
					item.setHandlerName(userName);
				}

				// 设置商品名称
				String productId = item.getProductId();
				// 这里要判断是否为空,不然会匹配到其他接口
				if (StringUtils.isNotEmpty(productId)) {
					List<Product> pts = this.pfs.list(productId);
					if (CollectionUtils.isNotEmpty(pts)) {
						Product pt = pts.get(0);
						if (pt != null) {
							String name = pt.getName();
							item.setProductName(name);
						}
					}
				}
			}
		}

	}

	public List<WorkOrder> list(WorkOrder entity) {

		List<WorkOrder> list = wom.list(entity);
		handleAdditional(list);
		return list;
	}

	/**
	 * 获取产品素材的主目录
	 * 
	 * @return
	 */
	public String getMediaDir() {
		String parentDir = UUIDUtils.simpleUuid();
		/**
		 * C这里使用在存储路径destPath统一价格时间戳作为父目录
		 * 
		 */
		String path = upload.getPath();

		String destPath = path + "/" + parentDir;

		return destPath;
	}

	public List<HashMap<String, Object>> statistic() {
		List<HashMap<String, Object>> map = this.wom.statistic();
		return map;
	}

	/**
	 * 
	 * 
	 * @param handlerCode
	 *            处理者编号
	 * @param uuid
	 *            工单编号
	 */
	public void notify(String siteCode,String handlerCode, String uuid, String notifyWho) {

		if (StringUtils.isBlank(handlerCode)) {
			log.error("handlerCode不能为空");
			return;
		}

		String auth = HeaderUtil.getAuth();
		String[] codes = handlerCode.split(",");
		for (String code : codes) {
			UserDetail user = this.queryUser(siteCode,code);
			String userName = null;
			String phone = null;
			if (user != null) {
				userName = user.getLogin_name();
				phone = user.getPhone();
			}

			if (StringUtils.isBlank(userName) || StringUtils.isBlank(phone)) {
				log.error("userName,phone都不能为空,usercode为"+ code);
			} else {
				if (notifyType.reminder_operator.getName().equalsIgnoreCase(notifyWho)) {
					// 站内通知
					this.mfs.handleNotice(auth, uuid, code);
					// 短信通知
					this.smfs.send(auth, phone, userName, uuid,
							notifyType.reminder_operator.getName());
				} else if (notifyType.reminder_customer.getName().equalsIgnoreCase(notifyWho)) {
					// 站内确认通知
					this.mfs.workOrderConfirmation(auth, userName, uuid, code);
					// 短信通知
					this.smfs.send(auth, phone, userName, uuid,
							notifyType.reminder_customer.getName());
				} else if (notifyType.distributed_customer.getName().equalsIgnoreCase(notifyWho)) {
					// 站内工单受理通知
					this.mfs.workOrderAcceptance(auth, userName, uuid, code);
					// 短信通知
					this.smfs.send(auth, phone, userName, uuid,
							notifyType.distributed_customer.getName());
				} else if (notifyType.distributed_operator.getName().equalsIgnoreCase(notifyWho)) {
					// 站内通知
					this.mfs.handleNotice(auth, uuid, code);
					// 短信通知
					this.smfs.send(auth, phone, userName, uuid,
							notifyType.distributed_operator.getName());
				} else if (notifyType.new_work_order.getName().equalsIgnoreCase(notifyWho)) {
					// 站内通知
					this.mfs.handleNotice(auth, uuid, code);
					// 短信通知
					this.smfs.send(auth, phone, userName, uuid,
							notifyType.new_work_order.getName());
				} else {
					log.info("接收者不匹配:" + notifyWho);
				}
			}
		}
	}

}
