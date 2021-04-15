package com.sobey.module.discount.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.exception.AppException;
import com.sobey.module.discount.mapper.DiscountMapper;
import com.sobey.module.discount.mapper.StatementMapper;
import com.sobey.module.discount.model.Discount;
import com.sobey.module.discount.model.Statement;
import com.sobey.module.product.model.Product;
import com.sobey.module.product.service.ProductService;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class DiscountService extends ServiceImpl<DiscountMapper, Discount> {

	@Autowired
	private DiscountMapper dm;
	@Autowired
	private StatementMapper sm;
	@Autowired
	private ProductService ps;

	public void save(Discount vs) {
		if (vs == null) {
			return;
		}
		this.dm.insert(vs);
	}

	public void check(Discount item) {

		String productId = item.getProductId();
		String userCode = item.getUserCode();
		if (StringUtils.isBlank(userCode) || StringUtils.isBlank(productId)) {
			throw new AppException("参数usercode和productIds数组中的productId必须传递");
		}

		//没有过期折扣
		Wrapper<Discount> wp = new EntityWrapper<Discount>(item);
		wp.gt("expirationDate", new Date());
		List<Discount> list = this.selectList(wp);
		if (CollectionUtils.isNotEmpty(list) && list.size() >= 1) {

			Product pt = this.ps.selectById(productId);
			String name = null;
			if (pt != null) {
				name = pt.getName();
			}

			throw new AppException("一个用户对一个商品只能有一个折扣信息,商品名称为:" + name);
		}
	}

	/**
	 * 查询免责声明
	 * 
	 * @return
	 */
	public Statement selectStatementById() {
		Statement st = this.sm.selectById("1");
		return st;
	}

	public Page<Discount> page(Page<Discount> page, Discount entity) {

		List<Discount> cts = this.dm.page(page, entity);
		page.setRecords(cts);
		return page;
	}
	
	
	public StringBuilder getDisCountText(String requestUuid, String operationType) {

		Wrapper<Discount> wp = new EntityWrapper<Discount>();
		wp.eq("requestUuid", requestUuid);
		List<Discount> list = this.selectList(wp);
		StringBuilder discountText = new StringBuilder("\r\n");
		if (CollectionUtils.isNotEmpty(list)) {
			for (Discount item : list) {
				BigDecimal discount = item.getDiscount().setScale(2, RoundingMode.DOWN);
				Product pt = this.ps.selectById(item.getProductId());
				if (pt != null) {
					String name = pt.getName();
					BigDecimal basic = new BigDecimal(10);
					discountText.append("产品：").append(name).append("，折扣：")
							.append(discount.multiply(basic)).append("折\r\n");
				}
				discountText.append("\r\n");
			}
		}
		return discountText;

	}
	

	
	
	

}
