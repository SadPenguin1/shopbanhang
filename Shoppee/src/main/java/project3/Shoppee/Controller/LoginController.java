package project3.Shoppee.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class LoginController {
	
	@GetMapping("/login")
	public String login() {
		return "dang-nhap";
	}

}
