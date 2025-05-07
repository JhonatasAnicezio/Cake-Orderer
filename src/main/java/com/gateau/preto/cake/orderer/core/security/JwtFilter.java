package com.gateau.preto.cake.orderer.core.security;

import com.gateau.preto.cake.orderer.authentication.domain.JwtService;
import com.gateau.preto.cake.orderer.authentication.domain.UserService;
import com.gateau.preto.cake.orderer.core.utils.HeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final HeaderUtils headerUtils;
  private final UserService userService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = headerUtils.extractToken(request)
        .orElseThrow();

    String subjectEmail = jwtService.jwtVerify(token);

    UserDetails user = userService.loadUserByUsername(subjectEmail);

    UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
        user, null, user.getAuthorities()
    );

    SecurityContextHolder.getContext().setAuthentication(usernamePassword);

    filterChain.doFilter(request, response);
  }
}
