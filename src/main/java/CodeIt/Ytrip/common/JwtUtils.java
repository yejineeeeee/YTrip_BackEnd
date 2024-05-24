package CodeIt.Ytrip.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {

    @Value("${JWT.SECRET.KEY}")
    private String key;

    public String generateToken(Long userId, int expireTime, String tokenName) {
        return Jwts.builder()
                .claims(createClaims(userId))
                .expiration(createExpireDate(expireTime))
                .signWith(createSigningKey())
                .compact();
    }

    private Map<String, Object> createClaims(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return claims;
    }

    private static Date createExpireDate(long expireTime) {
        long curTime = System.currentTimeMillis();
        return new Date(curTime + expireTime);
    }

    private SecretKey createSigningKey() {
        byte[] secret = key.getBytes();
        return Keys.hmacShaKeyFor(secret);
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(createSigningKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    public boolean isValidToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("token Expired");
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is Null");
            return false;
        }
    }

    public String splitBearerToken(String bearerToken) {
        return bearerToken.split(" ")[1];
    }
}
