package com.vp.vpbackend.handler;

import cn.dev33.satoken.exception.NotLoginException;
import com.vp.vpbackend.POJO.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理未登录异常
    @ExceptionHandler(NotLoginException.class)
    public Result handleNotLoginException(NotLoginException e,
                                          HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return new Result(401, "Please login.", e.getType());
    }
}
