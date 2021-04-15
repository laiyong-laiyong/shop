package com.sobey.module.product.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import com.sobey.module.brightSpot.model.BrightSpot;
import com.sobey.module.category.model.Category;
import com.sobey.module.cooperationCase.model.CooperationCase;
import com.sobey.module.function.model.Function;
import com.sobey.module.metric.model.Metric;
import com.sobey.module.packages.model.Packages;
import com.sobey.module.productPrivilege.model.ProductPrivilege;
import com.sobey.module.relationProduct.model.RelationProduct;
import com.sobey.module.version.model.Version;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_product")
@ApiModel
public class Product extends SuperModel<Product> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(example="商品名")
	@TableField(value = "name")
	private String name;

	@ApiModelProperty(example="类别编号")
	@TableField(value = "categoryId")
	private String categoryId;

	@ApiModelProperty(example="商品说明")
	@TableField(value = "desc")
	private String desc;
	
	@ApiModelProperty(example="商品概述")
	@TableField(value = "summary")
	private String summary;
	
	@ApiModelProperty(example="商品编码")
	@TableField(value = "code")
	private String code;

	@ApiModelProperty(example="销售类型：公开销售，非公开销售")
	@TableField(value = "saleType")
	private String saleType;
	
	@ApiModelProperty(example="销售渠道：包年包月,按量等")
	@TableField(value = "saleChannel")
	private String saleChannel;
	
	@ApiModelProperty(example="销售模式：包年，包月")
	@TableField(value = "saleMode")
	private String saleMode;

	@ApiModelProperty(example="定价说明")
	@TableField(value = "priceDesc")
	private String priceDesc;

	@ApiModelProperty(example="开通接口")
	@TableField(value = "openInterface")
	private String openInterface;
	
	@ApiModelProperty(example="关闭接口")
	@TableField(value = "closeInterface")
	private String closeInterface;
	
	@ApiModelProperty(example="续费接口")
	@TableField(value = "renewInterface")
	private String renewInterface;

	@ApiModelProperty(example="计费接口")
	@TableField(value = "chargingInterface")
	private String chargingInterface;

	@ApiModelProperty(example="控制台地址")
	@TableField(value = "console")
	private String console;

	@ApiModelProperty(example="产品素材的主目录")
	@TableField(value = "mediaDir")
	private String mediaDir;
	
	@ApiModelProperty(example="商品状态")
	@TableField(value = "state")
	private String state;
	
	@ApiModelProperty(example="商品对应的认证中心的资源Id")
	@TableField(value = "resourceId")
	private String resourceId;
	
	@ApiModelProperty(example="商品logo,关联media表中的mediaId")
	@TableField(value = "logo")
	private String logo;
	
	@ApiModelProperty(example="商品主图uuid")
	@TableField(value = "masterGraph")
	private String masterGraph;
	
	@ApiModelProperty(example="商品横幅uuid")
	@TableField(value = "banner")
	private String banner;
	
	@ApiModelProperty(example="应用场景图片uuid")
	@TableField(value = "scene")
	private String scene;
	
	@ApiModelProperty(example="用户手册uuid")
	@TableField(value = "userGuide")
	private String userGuide;
	
	@ApiModelProperty(example="开发手册uuid")
	@TableField(value = "developGuide")
	private String developGuide;
	
	@ApiModelProperty(example="定价表uuid")
	@TableField(value = "priceTable")
	private String priceTable;
	
	@ApiModelProperty(example="限价表uuid")
	@TableField(value = "priceLimited")
	private String priceLimited;
	
	@ApiModelProperty(example="")
	@TableField(value = "resourceCode")
	private String resourceCode;

	@ApiModelProperty(hidden=true)
	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@ApiModelProperty(hidden=true)
	@TableField(value = "updateDate", fill = FieldFill.UPDATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;

	@ApiModelProperty(example="")
	@TableField(value = "price")
	private BigDecimal price;
	
	@NotBlank(message="createUserCode不能为空")
	@TableField(value = "createUserCode")
	@ApiModelProperty(example="创建者编码")
	private String createUserCode;
	
	
	@TableField(value = "destroyInterface")
	@ApiModelProperty(example="销毁接口")
	private String destroyInterface;
	
	@TableField(value = "hot")
	@ApiModelProperty(example="是否是热点商品")
	private String hot;
	
	@TableField(value = "newLaunch")
	@ApiModelProperty(example="是否是新上架的商品")
	private String newLaunch;
	
	
	@TableField(value = "shelf")
	@ApiModelProperty(example="是否上架凌云商城")
	private String shelf;
	
	
	
	
	@TableField(value = "voucher")
	@ApiModelProperty(example="是否支持代金券")
	private String voucher;
	
	@TableField(value = "enablePackagesNotify")
	@ApiModelProperty(example="是否启用套餐包通知")
	private String enablePackagesNotify;
	
	@TableField(value = "notifyPackagesUrl")
	@ApiModelProperty(example="套餐包通知url")
	private String notifyPackagesUrl;
	
	@NotBlank(message="siteCode不能为空")
	@TableField(value = "siteCode")
	@ApiModelProperty(example="站点code")
	private String siteCode;
	
	
	@TableField(value = "enableRelation")
	@ApiModelProperty(example="是否启用关联商品，1：启用，2：不启用")
	private String enableRelation;

	/**
	 * 以下字段在数据库中不存在，供页面查询用
	 * 
	 */
	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date startDate;

	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date endDate;

	@TableField(exist = false)
	private Category category;
	
	

	/**
	 */

	@TableField(exist = false)
	private List<BrightSpot> spots;

	@TableField(exist = false)
	private List<Function> functions;

	@TableField(exist = false)
	private List<CooperationCase> cases;

	@TableField(exist = false)
	private List<Version> versions;

	@TableField(exist = false)
	private List<ProductPrivilege> privileges;
	
	@TableField(exist = false)
	private List<Metric> metrics;
	
	
	@TableField(exist = false)
	@ApiModelProperty(notes="套餐列表")
	private List<Packages> packages;
	
	
	@TableField(exist = false)
	@ApiModelProperty(example="创建者名称")
	private String createUserName;
	
	
	@TableField(exist = false)
	private List<RelationProduct> relations;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	public List<BrightSpot> getSpots() {
		return spots;
	}

	public void setSpots(List<BrightSpot> spots) {
		this.spots = spots;
	}

	public List<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

	public List<CooperationCase> getCases() {
		return cases;
	}

	public void setCases(List<CooperationCase> cases) {
		this.cases = cases;
	}


	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getPriceDesc() {
		return priceDesc;
	}

	public void setPriceDesc(String priceDesc) {
		this.priceDesc = priceDesc;
	}

	public String getOpenInterface() {
		return openInterface;
	}

	public void setOpenInterface(String openInterface) {
		this.openInterface = openInterface;
	}

	public String getChargingInterface() {
		return chargingInterface;
	}

	public void setChargingInterface(String chargingInterface) {
		this.chargingInterface = chargingInterface;
	}

	public String getConsole() {
		return console;
	}

	public void setConsole(String console) {
		this.console = console;
	}

	public List<Version> getVersions() {
		return versions;
	}

	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}

	public List<ProductPrivilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<ProductPrivilege> privileges) {
		this.privileges = privileges;
	}

	public String getMediaDir() {
		return mediaDir;
	}

	public void setMediaDir(String mediaDir) {
		this.mediaDir = mediaDir;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return the masterGraph
	 */
	public String getMasterGraph() {
		return masterGraph;
	}

	/**
	 * @param masterGraph the masterGraph to set
	 */
	public void setMasterGraph(String masterGraph) {
		this.masterGraph = masterGraph;
	}

	/**
	 * @return the banner
	 */
	public String getBanner() {
		return banner;
	}

	/**
	 * @param banner the banner to set
	 */
	public void setBanner(String banner) {
		this.banner = banner;
	}

	/**
	 * @return the scene
	 */
	public String getScene() {
		return scene;
	}

	/**
	 * @param scene the scene to set
	 */
	public void setScene(String scene) {
		this.scene = scene;
	}

	/**
	 * @return the userGuide
	 */
	public String getUserGuide() {
		return userGuide;
	}

	/**
	 * @param userGuide the userGuide to set
	 */
	public void setUserGuide(String userGuide) {
		this.userGuide = userGuide;
	}

	

	/**
	 * @return the developGuide
	 */
	public String getDevelopGuide() {
		return developGuide;
	}

	/**
	 * @param developGuide the developGuide to set
	 */
	public void setDevelopGuide(String developGuide) {
		this.developGuide = developGuide;
	}

	

	/**
	 * @return the priceTable
	 */
	public String getPriceTable() {
		return priceTable;
	}

	/**
	 * @param priceTable the priceTable to set
	 */
	public void setPriceTable(String priceTable) {
		this.priceTable = priceTable;
	}

	


	/**
	 * @return the saleChannel
	 */
	public String getSaleChannel() {
		return saleChannel;
	}

	/**
	 * @param saleChannel the saleChannel to set
	 */
	public void setSaleChannel(String saleChannel) {
		this.saleChannel = saleChannel;
	}

	/**
	 * @return the saleMode
	 */
	public String getSaleMode() {
		return saleMode;
	}

	/**
	 * @param saleMode the saleMode to set
	 */
	public void setSaleMode(String saleMode) {
		this.saleMode = saleMode;
	}

	/**
	 * @return the closeInterface
	 */
	public String getCloseInterface() {
		return closeInterface;
	}

	/**
	 * @param closeInterface the closeInterface to set
	 */
	public void setCloseInterface(String closeInterface) {
		this.closeInterface = closeInterface;
	}

	/**
	 * @return the renewInterface
	 */
	public String getRenewInterface() {
		return renewInterface;
	}

	/**
	 * @param renewInterface the renewInterface to set
	 */
	public void setRenewInterface(String renewInterface) {
		this.renewInterface = renewInterface;
	}

	/**
	 * @return the metrics
	 */
	public List<Metric> getMetrics() {
		return metrics;
	}

	/**
	 * @param metrics the metrics to set
	 */
	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the createUserCode
	 */
	public String getCreateUserCode() {
		return createUserCode;
	}

	/**
	 * @param createUserCode the createUserCode to set
	 */
	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * @return the packages
	 */
	public List<Packages> getPackages() {
		return packages;
	}

	/**
	 * @param packages the packages to set
	 */
	public void setPackages(List<Packages> packages) {
		this.packages = packages;
	}

	

	/**
	 * @return the destroyInterface
	 */
	public String getDestroyInterface() {
		return destroyInterface;
	}

	/**
	 * @param destroyInterface the destroyInterface to set
	 */
	public void setDestroyInterface(String destroyInterface) {
		this.destroyInterface = destroyInterface;
	}

	/**
	 * @return the hot
	 */
	public String getHot() {
		return hot;
	}

	/**
	 * @param hot the hot to set
	 */
	public void setHot(String hot) {
		this.hot = hot;
	}

	/**
	 * @return the newLaunch
	 */
	public String getNewLaunch() {
		return newLaunch;
	}

	/**
	 * @param newLaunch the newLaunch to set
	 */
	public void setNewLaunch(String newLaunch) {
		this.newLaunch = newLaunch;
	}

	/**
	 * @return the voucher
	 */
	public String getVoucher() {
		return voucher;
	}

	/**
	 * @param voucher the voucher to set
	 */
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

	/**
	 * @return the priceLimited
	 */
	public String getPriceLimited() {
		return priceLimited;
	}

	/**
	 * @param priceLimited the priceLimited to set
	 */
	public void setPriceLimited(String priceLimited) {
		this.priceLimited = priceLimited;
	}

	/**
	 * @return the enablePackagesNotify
	 */
	public String getEnablePackagesNotify() {
		return enablePackagesNotify;
	}

	/**
	 * @param enablePackagesNotify the enablePackagesNotify to set
	 */
	public void setEnablePackagesNotify(String enablePackagesNotify) {
		this.enablePackagesNotify = enablePackagesNotify;
	}

	/**
	 * @return the notifyPackagesUrl
	 */
	public String getNotifyPackagesUrl() {
		return notifyPackagesUrl;
	}

	/**
	 * @param notifyPackagesUrl the notifyPackagesUrl to set
	 */
	public void setNotifyPackagesUrl(String notifyPackagesUrl) {
		this.notifyPackagesUrl = notifyPackagesUrl;
	}

	/**
	 * @return the siteCode
	 */
	public String getSiteCode() {
		return siteCode;
	}

	/**
	 * @param siteCode the siteCode to set
	 */
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	/**
	 * @return the shelf
	 */
	public String getShelf() {
		return shelf;
	}

	/**
	 * @param shelf the shelf to set
	 */
	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	
	/**
	 * @return the relations
	 */
	public List<RelationProduct> getRelations() {
		return relations;
	}

	/**
	 * @param relations the relations to set
	 */
	public void setRelations(List<RelationProduct> relations) {
		this.relations = relations;
	}

	/**
	 * @return the enableRelation
	 */
	public String getEnableRelation() {
		return enableRelation;
	}

	/**
	 * @param enableRelation the enableRelation to set
	 */
	public void setEnableRelation(String enableRelation) {
		this.enableRelation = enableRelation;
	}

	

	
	
}
