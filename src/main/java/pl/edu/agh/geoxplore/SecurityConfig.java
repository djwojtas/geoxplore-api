package pl.edu.agh.geoxplore;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .exceptionHandling()
                .and()
                .anonymous()
                .and()
                .servletApi()
                .and()
                .headers().cacheControl();

        http
                .authorizeRequests().antMatchers("/add").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new LoginFilter(new AntPathRequestMatcher("/add")), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new AuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}