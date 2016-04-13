package net.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Mariusz Gorzycki
 * @since 26.03.2016
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("t").password("t").roles("USER").and()
                .withUser("a").password("a").roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/signup", "/about").permitAll() // #4
                .antMatchers("/rest/*").hasRole("ADMIN") // #6
                .antMatchers("/test/**").access("hasRole('ROLE_ADMIN')") // #6
                .anyRequest().authenticated() // 7
                .and()
                .formLogin()
                .permitAll()
                .and().logout().permitAll().permitAll();


//        http
//                .httpBasic().and().csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
//                .authorizeRequests()
//                .anyRequest().permitAll();

//        http.addFilter(new AuthenticationFilter(authenticationManager()));
//        http.addFilterBefore(new AuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }
}
