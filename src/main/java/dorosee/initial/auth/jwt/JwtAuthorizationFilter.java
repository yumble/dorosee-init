package dorosee.initial.auth.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;

import com.fasterxml.jackson.databind.ObjectMapper;
import dorosee.initial.auth.entity.User;
import dorosee.initial.auth.principal.PrincipalDetails;
import dorosee.initial.auth.repository.UserRepository;
import dorosee.initial.config.basestatus.BaseResponse;
import dorosee.initial.config.secret.Secret;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

import static dorosee.initial.config.basestatus.BaseResponseStatus.EXCEPTION_TOKEN_EXPIRED;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader(Secret.HEADER_STRING);

        printRequest(request);

        //header 유무 확인
        if (jwtHeader == null || !(jwtHeader.startsWith(Secret.TOKEN_PREFIX))) {
            chain.doFilter(request, response);
            return;
        }
        if(request.getRequestURI().contains("/v1/api/auth") || request.getRequestURI().contains("/login")) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = request.getHeader(Secret.HEADER_STRING).replace(Secret.TOKEN_PREFIX, "");

        try {
            String username = JWT.require(Algorithm.HMAC512(Secret.AT_JWT_KEY))
                    .build().verify(jwtToken).getClaim("username").asString();

            if (username != null) {
                User userEntity = userRepository.findByUsername(username);
                //userEntity request에 담아서 보내기
                request.setAttribute("user", userEntity);

                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                //jwt Token 서명 정상 -> Authentication 객체 생성
                Authentication authentication
                        = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                log.info("authentication = " + authentication);
                //강제 시큐리티 세션 접근 -> Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }
        } catch (TokenExpiredException e) {
            BaseResponse<Object> baseResponse = new BaseResponse<>(EXCEPTION_TOKEN_EXPIRED);
            response.addHeader("Content-Type", "application/json;charset=UTF-8");
            new ObjectMapper().writeValue(response.getWriter(), baseResponse);
        }

    }

    void printRequest(HttpServletRequest request) {
        log.info("\n-----------------------------------------------------------------");
        System.out.printf("[Request] : %s\n", request.getRequestURI());
        System.out.printf("[Method]: %s\n", request.getMethod());
        System.out.printf("[Header]: \n");
        Enumeration<String> headerNames = request.getHeaderNames();
        Enumeration<String> paramNames = request.getParameterNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.printf("\t%s : %s\n", name, value);
        }
        System.out.printf("[Parameters]:\n");
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String value = request.getParameter(name);
            System.out.printf("\t%s : %s\n", name, value);
        }
        System.out.printf("[Query String]: %s\n", request.getQueryString());
        System.out.printf("[TrailerFields]: %s\n", request.getTrailerFields());
        try {
            System.out.printf("[MultiParts]: \n");
            Collection<Part> parts = request.getParts();
            for (Part part : parts) {

                if (part.getHeader("Content-Disposition").contains("filename=")) {
                    String fileName = StringUtils.getFilenameExtension(part.getHeader("Content-Disposition"));

                    if (part.getSize() > 0) {
                        System.out.printf("\tfilaname : %s\t", fileName);
                    }
                } else {
                    String formValue = request.getParameter(part.getName());
                    System.out.printf("\tname : %s, value: %s, contentType :  %s,  size : %d bytes\n", part.getName(), formValue,
                            part.getContentType(), part.getSize());
                }
            }
        } catch (IOException | ServletException e) {
            System.out.printf("[No Parts]\n");
        }

        System.out.printf("-----------------------------------------------------------------\n");
    }
}
