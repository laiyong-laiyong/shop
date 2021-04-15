package com.sobey.module.mallPack.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/5/20 9:51
 * 购买套餐包后生成的套餐包信息
 */
@TableName(value = "mall_pack")
@ApiModel
public class MallPack extends SuperModel<MallPack> {

    @TableField(value = "siteCode")
    @ApiModelProperty(notes = "站点")
    private String siteCode;

    @TableField(value = "accountId")
    @ApiModelProperty(notes = "用户userCode")
    private String accountId;//购买用户id

    @TableField(value = "account")
    @ApiModelProperty(notes = "用户名")
    private String account;//购买用户

    @TableField(value = "packUuid")
    @ApiModelProperty(notes = "套餐包id")
    private String packUuid;

    @TableField(value = "packCode")
    @ApiModelProperty(notes = "套餐包编码")
    private String packCode;

    @TableField(value = "packName")
    @ApiModelProperty(notes = "套餐包名称")
    private String packName;

    @TableField(value = "productId")
    @ApiModelProperty(notes = "商品id")
    private String productId;

    @TableField(value = "productName")
    @ApiModelProperty(notes = "商品名称")
    private String productName;

    @TableField(value = "duration")
    @ApiModelProperty(notes = "有效时长",example = "12")
    private Integer duration;

    @TableField(value = "unit")
    @ApiModelProperty(notes = "有效时长单位",example = "2")
    private String unit;

    @TableField(value = "createDate",fill = FieldFill.INSERT)
    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //yyyy-MM-dd HH:mm:ss

    @TableField(value = "effectiveDate")
    @ApiModelProperty(notes = "生效时间,前端不传该参数")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date effectiveDate;//生效时间

    @TableField(value = "expireDate")
    @ApiModelProperty(notes = "失效时间,前端不传该参数")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expireDate;//失效时间

    @TableField(value = "isEffective")
    @ApiModelProperty(notes = "是否生效,前端不传该参数")
    private String isEffective;//是否生效

    @TableField(exist = false)
    @ApiModelProperty(notes = "资源信息")
    private List<MallPackResource> resources;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPackUuid() {
        return packUuid;
    }

    public void setPackUuid(String packUuid) {
        this.packUuid = packUuid;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(String isEffective) {
        this.isEffective = isEffective;
    }

    public List<MallPackResource> getResources() {
        return resources;
    }

    public void setResources(List<MallPackResource> resources) {
        this.resources = resources;
    }
}
