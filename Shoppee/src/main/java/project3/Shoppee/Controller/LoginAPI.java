package project3.Shoppee.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project3.Shoppee.Entity.Category;
import project3.Shoppee.Service.JwtTokenService;

@RequestMapping("/api")
@RestController
public class LoginAPI {
	
	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenService jwtTokenProvider;
	
	@GetMapping("/hi")
	@PreAuthorize("isAuthenticated()")
	public Category hello() {
		Category category = new Category();
		category.setId(4);
		category.setName("A");
		
		kafkaTemplate.send("notificaton",category);
		
		 return category;
	}
	
	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public Principal me(Principal p) {
		return p; 
	}

	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		return jwtTokenProvider.createToken(username);
	}

}
