package com.orcus.hha_report_manager.security.filters;

import com.orcus.hha_report_manager.security.beans.HTTPRequestUser;
import com.orcus.hha_report_manager.security.SignedJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter intercepts all incoming requests validate and authorize jwt tokens.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private HTTPRequestUser httpRequestUser;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwtToken = "";
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        }

        try {
            var jwt = SignedJwt.validate(jwtToken);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwt.getBody().getSubject();
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
                httpRequestUser.setUsername(username);
            }
        } catch (Exception e) {
            //Todo: handle exceptions?
            e.printStackTrace();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
