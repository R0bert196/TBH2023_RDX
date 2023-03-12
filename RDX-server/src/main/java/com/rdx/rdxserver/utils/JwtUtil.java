package com.rdx.rdxserver.utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdx.rdxserver.entities.AppUserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final ObjectMapper mapper = new ObjectMapper();



    public static String generateToken(AppUserEntity user, String SECRET_KEY) {
        Date now = new Date();
        Map<String, Object> map = mapper.convertValue(user, new TypeReference<Map<String, Object>>() {});
        Date expiryDate = new Date(now.getTime() + 3600000); // Token expiration time is 1 hour
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .addClaims(map)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String getEmailFromToken(String token, String SECRET_KEY) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

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
