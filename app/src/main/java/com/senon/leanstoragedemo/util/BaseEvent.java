package com.senon.leanstoragedemo.util;

/**
 * eventbus发送实体
 */
public class BaseEvent<T> {

    /**
     * 1：学员签到
     * 2：学员签到修改
     * 3：学员充值
     * 4：学员删除历史记录
     */
    private int code;
    private String msg;
    private boolean flag;
    private T data;
    private int position;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
