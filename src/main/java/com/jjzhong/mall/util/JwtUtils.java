package com.jjzhong.mall.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jjzhong.mall.common.Constant;
import com.jjzhong.mall.model.pojo.User;

import java.util.Date;

/**
 * 用于生成JWT和解析JWT中用户信息的工具类
 */
public class JwtUtils {
    /**
     * 生成 JWT
     * @param user 用户
     * @return JWT 字符串
     */
    public static String generateJwtToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        return JWT.create()
                .withClaim(Constant.USER_ID, user.getId())
                .withClaim(Constant.USER_NAME, user.getUsername())
                .withClaim(Constant.USER_ROLE, user.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                .sign(algorithm);
    }

    /**
     * 从 JWT 中解析用户信息
     * @param token JWT
     * @param user 要保存的用户
     * @return 用户
     */
    public static User getUserFromToken(String token, User user) {
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        user.setUsername(jwt.getClaim(Constant.USER_NAME).asString());
        user.setId(jwt.getClaim(Constant.USER_ID).asInt());
        user.setRole(jwt.getClaim(Constant.USER_ROLE).asInt());
        return user;
    }
}
