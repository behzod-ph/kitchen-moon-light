package uz.isdaha.security;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import uz.isdaha.constan.MessageConstants;
import uz.isdaha.constan.RestApiConstants;
import uz.isdaha.entity.User;
import uz.isdaha.exception.JwtExpiredException;
import uz.isdaha.exception.PaymeException;
import uz.isdaha.service.user.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret.key}")
    String secretKey;

    @Value("${jwt.expiry.date}")
    long expiryDate;
    private final AuthService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;


    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getPhoneNumber());
        claims.put("roles", user.getAuthorities());
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiryDate);
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
            .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }


    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtExpiredException();
        }
    }


    public boolean checkPaycomUserAuth(String basicAuth, JSONRPC2Response response) {

        basicAuth = basicAuth.substring("Basic".length()).trim();

        byte[] decode = Base64.getDecoder().decode(basicAuth);

        basicAuth = new String(decode, Charset.defaultCharset());

        String[] split = basicAuth.split(":", 2);

        User client = (User) userDetailsService.loadUserByUsername(split[0]);


        if (passwordEncoder.matches(split[1], client.getPassword())) {

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(client, null, new ArrayList<>());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        }
        response.setError(new JSONRPC2Error(-32504,
            "Error authentication",
            "auth"));
        throw new PaymeException(response);

    }


}
