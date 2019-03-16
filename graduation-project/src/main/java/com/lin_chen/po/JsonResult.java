package com.lin_chen.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author F_lin
 * @since 2018/4/5
 **/
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class JsonResult {

    /**
     * 成功的代码
     */
    private static final int CODE_SUCCESS = 0;

    /**
     * 错误的代码，可根据错误类型进行详细分类
     */
    private static final int CODE_ERROR = -1;

    private int code;
    private String msg;
    private Object data;

    /**
     * 创建一个成功的响应
     *
     * @param data 数据
     * @return JsonResult
     */
    public static JsonResult success(Object data) {
        return new JsonResult(CODE_SUCCESS, "成功", data);
    }

    /**
     * 空的成功返回
     *
     * @return JsonResult
     */
    public static JsonResult success() {
        return success(null);
    }

    /**
     * 创建一个错误的响应
     *
     * @param msg 错误消息
     * @return JsonResult
     */
    public static JsonResult error(String msg) {
        return new JsonResult(CODE_ERROR, msg, null);
    }


    /**
     * 工厂方法
     *
     * @param code 错误码
     * @param msg  错误消息
     * @param data 数据
     * @return JsonResult
     */
    public static JsonResult result(int code, String msg, Object data) {
        return new JsonResult(code, msg, data);
    }


    /**
     * 工厂方法
     *
     * @param code 错误码
     * @param msg  错误消息
     * @return JsonResult
     */
    public static JsonResult result(int code, String msg) {
        return new JsonResult(code, msg, null);
    }


    /**
     * @param code 错误码
     * @param msg  错误消息
     * @param data 数据
     */
    public JsonResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
