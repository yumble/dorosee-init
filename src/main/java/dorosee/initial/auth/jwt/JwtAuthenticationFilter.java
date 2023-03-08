package dorosee.initial.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonObject;
import dorosee.initial.auth.entity.Token;
import dorosee.initial.auth.entity.User;
import dorosee.initial.auth.principal.PrincipalDetails;
import dorosee.initial.auth.repository.TokenRepository;
import dorosee.initial.config.secret.Secret;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    // /login 요청 시 로그인 시도를 위해 실행되는 func.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper om = new ObjectMapper();
        User user = null;
        try {
            user = om.readValue(request.getInputStream(), User.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationServiceException("Cannot read login properties from inputStream", e);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails.getUser().getUsername() = " + principalDetails.getUser().getUsername());

        return authentication; // session에 Authentication 저장s
    }

    //attemptAuthentication 실행 후 인증이 정상 작동시 실행되는 함수
    // jwt 토큰을 여기서 만들어 사용자에게 토큰 response 해주면된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        //Hash 암호 방식
        String accessToken = JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + Secret.AT_EXPRIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getUserId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .withClaim("roles", principalDetails.getUser().getRoles())
                .sign(Algorithm.HMAC512(Secret.AT_JWT_KEY));

        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + Secret.RT_EXPRIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getUserId())
                .sign(Algorithm.HMAC512(Secret.RT_JWT_KEY));

        //todo refresh token DB저장 -> update대신 delete-insert
        tokenRepository.save(new Token(principalDetails.getUser().getUserId(),
                principalDetails.getUser().getUsername(), refreshToken));

        response.addHeader(Secret.AT_HEADER_STRING, accessToken);
        response.addHeader(Secret.RT_HEADER_STRING, refreshToken);
        response.addHeader("Content-Type", "application/json;charset=UTF-8");

        //token response body 작성
        PrintWriter writer = response.getWriter();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(Secret.AT_HEADER_STRING, accessToken);
        jsonObj.addProperty(Secret.RT_HEADER_STRING, refreshToken);

        writer.println(jsonObj);
    }
}
