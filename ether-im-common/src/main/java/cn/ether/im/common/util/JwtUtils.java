/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ether.im.common.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;

public class JwtUtils {

    private static final String CLAIM_NAME = "CN";

    /**
     * 生成jwt字符串  JWT(json web token)
     * @param userId  用户id
     * @param claimVal 自定义数据
     * @param expireIn 过期时间 秒
     * @param secret  秘钥
     * @return token
     * */
    public static String sign(Long userId, String claimVal,long expireIn,String secret) {
        try {
            Date date = new Date(System.currentTimeMillis() + expireIn*1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    //将userId保存到token里面
                    .withAudience(userId.toString())
                    //存放自定义数据
                    .withClaim(CLAIM_NAME, claimVal)
                    //过期时间
                    .withExpiresAt(date)
                    //token的密钥
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据token获取userId
     * @param token  登录token
     * @return 用户id
     * */
    public static Long getUserId(String token) {
        try {
            String userId = JWT.decode(token).getAudience().get(0);
            return Long.parseLong(userId);
        }catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 根据token获取用户数据
     * @param token 用户登录token
     * @return 用户数据
     * */
    public static String getInfo(String token) {
        try {
            return JWT.decode(token).getClaim(CLAIM_NAME).asString();
        }catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 校验token
     * @param token 用户登录token
     * @param secret 秘钥
     * @return true/false
     * */
    public static Boolean checkSign(String token,String secret) {
        try{
            Algorithm algorithm  = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        }catch (JWTVerificationException e) {
            return false;
        }
    }
}
