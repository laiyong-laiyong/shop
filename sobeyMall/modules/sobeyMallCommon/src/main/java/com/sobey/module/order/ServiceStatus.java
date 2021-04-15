package com.sobey.module.order;

/**
 * @Description 服务状态
 * @Author WuChenYang
 * @CreateTime 2020/3/13 17:24
 */
public enum ServiceStatus {

    Closed("0","已冻结"),
    Normal("1","正常"),
    Opening("2","开通中"),
    OpenFail("3","开通失败"),
    Destroy("4","已销毁");
    private String code;
    private String desc;

    /**
     * 判断是否购买过，除开通失败和以后要加的已销毁状态外都算购买过
     * @param code
     * @return
     */
    public static boolean isEverBought(String code){
        if (Normal.getCode().equals(code)){
            return true;
        }
        if (Opening.getCode().equals(code)){
            return true;
        }
        if (Closed.getCode().equals(code)){
            return true;
        }
        return false;
    }

    ServiceStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
