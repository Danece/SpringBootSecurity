package com.springboot_practice.demo.token;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.AuthException;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtils implements Serializable {
    private static final long EXPIRATION_TIME = 1 * 60 * 1000;
    /*
     * JWT Secret Key
     */
    private static final String SECURET = "jtron company system";

    /*
     * 簽發 JWT
     */
    public String generateToken (HashMap<String, String> userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", userDetails.get("userName"));

        return Jwts.builder()
        .setClaims(claims)
        .setExpiration(new Date(Instant.now().toEpochMilli() + EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS512, SECURET)
        .compact();
    }

    /*
     * 驗證 JWT
     */
    public void validateToken(String token) throws AuthException {
        try {
            Jwts.parser()
            .setSigningKey(SECURET)
            .parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new AuthException("Invaild JET signature.");
        } catch (MalformedJwtException e) {
            throw new AuthException("Invaild JWT token.");
        } catch (ExpiredJwtException e) {
            throw new AuthException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new AuthException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new AuthException("JWT token compact of handler are invalid");
        }
    }
}
