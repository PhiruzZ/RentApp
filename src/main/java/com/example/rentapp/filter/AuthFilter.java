package com.example.rentapp.filter;

import com.example.rentapp.exception.ExceptionBody;
import com.example.rentapp.service.ClientAuthenticationService;
import com.example.rentapp.service.CustomUserDetailsService;
import com.example.rentapp.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    private final CustomUserDetailsService customUserDetailsService;
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final static List<String> allowedPatterns = List.of("/v2/api-docs/**","/v3/api-docs/**","/swagger-ui.html/**","/swagger-ui/index.html/**","/webjars/**","/actuator/health","/auth/**");
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getServletPath());
        boolean match = false;
        for(String pattern : allowedPatterns) {
            match = match|| pathMatcher.match(pattern, request.getServletPath());
        }
        if(!match) {
            log.info("authenticating");
            String jwt = parseJwt(request);
            if(jwt==null || !jwtUtils.validateJwt(jwt)) {
                ExceptionBody exceptionBody = new ExceptionBody();
                exceptionBody.setMessage("Authentication failed");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                String responseBody = null;
                try {
                    responseBody = convertObjectToJson(exceptionBody);
                }catch (JsonProcessingException e) {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.getWriter().write("{\"message\":\"Internal Server Error\"}");
                    return;
                }
                response.getWriter().write(responseBody);
                return;
            }
            String userId = jwtUtils.getClientId(jwt);
            UserDetails user = customUserDetailsService.loadUserByUsername(userId);
            ClientAuthenticationService clientAuthentication =
                    new ClientAuthenticationService(user,null,user.getAuthorities(),jwt);
            clientAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(clientAuthentication);
        }
        filterChain.doFilter(request,response);
    }
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }


}
