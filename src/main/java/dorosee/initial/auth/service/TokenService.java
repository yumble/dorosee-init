package dorosee.initial.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dorosee.initial.auth.entity.Token;
import dorosee.initial.auth.entity.User;
import dorosee.initial.auth.repository.TokenRepository;
import dorosee.initial.config.basestatus.BaseException;
import dorosee.initial.config.secret.Secret;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static dorosee.initial.config.basestatus.BaseResponseStatus.EXPIRED_TOKEN;
import static dorosee.initial.config.basestatus.BaseResponseStatus.NOT_FOUND_BY_USER_ID;
import static dorosee.initial.config.secret.Secret.*;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Transactional
@Slf4j
@Service
@AllArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Map<String,String> issueAccessTokenByRefreshToken(String originRefreshToken) throws BaseException {

        String userId = null;
        String username = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(RT_JWT_KEY)).build();
            DecodedJWT decodedJWT = verifier.verify(originRefreshToken);
            userId = decodedJWT.getClaim("id").asString();
            username = decodedJWT.getClaim("username").asString();
        }catch(TokenExpiredException e){
            throw new BaseException(EXPIRED_TOKEN);
        }
        Optional<Token> tokenEntity = tokenRepository.findById(userId);
        if(tokenEntity.isEmpty()){
            throw new BaseException(NOT_FOUND_BY_USER_ID);
        }
        if(!tokenEntity.get().getRefreshToken().equals(originRefreshToken)) {
            throw new BaseException(EXPIRED_TOKEN);
        }

        String accessToken = JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + Secret.AT_EXPRIRATION_TIME)) //JwtProperties.EXPIRATION_TIME)) //30분 이후
                .withClaim("id", userId)
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(Secret.AT_JWT_KEY));

        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + Secret.RT_EXPRIRATION_TIME))
                .withClaim("id", userId)
                .sign(Algorithm.HMAC512(Secret.RT_JWT_KEY));

        //dirty checking update
        tokenEntity.get().setRefreshToken(refreshToken);

        Map<String, String> tokenMap = new LinkedHashMap<>();
        tokenMap.put(AT_HEADER_STRING, accessToken);
        tokenMap.put(RT_HEADER_STRING, refreshToken);

        return tokenMap;
    }
    public Map<String,String> issueAccessTokenByModify (User user) throws BaseException {

        String userId = user.getUserId();
        String username = user.getUsername();

        Optional<Token> tokenEntity = tokenRepository.findById(userId);
        if(tokenEntity.isEmpty()){
            throw new BaseException(NOT_FOUND_BY_USER_ID);
        }

        String accessToken = JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + Secret.AT_EXPRIRATION_TIME)) //JwtProperties.EXPIRATION_TIME)) //30분 이후
                .withClaim("id", userId)
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(Secret.AT_JWT_KEY));

        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + Secret.RT_EXPRIRATION_TIME))
                .withClaim("id", userId)
                .sign(Algorithm.HMAC512(Secret.RT_JWT_KEY));

        //update
        tokenEntity.get().setUsername(username);
        tokenEntity.get().setRefreshToken(refreshToken);

        Map<String, String> tokenMap = new LinkedHashMap<>();
        tokenMap.put(AT_HEADER_STRING, accessToken);
        tokenMap.put(RT_HEADER_STRING, refreshToken);

        return tokenMap;
    }
}
