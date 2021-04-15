package com.sobey.module.wxpay.entity.unifiedorder;

/**
 * @Description 场景信息
 * @Author WuChenYang
 * @Since 2020/1/14 11:25
 */
public class SceneInfo {

    private String id;//门店编号，由商户自定义
    private String name;//门店名称，由商户自定义
    private String area_code;//门店所在地行政区划码，详细见《最新县及县以上行政区划代码》
    private String address;//门店详细地址，由商户自定义

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
