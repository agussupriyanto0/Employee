package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.filter;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity.User;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.repository.UserRepository;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository repository;



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        Enumeration<String> headers = request.getHeaderNames();

        while(headers.hasMoreElements()){
            String header = headers.nextElement();
            System.out.println(header + " : " + request.getHeader(header));
        }
        final  String authHeader = request.getHeader("Authorization");
        System.out.println("HEADER : " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")){

            filterChain.doFilter(request,response);
            return;
        }

        String token  = authHeader.substring(7);
        System.out.println("TOKEN : " + token);

        try {
            String username = jwtService.extraUsername(token);
            System.out.println("USERNAME TOKEN : " + username);

            if (username != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {
                User user = repository.findByUsername(username).orElse(null);
                if (user != null &&
                        jwtService.isTokenValid(token)) {

                    System.out.println("USERNAME : " + username);
                    System.out.println("ROLE : " + user.getRole().name());
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(new SimpleGrantedAuthority(
                                            user.getRole().name()
                                    ))
                            );
                    SecurityContextHolder.getContext()
                            .setAuthentication(authenticationToken);
                }
            }
        }catch (Exception exception){

            filterChain.doFilter(request,response);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
