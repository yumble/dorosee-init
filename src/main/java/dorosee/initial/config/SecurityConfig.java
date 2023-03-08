package dorosee.initial.config;

import dorosee.initial.auth.jwt.JwtAuthenticationFilter;
import dorosee.initial.auth.jwt.JwtAuthorizationFilter;
import dorosee.initial.auth.repository.TokenRepository;
import dorosee.initial.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    //WebSecurityConfigurerAdapter 추후에 제거 필요,,
    //@override -> configure
    //authenticationManager 내장

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;


    AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 x
                .and()
                .formLogin().disable() //form login 사용 x
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), tokenRepository))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(authenticationConfiguration), userRepository))
                .authorizeHttpRequests( (authz) -> authz
                        .requestMatchers("/v1/api/hello/**").hasAnyRole("USER", "TEAMLEADER", "MANAGER") //DB 저장은 ROLE_USER 로 해야함
//                        .requestMatchers("/v1/api/hello/**").hasRole("USER")
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}