package com.exp.cl.config;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.exp.cl.user.repository.UserRepository;
import com.exp.cl.utils.Constants;
import com.exp.cl.utils.JWTUtils;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    public JWTAuthenticationFilter(JWTUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
        throws ServletException, IOException {

        String header = request.getHeader(Constants.AUTHORIZATION);

        if (header != null && header.startsWith(Constants.PREFIX_TOKEN)) {
            String token = header.substring(7);
            if (jwtUtils.validateToken(token) && userRepository.findByToken(token).isPresent()) {
                Authentication auth = jwtUtils.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }

}