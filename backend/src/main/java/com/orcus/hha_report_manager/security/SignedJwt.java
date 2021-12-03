package com.orcus.hha_report_manager.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;


/**
 * Utility class for generating and verifying JWT's.
 * */
public class SignedJwt {

    /*
     *  Todo: We need to store the key for subsequent decryption.
     *  This will need to be secure as exposing the key => our token can be generated.
     *  The Keys.secretKeyFor(SignatureAlgorithm.HS256) is provided to generate a valid key;
     */
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final JwtParser PARSER = Jwts.parserBuilder()
            .setSigningKey(KEY)
            .build();

    /**
     * Validate the Jwt token against the key.
     * Throw exceptions if not valid.
     * */
    public static Jws<Claims> validate(String token) throws Exception{
        return PARSER.parseClaimsJws(token);
    }

    public static String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        return null;
    }

    public static String make(UserDetails userDetails) {
        //todo: add data to be contained in token.
        var claims = new HashMap<String, Object>();
        return make(userDetails.getUsername(), claims);
    }

    private static String make(String subject, HashMap<String, Object> claims) {
        //Todo: some form of token timout control. Need a persistent key.
        var createdDate = new Date(System.currentTimeMillis());
        var expiryDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expiryDate)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static class JWSBody{

    }
}
