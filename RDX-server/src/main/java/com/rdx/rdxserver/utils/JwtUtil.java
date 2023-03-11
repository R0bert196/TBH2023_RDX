package com.rdx.rdxserver.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JwtUtil {





    public static String generateToken(String username, String SECRET_KEY) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000); // Token expiration time is 1 hour
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

//    public static String getUsernameFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.getSubject();
//    }

//    public static boolean validateToken(String token, String username) {
//        String tokenUsername = getUsernameFromToken(token);
//        return tokenUsername.equals(username) && !isTokenExpired(token);
//    }
//
//    private static boolean isTokenExpired(String token) {
//        Date expirationDate = Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody()
//                .getExpiration();
//        return expirationDate.before(new Date());
//    }

}
