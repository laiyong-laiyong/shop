package com.sobey.module.stationMsg.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Author WCY
 * @CreateTime 2020/4/14 14:18
 * 公告类消息
 */
@TableName("mall_msg_proclamation")
@ApiModel
public class MallMsgProclamation extends SuperModel<MallMsgProclamation> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableField("title")
    @ApiModelProperty(notes = "公告标题",example = "xxx")
    private String title;

    @TableField("content")
    @ApiModelProperty(notes = "公告内容",example = "xxx")
    private String content;

    @TableField("typeCode")
    @ApiModelProperty(notes = "公告类型编码",example = "8")
    private String typeCode;

    @TableField("typeDesc")
    @ApiModelProperty(notes = "公告类型",example = "系统公告")
    private String typeDesc;

    @TableField("publishStatus")
    @ApiModelProperty(notes = "发布状态 0-未发布 1-已发布 2-已撤销",example = "1")
    private String publishStatus;

    @TableField("promotes")
    @ApiModelProperty(notes = "推广类型,多个类型code用','(英文半角)分割")
    private String promotes;
    
    
    @TableField("cover")
    @ApiModelProperty(notes = "封面图片")
    private String cover;

    @TableField("createTime")
    @ApiModelProperty(notes = "",example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+08")
    private Date createTime;

    @TableField("publishTime")
    @ApiModelProperty(notes = "",example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+08")
    private Date publishTime;

    @TableField("updateTime")
    @ApiModelProperty(notes = "更新时间",example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+08")
    private Date updateTime;

    public String getPromotes() {
        return promotes;
    }

    public void setPromotes(String promotes) {
        this.promotes = promotes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	/**
	 * @return the cover
	 */
	public String getCover() {
		return cover;
	}

	/**
	 * @param cover the cover to set
	 */
	public void setCover(String cover) {
		this.cover = cover;
	}
    
    
}
