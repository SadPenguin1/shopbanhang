package project3.Shoppee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import project3.Shoppee.Service.SecurityInterceptor;

@SpringBootApplication
@EnableJpaAuditing
public class ShoppeeApplication implements WebMvcConfigurer{
	
	@Autowired
	SecurityInterceptor securityInterceptor;

	public static void main(String[] args) {
		SpringApplication.run(ShoppeeApplication.class, args);
	}
	@Bean
	JsonMessageConverter converter() {
		return new JsonMessageConverter();
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(securityInterceptor);
	}

}
