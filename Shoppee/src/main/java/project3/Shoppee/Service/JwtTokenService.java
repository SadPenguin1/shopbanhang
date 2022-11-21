package project3.Shoppee.Service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class JwtTokenService {
	
	
	@Value("${jwt.secret}")
	private String secretKey ;

	private long validityInMilliseconds = 3600000; // 1h

	@Autowired
	UserDetailsService userDetailsService;
// xac thuc tra lai token 
	public String createToken(String username) {
		Claims claims = Jwts.claims().setSubject(username);

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);
		String accessToken = Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.HS256, secretKey)//
				.compact();

		return accessToken;
	}
//
	public boolean validateToken(String token) {
		Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		return true;
	}
//khi gui lai lan 2 de lay du lieu sau khi co token 
//Gia mao dang nhap 
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
	}
// 
	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

}
