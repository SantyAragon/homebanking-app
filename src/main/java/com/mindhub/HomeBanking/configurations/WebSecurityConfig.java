package com.mindhub.HomeBanking.configurations;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/api/authenticated").permitAll()
                .antMatchers("/rest/**", "/h2-console/**").hasAuthority("ADMIN")
                .antMatchers("/api/clients/current/verification").permitAll()
                .antMatchers("/api/clients/current/password").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/cards/all").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/cards/disabled").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/accounts/disabled").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/transactions/payment").permitAll()
                .antMatchers(HttpMethod.POST, "/api/transactions/generate").hasAuthority("CLIENT")
                .antMatchers("/api/transactions/generate").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/loans").hasAuthority("CLIENT")
                .antMatchers("/api/admin", "/api/loans").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers("/api/cryptos/**").hasAuthority("CLIENT")
                .antMatchers("/api/**").hasAuthority("ADMIN")

                .antMatchers("/web/index.html", "/web/recoverpassword.html", "web/assets/**", "/web/styles/index.css", "/web/styles/styles.css", "/web/scripts/index.js", "/web/scripts/password.js").permitAll()
                .antMatchers("/**").hasAuthority("CLIENT");


        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login")
        ;


        http.logout().logoutUrl("/api/logout");
        // turn off checking for CSRF tokens

        http.csrf().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
}
