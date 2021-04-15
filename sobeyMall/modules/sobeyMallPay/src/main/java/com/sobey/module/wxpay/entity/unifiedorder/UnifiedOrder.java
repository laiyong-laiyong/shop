package com.sobey.module.wxpay.entity.unifiedorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description 统一下单
 * @Author WuChenYang
 * @Since 2020/1/14 10:35
 */
@ApiModel(description = "微信支付实体类")
public class UnifiedOrder {

    @ApiModelProperty(notes = "服务商appid")
    private String appid;//服务商appid
    @ApiModelProperty(notes = "微信支付分配的商户号")
    private String mch_id;//微信支付分配的商户号
    @ApiModelProperty(notes = "微信分配的子商户公众账号ID,如需在支付完成后获取sub_openid则此参数必传")
    private String sub_appid;//微信分配的子商户公众账号ID,如需在支付完成后获取sub_openid则此参数必传.
    @ApiModelProperty(notes = "微信支付分配的子商户号")
    private String sub_mch_id;//微信支付分配的子商户号
    @ApiModelProperty(notes = "终端设备号,PC网页或JSAPI支付请传WEB")
    private String device_info;//终端设备号(门店号或收银设备ID)，注意：PC网页或JSAPI支付请传"WEB"
    @ApiModelProperty(notes = "是否开具发票,Y或N--传入Y时,支付成功消息和支付详情页将出现开票入口,需要在微信支付商户平台或微信公众平台开通电子发票功能,传此字段才可生效")
    private String receipt;//Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效
    @ApiModelProperty(notes = "随机字符串,不长于32位")
    private String nonce_str;//随机字符串，不长于32位
    @ApiModelProperty(notes = "签名")
    private String sign;//签名
    @ApiModelProperty(notes = "签名类型,目前支持HMAC-SHA256和MD5,默认为MD5")
    private String sign_type;//签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    @ApiModelProperty(notes = "商品描述,PC网站——传入浏览器打开的网站主页title名-实际商品名称,例如:腾讯充值中心-QQ会员充值")
    private String body;//商品描述,PC网站——传入浏览器打开的网站主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
    @ApiModelProperty(notes = "商品详细描述,json字符串,非单品优惠用户可不传")
    private String detail;//商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明”
    @ApiModelProperty(notes = "附加数据,该字段主要用于商户携带订单的自定义数据,此字段用于保存商品名称")
    private String attach;//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    @ApiModelProperty(notes = "商户系统内部订单号,要求32个字符内,只能是数字、大小写字母_-|*且在同一个商户号下唯一")
    private String out_trade_no;//商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。
    @ApiModelProperty(notes = "币种,默认人民币:CNY")
    private String fee_type;//符合ISO 4217标准的三位字母代码，默认人民币：CNY
    @ApiModelProperty(notes = "总金额,单位:分,只能为整数",example = "100",dataType = "java.lang.Integer")
    private Integer total_fee;//订单总金额,单位为(分),只能为整数
    @ApiModelProperty(notes = "调用微信支付API的机器IP")
    private String spbill_create_ip;//支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
    @ApiModelProperty(notes = "订单生成时间, 格式为yyyyMMddHHmmss")
    private String time_start;//订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
    /**
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
     * 订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期
     * 所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id.
     * time_expire只能第一次下单传值，不允许二次修改，二次修改系统将报错。
     * 如用户支付失败后，需再次支付，需更换原订单号重新下单。
     */
    @ApiModelProperty(notes = "订单失效时间,格式为yyyyMMddHHmmss")
    private String time_expire;
    @ApiModelProperty(notes = "订单优惠标记,代金券或立减优惠功能的参数")
    private String goods_tag;//订单优惠标记，代金券或立减优惠功能的参数
    @ApiModelProperty(notes = "接收微信支付异步通知回调地址,通知url必须为直接可访问的url，不能携带参数")
    private String notify_url;//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数
    @ApiModelProperty(notes = "交易类型, JSAPI-JSAPI支付 NATIVE-Native支付 APP-APP支付 MWEB-H5支付")
    private String trade_type;//交易类型,JSAPI-JSAPI支付 NATIVE-Native支付 APP-APP支付
    @ApiModelProperty(notes = "商品ID, trade_type=NATIVE时,此参数必传")
    private String product_id;//商品ID，trade_type=NATIVE时，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
    @ApiModelProperty(notes = "指定支付方式, eg:no_credit--指定不能使用信用卡支付")
    private String limit_pay;//指定支付方式，no_credit--指定不能使用信用卡支付
    @ApiModelProperty(notes = "用户标识, trade_type=JSAPI,此参数必传,用户在主商户appid下的唯一标识")
    private String openid;//用户标识,trade_type=JSAPI，此参数必传，用户在主商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
    @ApiModelProperty(notes = "用户子标识, trade_type=JSAPI,此参数必传,用户在子商户appid下的唯一标识")
    private String sub_openid;//用户子标识，trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
    @ApiModelProperty(notes = "场景信息, 该字段常用于线下活动时的场景信息上报")
    private String scene_info;//场景信息，该字段常用于线下活动时的场景信息上报，支持上报实际门店信息，商户也可以按需求自己上报相关信息。该字段为JSON对象数据，对象格式为{"store_info":{"id": "门店ID","name": "名称","area_code": "编码","address": "地址" }}

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSub_appid() {
        return sub_appid;
    }

    public void setSub_appid(String sub_appid) {
        this.sub_appid = sub_appid;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    public String getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getLimit_pay() {
        return limit_pay;
    }

    public void setLimit_pay(String limit_pay) {
        this.limit_pay = limit_pay;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSub_openid() {
        return sub_openid;
    }

    public void setSub_openid(String sub_openid) {
        this.sub_openid = sub_openid;
    }

    public String getScene_info() {
        return scene_info;
    }

    public void setScene_info(String scene_info) {
        this.scene_info = scene_info;
    }
}
