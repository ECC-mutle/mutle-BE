package com.mutle.mutle.jwt;

import com.mutle.mutle.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklist tokenBlacklist;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenBlacklist tokenBlacklist) {
        this.jwtUtil=jwtUtil;
        this.tokenBlacklist=tokenBlacklist;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String header=request.getHeader("Authorization");

        if(header==null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token=header.substring(7); //Bearer 제거

            if(tokenBlacklist.isBlackListed(token)){
                sendErrorResponse(response, ErrorCode.TOKEN_ERROR);
                return;
            }

            Claims claims= jwtUtil.parseToken(token); //토큰 파싱

            Long id=Long.parseLong(claims.getSubject());

            UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(id, null, null);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request,response);

        }catch (Exception e){
            SecurityContextHolder.clearContext(); //인증 제거
            sendErrorResponse(response, ErrorCode.TOKEN_ERROR);
        }


    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth") ||
                path.startsWith("/api/music") ||
                path.startsWith("/api/island") ||
                path.startsWith("/api/image") ||
                path.startsWith("/api/friends") ||
                path.startsWith("/api/bottles") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs");
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus());
        response.setContentType("application/json;charset=UTF-8");
        String json = String.format(
                "{\"status\":%d,\"code\":\"%s\",\"message\":\"%s\"}",
                errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage()
        );
        response.getWriter().write(json);
    }
}