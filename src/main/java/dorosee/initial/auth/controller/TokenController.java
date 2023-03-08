package dorosee.initial.auth.controller;

import dorosee.initial.auth.service.TokenService;
import dorosee.initial.config.basestatus.BaseException;
import dorosee.initial.config.basestatus.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static dorosee.initial.config.basestatus.BaseResponseStatus.EXCEPTION_WRONG_TOKEN;
import static dorosee.initial.config.secret.Secret.RT_HEADER_STRING;
import static dorosee.initial.config.secret.Secret.TOKEN_PREFIX;

@Slf4j
@RestController
@RequestMapping("/v1/api/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    /*
     username에 해당하는 유저 정보 가져오기
      */
    @GetMapping("/refresh")
    public BaseResponse<Map<String,String>> getRefreshToken(HttpServletRequest req, HttpServletResponse res) throws BaseException {

        String authorizationHeader = req.getHeader(RT_HEADER_STRING);
        try {
            if(authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
                throw new BaseException(EXCEPTION_WRONG_TOKEN);
            }
            String refreshToken = authorizationHeader.replace(TOKEN_PREFIX, "");

            Map<String, String> tokenMap = tokenService.issueAccessTokenByRefreshToken(refreshToken);

            return new BaseResponse<>(tokenMap, null);
        } catch (BaseException exception) {
            log.error("exception.getStatus() = " + exception.getStatus());
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
