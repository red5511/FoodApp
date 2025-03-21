package com.foodapp.foodapp.security;

import com.foodapp.foodapp.auth.jwtToken.JwtTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class JwtService {
    private static final String SECRETE_KEY = "3634383642413744313139453535453845313843423236333141433534";
    private final JwtTokenRepository jwtTokenRepository;

    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(final String token, Function<Claims, T> claimsResolver) {
        var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(final Map<String, Object> extraClaims, final UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getTokenExpirationDateAt3am())
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Date getTokenExpirationDateAt3am(){
        Calendar calendar = Calendar.getInstance();

        // Set the calendar to the next day
        calendar.add(Calendar.DATE, 1);

        // Set the time to 3:00 AM
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Get the expiration date (3:00 AM next day)
        return calendar.getTime();
    }

    public boolean isTokenValid(final String token, final UserDetails userDetails) {
        String username = extractUsername(token);
        var jwtToken = jwtTokenRepository.findByToken(token).orElseThrow(() -> new UsernameNotFoundException("Invalid JTW token"));
        if (!username.equals(userDetails.getUsername()) ||
                isTokenExpired(token) ||
                jwtToken.isRevoked() ||
                jwtToken.isExpired()) {
            throw new UsernameNotFoundException("Invalid JTW token");
        }
        return true;
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRETE_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
