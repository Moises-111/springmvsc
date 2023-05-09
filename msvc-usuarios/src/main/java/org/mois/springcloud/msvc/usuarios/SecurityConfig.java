package org.mois.springcloud.msvc.usuarios;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("/authorized","/login").permitAll()
                .antMatchers(HttpMethod.GET,"/").hasAnyAuthority("SCOPE_read","SCOPE_write")
                .antMatchers(HttpMethod.POST,"/").hasAnyAuthority("SCOPE_write")
                .antMatchers(HttpMethod.PUT,"/{id}").hasAnyAuthority("SCOPE_write")
                .antMatchers(HttpMethod.DELETE,"/{id}").hasAnyAuthority("SCOPE_write")
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/msvc-usuarios-client")).csrf().disable()
                .oauth2Login(withDefaults()).csrf().disable()
                .oauth2ResourceServer().jwt();
        return httpSecurity.build();
    }
}
