package project3.Shoppee.Service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
public class JwtTokenFilter extends OncePerRequestFilter{
	@Autowired
	private JwtTokenService jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
//doc token gui len tu header
		String token = resolveToken(httpServletRequest);

		try {
			//verify token
			if (token != null && jwtTokenProvider.validateToken(token)) {
				//co token roi thi lay usename ,goi db len user
				Authentication auth = jwtTokenProvider.getAuthentication(token);
//set vao context de co dang nhap roi 
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception ex) {
			// this is very important, since it guarantees the user is not authenticated at
			// all
			SecurityContextHolder.clearContext();
			httpServletResponse.sendError(401, ex.getMessage());
			return;
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
