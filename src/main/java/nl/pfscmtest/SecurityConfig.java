package nl.pfscmtest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().authenticated()  // Require authentication for all requests
                )
                .httpBasic()  // Enable Basic Authentication
                .and()
                .csrf().disable();  // Disable CSRF for simplicity (enable in production)

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Define an in-memory user with a username and password for testing
        return new InMemoryUserDetailsManager(
                User.withDefaultPasswordEncoder()
                        .username("emp_req_tst")
                        .password("HwLmGvX3ogReMV7B")
                        .build()
        );
    }
}
