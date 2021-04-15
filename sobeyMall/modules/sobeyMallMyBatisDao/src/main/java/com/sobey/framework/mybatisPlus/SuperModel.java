package com.sobey.framework.mybatisPlus;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;

/**
 * @author lgc
 * @date 2020年1月19日 上午10:27:59
 * @param <T>
 */
public class SuperModel<T extends Model> extends Model<T> implements Serializable {

    protected static final long serialVersionUID = 1L;
    //固定主键

    @TableId
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public Serializable pkVal() {
        return this.uuid;
    }
}