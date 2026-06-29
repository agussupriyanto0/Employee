package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.config;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(http -> http.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        )
                        .permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/employee/**","/api/employee")
                        .hasAnyAuthority("USER","ADMIN")

                        .requestMatchers(HttpMethod.POST,"/api/employee/**","/api/employee")
                        .hasAnyAuthority("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/api/employee/**","/api/employee")
                        .hasAnyAuthority("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/employee/**","/api/employee")
                        .hasAnyAuthority("ADMIN")

                        .anyRequest()
                        .authenticated())
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return httpSecurity.build();
    }
}
