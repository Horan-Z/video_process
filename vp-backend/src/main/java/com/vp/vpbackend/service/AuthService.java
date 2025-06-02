package com.vp.vpbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vp.vpbackend.POJO.DO.UserDO;
import com.vp.vpbackend.POJO.DTO.AuthDTO;
import com.vp.vpbackend.POJO.DTO.TokenDTO;
import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.POJO.VO.AuthVO;
import com.vp.vpbackend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import cn.dev33.satoken.stp.StpUtil;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;

    public Result login(AuthDTO authDTO) {
        LambdaQueryWrapper<UserDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserDO::getUsername, authDTO.getUsername());
        UserDO user = userMapper.selectOne(lqw);
        if (user == null || !BCrypt.checkpw(authDTO.getPassword(), user.getPassword())) {
            return new Result(401, "用户名或密码错误", null);
        } else {
            StpUtil.login(user.getUuid());
            return new Result(200, "Success", new AuthVO(user.getUsername(), user.getUuid(), StpUtil.getTokenInfo()));
        }
    }

    public Result register(AuthDTO authDTO) {
        LambdaQueryWrapper<UserDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserDO::getUsername, authDTO.getUsername());
        if (userMapper.selectCount(lqw) > 0) {
            return new Result(401, "用户名已存在", null);
        }
        String hashedPassword = BCrypt.hashpw(authDTO.getPassword(), BCrypt.gensalt());
        UserDO user = new UserDO();
        user.setUsername(authDTO.getUsername());
        user.setPassword(hashedPassword);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setUuid(UUID.randomUUID().toString());
        userMapper.insert(user);
        return new Result(200, "Success", null);
    }

    public Result logout(TokenDTO tokenDTO) {
        StpUtil.logoutByTokenValue(tokenDTO.getTokenValue());
        return new Result(200, "Success", null);
    }

    public Result checkLogin() {
        if(StpUtil.isLogin()) {
            return new Result(200, "Yes", null);
        } else {
            return new Result(401, "No", null);
        }
    }
}
