package DAJ2EE.service.Auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import DAJ2EE.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String sercet;
    @Value("${jwt.expiration")
    private String expiration;
    
    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(sercet.getBytes());
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
