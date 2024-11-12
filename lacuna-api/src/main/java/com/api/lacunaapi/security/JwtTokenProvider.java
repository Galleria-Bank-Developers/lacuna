package com.api.lacunaapi.security;

import br.com.galleriabank.jwt.common.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;

import static br.com.galleriabank.jwt.common.SecurityConstants.AUTHORITIES_KEY;

@Service
public class JwtTokenProvider {
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SecurityConstants.CHAVE_SERVICO).build()
                .parseClaimsJws(token).getBody();

        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null
                ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils
                        .commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

public Mono<Boolean> validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(SecurityConstants.CHAVE_SERVICO)
                    .build().parseClaimsJws(token);
            // parseClaimsJws will check expiration date. No need do here.
//            log.info("expiration date: {}", claims.getBody().getExpiration());
            return Mono.just(true);
        }
        catch (JwtException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
//            log.trace("Invalid JWT token trace.", e);
        }
        return Mono.just(false);
    }

}
