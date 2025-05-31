package com.vp.vpbackend.service;

import cn.dev33.satoken.stp.StpUtil;
import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.common.AliyunSTS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OssService {

    public Result getSts() {
        if(StpUtil.isLogin()) {
            return new Result(200, "OK", AliyunSTS.getStsCredentials());
        }
        return new Result(400, "Fail", null);
    }
}
