package com.vp.vpbackend.POJO.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthVO {
    private String username;
    private Object tokenInfo;
}