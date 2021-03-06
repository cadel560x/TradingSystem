package ie.gmit.sw.fyp.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;




@Configuration
@EnableWebSecurity
public class ShutdownEndpointConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		  .csrf()
	      .disable()
	      .httpBasic()
	      .and()
		  .requestMatcher(EndpointRequest.to("shutdown")).authorizeRequests()
	      .anyRequest().hasRole("SUPERUSER");
		
	} // end configure(HttpSecurity http)

} // end class ShutdownEndpointConfig
