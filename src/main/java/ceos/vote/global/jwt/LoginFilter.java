package ceos.vote.global.jwt;

import ceos.vote.domain.member.dto.request.LoginRequestDto;
import ceos.vote.domain.member.entity.Refresh;
import ceos.vote.global.repository.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequestDto loginRequestDto;

        try {
            // JSON 요청 본문을 읽어 LoginRequestDto 객체로 변환
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginRequestDto = objectMapper.readValue(messageBody, LoginRequestDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String userId = loginRequestDto.userId();
        String password = loginRequestDto.password();

        // 스프링 시큐리티에서 userId와 password를 검증하기 위해서는 token(dto)에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password, null);

        // token에 담은 값들의 검증을 위해 AuthenticationManager로 전달 -> 검증 진행
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공 시 실행하는 메소드 (JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        String nickname = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", nickname, role, 1000L * 60 * 60 * 24 * 14);           // 2주 (임시)
        String refresh = jwtUtil.createJwt("refresh", nickname, role, 1000L * 60 * 60 * 24 * 14);   // 2주

        //Refresh 토큰 저장
        addRefreshEntity(nickname, refresh, 1000L * 60 * 60 * 24 * 14);

        //응답 설정
        response.setHeader("Authorization", "Bearer " + access);
        response.addCookie(createCookie("refreshToken", refresh));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"result\": \"로그인이 성공적으로 완료되었습니다.\"}");
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"result\": \"로그인에 실패하였습니다.\"}");
    }

    private void addRefreshEntity(String userId, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = Refresh.builder()
                .userId(userId)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 14);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
