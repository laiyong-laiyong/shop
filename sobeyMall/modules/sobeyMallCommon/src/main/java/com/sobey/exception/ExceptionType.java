package com.sobey.exception;

/**
 * 
 * @author lgc
 * @date 2020年1月17日 上午11:22:54
 */
public enum ExceptionType {

	/**
	 * 系统级异常以SYS_打头,这里关于token的错误码统一为：system.0003。
	 */
	SYS_RUNTIME("system.0001","系统异常"),
	SYS_REST("system.0002","rest服务调用失败"),
	SYS_TOKEN("system.0003","token过期或不匹配,请检查"),
	SYS_TOKEN_AUTH_CENTER("system.0003","认证中心检测到401错误,"),
	SYS_TOKEN_NOT_EXIST("system.0003","token不存在"),
	SYS_TOKEN_NOT_SIGN_IN_AUTH_CENTER("system.0003","此token不是认证中心签发"),
	SYS_TOKEN_NOT_BEEN_PARSE("system.0003","此token不能被解析，请检查token"),
	SYS_PARAMETER("system.0004","参数错误"),
	SYS_PASSWORD("system.0005","用户名和密码不匹配"),
	SYS_FILE_UPLOAD("system.0007","文件上传报错"),
	SYS_FILE_DOWNLOAD("system.0008","文件下载报错"),
	SYS_FILE_NAME_NOT_EXIST("system.0009","文件名不能为空"),
	SYS_FILE_TOO_MANY("system.0010","一次只能上传一个文件"),
	SYS_ENTITY_NOT_EXIST("system.0010","实体不存在"),
	
	/**
	 * 商品服务异常,以PRODUCT_打头
	 */
	PRODUCT_SAVE("product.0001","新增商品失败"),
	PRODUCT_UPDATE("product.0001","修改商品失败"),
	PRODUCT_NOT_EXIST("product.0001","商品不存在"),
	PRODUCT_NOT_NULL("product.0002","商品不能为空"),
	PRODUCT_MEDIA_NOT_NULL("product.0003","商品素材不能为空"),
	
	/**
	 * 支付服务异常,以PAY_打头
	 */
	PAY_CODE_WX_PAY("pay.0001","微信支付异常"),
	PAY_CODE_ZFB_PAY("pay.0002","支付宝支付异常"),
	PAY_CODE_BAL_PAY("pay.0003","余额服务异常"),

	/**
	 * 订单服务异常,以ORDER_打头
	 */
	ORDER_CODE("order.0001","订单编号不能为空"),
	ORDER_CODE_PARAM("order.0002","参数错误"),
	ORDER_CODE_UPDATE("order.0003","订单更新异常"),
	ORDER_CODE_INSERT("order.0004","创建订单异常"),
	ORDER_CODE_QUERY("order.0005","查询订单异常"),
	ORDER_CODE_OPEN_FAIL("order.0006","商品开通失败"),
	ORDER_CODE_RENEW_FAIL("order.0007","商品续费失败"),

	/**
	 * 工单服务异常,以work_order打头
	 */
	WORK_ORDER_SAVE("work_order.0001","新增工单失败"),
	WORK_ORDER_NOT_EXIST("work_order.0002","工单不存在"),
	WORK_ORDER_DELETE("work_order.0002","删除工单失败");
	


	ExceptionType(String code,String message) {
		this.code = code;
		this.message = message;
	}

	//应用级别错误代码
	private String code;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	
}
