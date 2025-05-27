package com.vp.vpbackend.service;

import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.common.AliyunSTS;
import org.springframework.stereotype.Service;

@Service
public class AliService {

    public Result getSts() {
        return new Result(200, "OK", AliyunSTS.getStsCredentials());
    }
}
