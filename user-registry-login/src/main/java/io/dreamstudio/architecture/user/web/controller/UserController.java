package io.dreamstudio.architecture.user.web.controller;

import io.dreamstudio.architecture.user.enums.LoginTypeEnum;
import io.dreamstudio.architecture.user.service.UserService;
import io.dreamstudio.architecture.user.web.vo.AuthCodeRequestVO;
import io.dreamstudio.architecture.user.web.vo.LoginRequestVO;
import io.dreamstudio.common.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Ricky Fung
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "userService")
    private UserService userService;

    @PostMapping("/login")
    public ApiResult login(@RequestBody LoginRequestVO req) {
        //参数校验
        if (StringUtils.isEmpty(req.getMobile())) {
            return ApiResult.invalidParam("手机号为空");
        }

        if (req.getLoginType()==null) {
            return ApiResult.invalidParam("参数为空");
        }
        if (!(req.getLoginType().intValue() == LoginTypeEnum.PASSWORD.getValue() ||
                req.getLoginType().intValue() == LoginTypeEnum.SMS_CODE.getValue())) {
            return ApiResult.invalidParam();
        }

        if (req.getLoginType().intValue() == LoginTypeEnum.PASSWORD.getValue() &&
                StringUtils.isEmpty(req.getPassword())) {
            return ApiResult.invalidParam();
        }
        if (req.getLoginType().intValue() == LoginTypeEnum.SMS_CODE.getValue() &&
                StringUtils.isEmpty(req.getAuthCode())) {
            return ApiResult.invalidParam();
        }
        try {
            return userService.login(req);
        } catch (Exception e) {
            logger.error("用户服务-用户登录异常, mobile:{}", req.getMobile(), e);
        }
        return ApiResult.systemError();
    }

    @PostMapping("/auth-code")
    public ApiResult getAuthCode(@RequestBody AuthCodeRequestVO req) {
        //参数校验
        if (StringUtils.isEmpty(req.getMobile())) {
            return ApiResult.invalidParam("手机号为空");
        }

        try {
            return userService.getAuthCode(req);
        } catch (Exception e) {
            logger.error("用户服务-用户获取登录验证码异常, mobile:{}", req.getMobile(), e);
        }
        return ApiResult.systemError();
    }
}