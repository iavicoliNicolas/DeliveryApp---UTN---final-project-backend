package com.deliveryapp.backend.common.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    //clave secreta de al menos 64 caracteres de largo
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    //private static final String SECRET_KEY = "ID32WQ74uayoGI3nW7o9MEZKoaNzDongyYltCai2aY8kdWIqlrn4dmnbA3EFngsi";

    //tiempo de expiracion de nuestro token en milisegundos .. voy hacer que expire a las 24 hs
    @Value("${jwt.expiration}")
    private static long TOKEN_EXPIRATION;
    //private static final long TOKEN_EXPIRATION = 1000;

    //un token va a poder ser renovado hasta 7 dias despues de haya caducado
    @Value("${jwt.refresh-window}")
    private static long REFRESH_TOKEN_WINDOW;

    public String generateToken(UserDetails userDetails) {
        //puedo añadirle otra informacion que quiera al este mapa
        Map<String, Object> claims = Map.of("authorities", userDetails.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .toList()
        );
        return generateToken(claims, userDetails.getUsername());

    }

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)  //mapa clave valor
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaims(String token) {

        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token or mal formed", e);
        }
    }

    public String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public Date getExpirationDate(String token) {
        return getAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public boolean canBeTokenRenewed(String token) {
        return getExpirationDate(token).before(new Date(System.currentTimeMillis() + REFRESH_TOKEN_WINDOW));
        //   return new Date(System.currentTimeMillis()).before(getExpirationDate(token) + REFRESH_TOKEN_WINDOW)
    }

    public String renewToken(String token, UserDetails userDetails) {

        if (!canBeTokenRenewed(token)) {
            throw new RuntimeException("Token cannot be renewed!");
        }

        return generateToken(userDetails);
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        String username = getUsername(token);
        return username.equals(userDetails.getUsername());
    }


//    public String getAuthorities(String token) {
//        return getAllClaims(token).g
//    }


}
