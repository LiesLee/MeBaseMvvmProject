package com.common.http;

/**
 * Created by LiesLee on 2016/7/13.
 * Email: LiesLee@foxmail.com
 */
public class DataResult<T> {
    private DataStatus dataStatus;
    private int code = -1;
    private String msg;
    private T data;

    public DataResult() {
    }

    public DataResult(DataStatus dataStatus) {
        this.dataStatus = dataStatus;
    }

    public DataResult(DataStatus dataStatus, int code, String msg, T data) {
        this.dataStatus = dataStatus;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public DataStatus getDataStatus() {
        if (code == StatusCode.Success) {
            if (null == data) {
                dataStatus = DataStatus.Empty;
            }else{
                dataStatus = DataStatus.Content;
            }
            return dataStatus;
        } else {
            dataStatus = DataStatus.Error;
            return dataStatus;
        }
    }

    public void setDataStatus(DataStatus dataStatus) {
        this.dataStatus = dataStatus;
    }
}
