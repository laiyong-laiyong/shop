package com.sobey.module.topic.model.request.openDetail;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/17 10:53
 */
public class Charge {
    private String id;//计费编码
    private String typeCode;//计费模式编码
    private String type;//计费模式描述

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
