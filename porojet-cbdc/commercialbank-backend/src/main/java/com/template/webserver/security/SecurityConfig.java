package com.template.webserver.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserDetailsService userDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        //Desactivation de la session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //donner la permission à tous les utilisateurs de s'authentifier ou de s'enregistrer
        http.authorizeRequests().antMatchers("/login/**").permitAll();
        //donner les permissions
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/commercialbank/create").hasAuthority("centralbank");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/commercialbank/saveotheraccount").hasAuthority("commercialbank");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"commercialbank/update").hasAuthority("centralbank");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/commercialbank/deleteOrActiveOrSwithAccountType").hasAuthority("centralbank");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CommercialBankAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(new CommercialBankAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
