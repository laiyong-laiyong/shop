package com.sobey.module.product.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.brightSpot.model.BrightSpot;
import com.sobey.module.brightSpot.service.BrightSpotService;
import com.sobey.module.cooperationCase.model.CooperationCase;
import com.sobey.module.cooperationCase.service.CooperationCaseService;
import com.sobey.module.function.model.Function;
import com.sobey.module.function.service.FunctionService;
import com.sobey.module.media.model.Media;
import com.sobey.module.media.service.MediaService;
import com.sobey.module.metric.model.Metric;
import com.sobey.module.metric.service.MetricService;
import com.sobey.module.packages.model.Packages;
import com.sobey.module.packages.service.PackagesService;
import com.sobey.module.product.enumeration.HideType;
import com.sobey.module.product.enumeration.HotType;
import com.sobey.module.product.enumeration.LanchType;
import com.sobey.module.product.enumeration.PackagesNotifyType;
import com.sobey.module.product.enumeration.ProductState;
import com.sobey.module.product.enumeration.SaleChannel;
import com.sobey.module.product.enumeration.SaleMode;
import com.sobey.module.product.enumeration.SaleType;
import com.sobey.module.product.enumeration.ShelfType;
import com.sobey.module.product.enumeration.VoucherType;
import com.sobey.module.product.model.Product;
import com.sobey.module.product.service.ProductService;
import com.sobey.module.productPrivilege.model.ProductPrivilege;
import com.sobey.module.productPrivilege.service.ProductPrivilegeService;
import com.sobey.module.relationProduct.model.RelationProduct;
import com.sobey.module.relationProduct.service.RelationProductService;
import com.sobey.module.resource.model.Resource;
import com.sobey.module.resource.service.business.ResourceService;
import com.sobey.module.resource.service.feign.ResourceFeignService;
import com.sobey.module.version.model.Version;
import com.sobey.module.version.service.VersionService;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.business.identity.Identity;
import com.sobey.util.common.ToolKit;
import com.sobey.util.common.file.FileUtil;
import com.sobey.util.common.json.JsonKit;
import com.sobey.util.common.uuid.UUIDUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/products")
@Api(value = "商品", description = "商品相关接口")
public class ProductController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ProductService ps;
	@Autowired
	private MediaService ms;
	@Autowired
	private BrightSpotService bs;
	@Autowired
	private FunctionService fs;
	@Autowired
	private CooperationCaseService cs;
	@Autowired
	private VersionService vs;
	@Autowired
	private ProductPrivilegeService pps;
	@Autowired
	private MetricService mts;
	@Autowired
	private PackagesService pks;
	
	@Autowired
	private RelationProductService rps;
	
	@Autowired
	private ResourceFeignService resourceFeign;
	@Autowired
	private ResourceService resourceBusi;

	@ApiOperation(value = "商品分页查询", notes = "商品分页查询接口")
	@PassToken
	@GetMapping
	public Page<Product> page(@RequestParam int page, @RequestParam int size, Product entity) {
		Page<Product> page1 = new Page<Product>(page, size);
		ps.page(page1, entity);
		return page1;
	}
	
	@ApiOperation(value = "列表")
	@PassToken
	@GetMapping("/list")
	public List<Product> list(Product entity) {
		List<Product> list = this.ps.list(entity);
		return list;
	}

	@ApiOperation(value = "商品查询", notes = "商品查询接口")
	@PassToken
	@GetMapping("/{uuid}")
	public List<Product> list(@PathVariable(value = "uuid") String uuid) {

		Product pt = new Product();
		pt.setUuid(uuid);
		List<Product> list = ps.list(pt);
		return list;
	}

	@ApiOperation(value = "商品统计", notes = "商品统计")
	@GetMapping("/statistic")
	public List<HashMap<String, Object>> statistic() {

		List<HashMap<String, Object>> list = ps.statistic();
		return list;
	}
	
	
	@ApiOperation(value = "商品删除", notes = "商品删除")
	@DeleteMapping("/{uuid}")
	public void delete(@PathVariable(name = "uuid") String uuid) {
		Product pt = new Product();
		pt.setUuid(uuid);
		List<Product> list = ps.list(pt);
		
		if (list == null) {
			throw new AppException(ExceptionType.PRODUCT_NOT_EXIST);
		}
		
		String auth = HeaderUtil.getAuth();
		String site = Identity.PUBLIC_SITE_CODE.getCode();
		
		try {
			for (Product itm : list) {
				
				String productId = itm.getUuid();
				
				
				CooperationCase cs = new CooperationCase();
				cs.setProductId(productId);
				this.cs.remove(cs);
				
				Function ft = new Function();
				ft.setProductId(productId);
				this.fs.remove(ft);
				
				BrightSpot spot = new BrightSpot();
				spot.setProductId(productId);
				this.bs.remove(spot);
				
				ProductPrivilege pp = new ProductPrivilege();
				pp.setProductId(productId);
				this.pps.remove(pp);
				
				Version version = new Version();
				version.setProductId(productId);
				this.vs.remove(version);
				
				Media media = new Media();
				media.setProductId(productId);
				this.ms.remove(media);
				
				Packages pk = new Packages();
				pk.setProductId(productId);
				this.pks.remove(pk);
				
				//删除素材父目录
				String destPath = itm.getMediaDir();
				FileUtil.forceDelete(destPath);
				
				this.ps.deleteById(itm.getUuid());
				
				//不再同步，暂时注释。删除商品对应的资源
//				String resourceId = itm.getResourceId();
//				if (StringUtils.isNotEmpty(resourceId)) {
//					this.resourceBusi.deleteFromAuthCenter(Long.valueOf(resourceId), true, auth, site);
//				}
			}
			
		} catch (Exception e) {
			log.error("删除商品报错", e);
		}
		
		
		
	}

	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * @param pt
	 */
	@ApiOperation(value = "商品新增", notes = "商品新增")
	@PostMapping
	@Transactional
	public void insert(@Validated  @RequestBody Product entity, BindingResult bindingResult) {
		
		ToolKit.validData(bindingResult);
		
		if (entity == null) {
			throw new AppException(ExceptionType.PRODUCT_NOT_NULL);
		}
		String name = entity.getName();
		String code = entity.getCode();
		if (StringUtils.isBlank(name)||StringUtils.isBlank(code)) {
			throw new AppException("商品名称和编码不能为空");
		}

		String destPath = entity.getMediaDir();
		// 上传文件路径的列表
		String productId = entity.getUuid();
		
		String auth = HeaderUtil.getAuth();
		String site = Identity.PUBLIC_SITE_CODE.getCode();
		Long resourceId = null;
		try {

			
			// C暂时不需要这块逻辑，新增一个以商品名为名称的资源
//			Resource resource = resourceBusi.warpResource(entity.getName(),null,true);
//			resourceId = this.resourceFeign.add(resource, auth,site);
//
//			entity.setResourceId(resourceId.toString());
//			entity.setResourceCode(resource.getCode());
			this.ps.insert(entity);
			
			
			List<RelationProduct> relations = entity.getRelations();
			this.rps.insertBatch(relations,productId);
			
			// C处理商品亮点
			List<BrightSpot> spots = entity.getSpots();
			this.bs.insertBatch(spots, productId);

			// C处理商品功能
			List<Function> fts = entity.getFunctions();
			this.fs.insertBatch(fts, productId);

			// C处理商品案例
			List<CooperationCase> cases = entity.getCases();
			this.cs.insertBatch(cases, productId);

			List<Version> versions = entity.getVersions();
			this.vs.insertBatch(versions, productId);
			
			List<Metric> metrics = entity.getMetrics();
			this.mts.insertBatch(metrics, productId);
			
			List<Packages> packages = entity.getPackages();
			this.pks.insertBatch(packages, productId);

			List<ProductPrivilege> privileges = entity.getPrivileges();
			pps.insertBatch(privileges, productId,resourceId);

		} catch (Exception e) {
			// C连父目录一起删除
			FileUtil.forceDelete(destPath);
			//不需要同步到认证中心，所以这里不用再删除以商品名为名称的资源，暂时注释
//			this.resourceBusi.deleteFromAuthCenter(resourceId, true, auth, site);
			throw new AppException(ExceptionType.PRODUCT_SAVE, e);
		}
	}
	
	@ApiOperation(value = "商品预新增")
	@GetMapping("/pre")
	public Product preInsert() {
		
		Product pt = new Product();
		
		String destPath = this.ps.getMediaDir();
		pt.setMediaDir(destPath);
		//C新增的时候才生成uuid
		String uuid = UUIDUtils.simpleUuid();
		pt.setUuid(uuid);
		String logoUuid = UUIDUtils.simpleUuid();
		pt.setLogo(logoUuid);
		String masterUuid = UUIDUtils.simpleUuid();
		pt.setMasterGraph(masterUuid);
		String bannerUuid = UUIDUtils.simpleUuid();
		pt.setBanner(bannerUuid);
		String sceneUuid = UUIDUtils.simpleUuid();
		pt.setScene(sceneUuid);
		String userUuid = UUIDUtils.simpleUuid();
		pt.setUserGuide(userUuid);
		String developUuid = UUIDUtils.simpleUuid();
		pt.setDevelopGuide(developUuid);
		String priceUuid = UUIDUtils.simpleUuid();
		pt.setPriceTable(priceUuid);
		String priceLimited = UUIDUtils.simpleUuid();
		pt.setPriceLimited(priceLimited);
		
		return pt;

	}

	
	
	
	@ApiOperation(value = "商品修改", notes = "商品修改")
	@PatchMapping
	@Transactional
	public void update(@RequestBody Product entity) {
		
		String uuid = entity.getUuid();
		Product dbPt = this.ps.selectById(uuid);
		if (dbPt == null) {
			throw new AppException(ExceptionType.PRODUCT_NOT_EXIST);
		}
		
		
		String resourceId = dbPt.getResourceId();
		String auth = HeaderUtil.getAuth();
		String site = Identity.PUBLIC_SITE_CODE.getCode();
		try {
			
			this.ps.updateById(entity);
			
			/**
			 * 暂时不再需要这个逻辑
			 * 
			 */
//			String name = entity.getName();
//			if (StringUtils.isNotBlank(name)) {
//				// C修改一个以商品名为名称的资源
//				Resource resource = resourceBusi.warpResource(name,dbPt.getResourceId(),dbPt.getResourceCode());
//				this.resourceFeign.update(resource, auth,site);
//			}
			
			
			// C处理关联商品	
			List<RelationProduct> relations = entity.getRelations();
			this.rps.updateBatch(relations,uuid);
			
			// C处理商品亮点
			List<BrightSpot> spots = entity.getSpots();
			this.bs.updateBatch(spots);
			
			// C处理商品功能
			List<Function> fts = entity.getFunctions();
			this.fs.updateBatch(fts);
			
			// C处理商品案例
			List<CooperationCase> cases = entity.getCases();
			this.cs.updateBatch(cases);
			
			
			List<Version> versions = entity.getVersions();
			this.vs.updateBatch(versions,uuid);
			
			
			List<Metric> metrics = entity.getMetrics();
			this.mts.updateBatch(metrics);
			
			List<Packages> packages = entity.getPackages();
			this.pks.updateBatch(packages, uuid);
			
			List<ProductPrivilege> privileges = entity.getPrivileges();
			pps.updateBatch(privileges, uuid, null);
			
		} catch (Exception e) {
			throw new AppException(ExceptionType.PRODUCT_UPDATE, e);
		}
	}
	
	
	@ApiOperation(value = "商品状态")
	@GetMapping("/product-type")
	@PassToken
	public String getType() {
		
		String json = JsonKit.enumToJson(ProductState.class);
		return json;
	}
	
	@ApiOperation(value = "包年包月,按量")
	@GetMapping("/sale-channel")
	@PassToken
	public String getSaleChannel() {
		
		String json = JsonKit.enumToJson(SaleChannel.class);
		return json;
	}
	
	@ApiOperation(value = "包年,包月")
	@GetMapping("/sale-mode")
	@PassToken
	public String getSaleMode() {
		
		String json = JsonKit.enumToJson(SaleMode.class);
		return json;
	}
	
	@ApiOperation(value = "公开销售,非公开销售")
	@GetMapping("/sale-type")
	@PassToken
	public String getSaleType() {
		
		String json = JsonKit.enumToJson(SaleType.class);
		return json;
	}
	
	@ApiOperation(value = "字段隐藏显示")
	@GetMapping("/hide-type")
	@PassToken
	public String getHideType() {
		
		String json = JsonKit.enumToJson(HideType.class);
		return json;
	}
	
	@ApiOperation(value = "是否是热点商品枚举")
	@GetMapping("/hot-type")
	@PassToken
	public String getHotType() {
		
		String json = JsonKit.enumToJson(HotType.class);
		return json;
	}
	@ApiOperation(value = "是否是新上架商品枚举")
	@GetMapping("/lanch-type")
	@PassToken
	public String getLanchType() {
		
		String json = JsonKit.enumToJson(LanchType.class);
		return json;
	}
	
	@ApiOperation(value = "是否支持带金券的枚举")
	@GetMapping("/voucher-type")
	@PassToken
	public String getVoucherType() {
		
		String json = JsonKit.enumToJson(VoucherType.class);
		return json;
	}
	
	@ApiOperation(value = "是否启用套餐包通知")
	@GetMapping("/packages-notify-type")
	@PassToken
	public String getPackagesNotifyType() {
		
		String json = JsonKit.enumToJson(PackagesNotifyType.class);
		return json;
	}
	
	
	@ApiOperation(value = "是否上架凌云商城")
	@GetMapping("/shelf-type")
	@PassToken
	public String getShelfType() {
		
		String json = JsonKit.enumToJson(ShelfType.class);
		return json;
	}
	
	

	

}
