package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;


@Configuration
@EnableAuthorizationServer //OAuth2 권한 서버
@SpringBootApplication
@RequiredArgsConstructor
public class Oauth2ServerApplication extends AuthorizationServerConfigurerAdapter {

//    @Autowired
//    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Value("${resource.id:spring-boot-application}")
    private String resourceId;

    @Value("${access_token.validity_period:3600}")
    private int accessTokenValiditySeconds = 3600;


    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class, args);
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints.accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("bar")
                .authorizedGrantTypes("password")
                .authorities("ROLE_USER")
                .scopes("read", "write")
                .resourceIds(resourceId)
                .accessTokenValiditySeconds(accessTokenValiditySeconds)
                .secret("foo");
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        return new JwtAccessTokenConverter();
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    //jdbc:h2:mem:tesgdb 에 token을 저장
//	@Bean
//	public TokenStore JdbcTokenStore(DataSource dataSource) {
//		return new JdbcTokenStore(dataSource);
//	}


}

//
//@Configuration
//@EnableWebSecurity
//class SecurityConfigurer extends WebSecurityConfigurerAdapter {
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManagerBean();
//    }
//}