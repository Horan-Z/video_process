package com.vp.vpbackend.service;

import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.common.AliyunSTS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OssService {

    public Result getSts() {
        return new Result(200, "OK", AliyunSTS.getStsCredentials());
    }
}
