package co.com.bancolombia.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clave secreta (mejor guardarla en variables de entorno)
    private final String SECRET_KEY = "MiSecretaClaveSuperLargaParaJWT1234567890";

    // Duración del token: 1 hora (en ms)
    private final long JWT_EXPIRATION = 60 * 60 * 1000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Genera token con correo y rol como claims
    public String generateToken(String correo, String nombreRol,Long documento) {
        return Jwts.builder()
                .claim("correo", correo)
                .claim("nombreRol", nombreRol)
                .claim("documento", documento)
                .setSubject(correo)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraer claim general
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrae todos los claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extraer correo del token
    public String extractCorreo(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraer rol del token
    public String extractRol(String token) {
        return extractClaim(token, claims -> claims.get("nombreRol", String.class));
    }
    public Long extractDocumento(String token) {
        return extractClaim(token, claims -> claims.get("documento", Long.class));
    }

    // Validar token
    public boolean validateToken(String token, String correo) {
        final String username = extractCorreo(token);
        return (username.equals(correo) && !isTokenExpired(token));
    }

    // Verificar expiración
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}