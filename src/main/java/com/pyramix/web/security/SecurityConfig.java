package com.pyramix.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserSecurityControl userSecurityControl;

	private static final String ZUL_FILES = "/zkau/web/**/*.zul";
    private static final String[] ZK_RESOURCES = {
            "/zkau/web/**/js/**",
            "/zkau/web/**/zul/css/**",
            "/zkau/web/**/font/**",
            "/zkau/web/**/img/**"
    };
    private static final String REMOVE_DESKTOP_REGEX = "/zkau\\?dtid=.*&cmd_0=rmDesktop&.*";
	
    private final Logger log = Logger.getLogger(SecurityConfig.class);

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("Configure with authenticationProvider using AuthenticationManagerBuilder...");
		
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("Configure with HttpSecurity...");
		
		// ZK already sends a AJAX request with a built-in CSRF token
        // please refer to https://www.zkoss.org/wiki/ZK%20Developer's%20Reference/Security%20Tips/Cross-site%20Request%20Forgery
		http.csrf().disable();
		// redirect to https in Spring Security
		http.requiresChannel()
			.requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
			.requiresSecure();
		http.authorizeRequests()
				.antMatchers(ZUL_FILES).denyAll() // block direct access to zul files
				.antMatchers(HttpMethod.GET, ZK_RESOURCES).permitAll() // allow zk resources
				.regexMatchers(HttpMethod.GET, REMOVE_DESKTOP_REGEX).permitAll() // allow desktop cleanup
				.requestMatchers(req -> "rmDesktop".equals(req.getParameter("cmd_0"))).permitAll() // allow desktop cleanup from ZATS
				.mvcMatchers("/", "/login", "/logout").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login").permitAll()
				.defaultSuccessUrl("/landing", true) // send to target URL; allows controller to process the default-target-url
				.failureUrl("/login?error=true")
			.and()
                .rememberMe()
                .key("aBcdeFghijklmNOPQRstuv12345678")
                // https://stackoverflow.com/questions/46421185/remember-me-not-working-throws-java-lang-illegalstateexception-userdetailsse
                .userDetailsService(getUserSecurityControl())
            .and()
                // ref: https://www.codejava.net/frameworks/spring-boot/spring-security-logout-success-handler-example
                .logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler() {
                    
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                            throws IOException, ServletException {
                        
                    	// 29/01/2022 - this causes 'Unable to activate destroyed desktop' exception
                        // UserSecurityDetails securityDetails = (UserSecurityDetails) authentication.getPrincipal();
                        // log.info("User '"+securityDetails.getUsername()+"' logged out successfully");
                        
                        response.sendRedirect(request.getContextPath());
                    }

                });

		// ref: https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/headers.html
		// to allow loading of jasper file - for report printing
		http.headers()
			.frameOptions().sameOrigin()
			.httpStrictTransportSecurity().disable();

	}

	@Bean
	protected AuthenticationProvider authenticationProvider() {
		log.info("Configure the authenticationProvider with UserSecurityControl and BCryptPasswordEncoder");
		
		DaoAuthenticationProvider provider
			= new DaoAuthenticationProvider();
		provider.setUserDetailsService(getUserSecurityControl());
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		
		return provider;
	}    
    
	public UserSecurityControl getUserSecurityControl() {
		return userSecurityControl;
	}

	public void setUserSecurityControl(UserSecurityControl userSecurityControl) {
		this.userSecurityControl = userSecurityControl;
	}
	
}
