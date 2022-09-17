package com.epidemicmanagement.config;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * @author XUE
 */
@Data
@NoArgsConstructor
public class Result extends HashMap<String, Object> {
//    private Integer code; // 状态码
//    private String msg; // 消息
//    private Object data; // 数据

    public Result(int code) {
        this.put("code", code);
    }

    public Result(int code, String msg) {
        this.put("code", code);
        this.put("msg", msg);
    }

    public Result(int code, String msg, Object data) {
        this.put("code", code);
        this.put("msg", msg);
        this.put("data", data);
    }

    public static Result success() {
        return new Result(200);
    }

    public static Result success(Integer code, String msg) {
        return new Result(code, msg);
    }

    public static Result success(Integer code, String msg, Object obj) {
        return new Result(code, msg, obj);
    }

    public static Result fail() {
        return new Result(500);
    }

    public static Result fail(Integer code, String msg) {
        return new Result(code, msg);
    }

    public static Result fail(Integer code, String msg, Object obj) {
        return new Result(code, msg, obj);
    }

    @SuppressWarnings("unchecked")
    public Result setAttribute(String key, Object value) {
        String data = "data";
        if (!this.containsKey(data)) {
            this.put(data, new HashMap<String, Object>());
        }
        ((HashMap<String, Object>) this.get(data)).put(key, value);
        return this;
    }

}
