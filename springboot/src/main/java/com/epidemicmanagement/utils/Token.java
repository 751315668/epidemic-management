package com.epidemicmanagement.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epidemicmanagement.dao.UserDao;
import com.epidemicmanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;

/**
 * @author XUE
 */
public class Token {

    private static final String TOKEN_SECRET="user_token";

    // token生成
    public static String createToken(Integer userId) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        long current = System.currentTimeMillis();
        long expires = 300 * 1000 * 60; // token生存时间：30m
        Date date = new Date(current + expires);
        HashMap<String, Object> header = new HashMap<>();
        header.put("typ", "JWT"); // token令牌类型
        header.put("alg", "HS256"); // 签名算法
        return JWT.create()
                // Header头部：Token类型和加密算法
                .withHeader(header)
                .withClaim("id", userId) // Payload载荷：用户id
                .withExpiresAt(date)
                .sign(algorithm);
    }

    // token校验
    public static boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
            DecodedJWT verify = verifier.verify(token);
//            String payload = verify.getPayload();
//            System.out.println("payloda==" + payload);
//            System.out.println("id===" + verify.getClaim("id"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Integer getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asInt();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

}
