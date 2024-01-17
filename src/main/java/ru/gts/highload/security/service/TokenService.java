package ru.gts.highload.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    /**
     * Секретный ключ.
     */
    private final String jwtKey;

    /**
     * Срок действия токена в минутах.
     */
    private final Integer jwtExp;

    private final JWTVerifier jwtVerifier;


    public TokenService(@Value("${jwt.key}") String jwtKey, @Value("${jwt.exp}") Integer jwtExp) {
        this.jwtKey = jwtKey;
        this.jwtExp = jwtExp;
        jwtVerifier = JWT.require(Algorithm.HMAC512(jwtKey)).build();

    }

    /**
     * Декодирует токен и возвращает идентификатор пользователя из него.
     * <p>
     * При декодировании происходит валидация.
     *
     * @param token токен
     * @return идентификатор пользователя
     */
    public String getUserId(String token) {
        return jwtVerifier.verify(token).getSubject();
    }

    /**
     * Генерирует новый токен.
     *
     * @param userId идентификатор пользователя
     * @return токен
     */
    public String generateToken(String userId) {
        LocalDateTime issued = LocalDateTime.now();
        return JWT.create().withSubject(userId)
                .withIssuedAt(issued.atZone(ZoneId.systemDefault()).toInstant())
                .withExpiresAt(issued.plus(jwtExp, ChronoUnit.MINUTES)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                ).sign(Algorithm.HMAC512(jwtKey));
    }


}
