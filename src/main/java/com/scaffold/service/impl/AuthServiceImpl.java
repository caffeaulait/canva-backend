package com.scaffold.service.impl;


import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.scaffold.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${app.id}")
    private String appId;

    public static final Integer TIMEOUT_MILLISECONDS = 3000;

    public PublicKey getPublicKey(String kid)
        throws URISyntaxException, MalformedURLException, JwkException {
        String uri = String.format("https://api.canva.com/rest/v1/apps/%s/jwks", this.appId);
        URL url = new URI(uri).toURL();
        JwkProvider jwkProvider = new UrlJwkProvider(url, TIMEOUT_MILLISECONDS, TIMEOUT_MILLISECONDS, null);
        return jwkProvider.get(kid).getPublicKey();
    }


    public boolean verifyToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String kid = jwt.getKeyId();
        try {
            PublicKey publicKey = getPublicKey(kid);
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm).withAudience(appId).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            System.err.printf("verify token failed: %s\n", e);
            return false;
        }
    }
}
