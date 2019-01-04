package com.util;

import java.io.Serializable;

/**
 * service层返回对象列表封装
 *
 * @param <T>
 */
public class ServiceResult<T> implements Serializable {

    private boolean success = false;

    private String code;

    private String message;

    private T result;

    private ServiceResult() {
    }

    public static <T> ServiceResult<T> success(T result) {
        ServiceResult<T> item = new ServiceResult<T>();
        item.success = true;
        item.result = result;
        item.code = "0";
        item.message = "";
        return item;
    }

    public static <T> ServiceResult<T> success(T result, String message) {
        ServiceResult<T> item = new ServiceResult<T>();
        item.success = true;
        item.result = result;
        item.code = "0";
        item.message = message;
        return item;
    }

    public static <T> ServiceResult<T> failure(String errorCode, String errorMessage) {
        ServiceResult<T> item = new ServiceResult<T>();
        item.success = false;
        item.code = errorCode;
        item.message = errorMessage;
        return item;
    }

    public static <T> ServiceResult<T> failure() {
        return failure("1", "请求超时，请稍后再试");
    }

    public static <T> ServiceResult<T> failure(String message) {
        ServiceResult<T> item = new ServiceResult<T>();
        item.success = false;
        item.code = "1";
        item.message = message;
        return item;
    }

    public boolean hasResult() {
        return result != null;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
