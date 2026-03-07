package com.wzc.lease.common.result; // 声明当前类所在的包

import lombok.Data; // 导入 Lombok 的 @Data 注解

/**
 * 全局统一返回结果类
 */
@Data // 自动生成 getter、setter、toString、equals、hashCode 等方法
public class Result<T> { // 定义统一返回类，T 表示 data 的泛型类型

    // 返回状态码
    private Integer code; // 例如 200 成功、201 失败

    // 返回提示信息
    private String message; // 例如 "成功"、"失败"

    // 返回业务数据
    private T data; // 具体返回内容，类型由泛型 T 决定

    // 无参构造方法
    public Result() { // 供框架或工具创建空对象
    } // 构造方法结束

    // 基础构建方法：创建 Result 并按需设置 data
    private static <T> Result<T> build(T data) { // 泛型静态方法，入参是要返回的数据
        Result<T> result = new Result<>(); // 创建一个新的结果对象
        if (data != null) // 只有 data 不为空时才设置
            result.setData(data); // 给结果对象写入 data
        return result; // 返回构建好的结果对象
    } // build(data) 方法结束

    // 完整构建方法：在基础构建上补齐 code 和 message
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) { // body 是数据，resultCodeEnum 是状态定义
        Result<T> result = build(body); // 先复用基础构建逻辑设置 data
        result.setCode(resultCodeEnum.getCode()); // 从枚举中取状态码并写入
        result.setMessage(resultCodeEnum.getMessage()); // 从枚举中取提示信息并写入
        return result; // 返回完整结果
    } // build(body, resultCodeEnum) 方法结束

    // 成功返回（携带数据）
    public static <T> Result<T> ok(T data) { // 对外提供的成功快捷方法
        return build(data, ResultCodeEnum.SUCCESS); // 使用 SUCCESS 枚举构建成功结果
    } // ok(data) 方法结束

    // 成功返回（不携带数据）
    public static <T> Result<T> ok() { // 无参成功快捷方法
        return Result.ok(null); // 复用有参 ok，data 传 null
    } // ok() 方法结束

    // 失败返回（默认不携带数据）
    public static <T> Result<T> fail() { // 对外提供的失败快捷方法
        return build(null, ResultCodeEnum.FAIL); // 使用 FAIL 枚举构建失败结果
    } // fail() 方法结束

    /**
     * 失败返回（自定义错误码和错误信息）
     * 专门给 GlobalExceptionHandler 捕获 LeaseException 时使用：
     * Result.fail(e.getCode(), e.getMessage())
     * → 把异常中的 code（如310）和 message（如"请先删除房间"）包装成统一格式返回给前端
     */
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = build(null); // 失败时不携带业务数据，data 为null
        result.setCode(code); // 设置错误码（如 310）
        result.setMessage(message); // 设置错误信息（如"请先删除房间"）
        return result;
    }
} // Result 类结束
