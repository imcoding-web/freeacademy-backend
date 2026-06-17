package fr.imcoding.edu365.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {
//        authenticationManagerBuilder.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
        DaoAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authenticationManagerBuilder.authenticationProvider(authProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    /*    http.antMatcher("/**").authorizeRequests()
            .antMatchers("/user").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login();*/
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/auth/**").permitAll().and()
                .authorizeRequests().antMatchers("/images/**").permitAll().and()
                .authorizeRequests().antMatchers("/user/activate").permitAll().and()
                .authorizeRequests().antMatchers("/user/verify*").permitAll().and()
                .authorizeRequests().antMatchers("/file/downloadFile/**").permitAll().and()
                .authorizeRequests().antMatchers("/user/add-new-user").permitAll().and()
                .authorizeRequests().antMatchers("/expert/register").permitAll().and()
                .authorizeRequests().antMatchers("/expert/inscription-requests").permitAll().and()
                .authorizeRequests().antMatchers("/expert/validate-request/**").permitAll().and()
                .authorizeRequests().antMatchers("/expert/verify-email").permitAll().and()
                .authorizeRequests().antMatchers("/expert/teacher-details**").permitAll().and()
                .authorizeRequests().antMatchers("/user/changePassword").permitAll().and()
                .authorizeRequests().antMatchers("/user/savePassword").permitAll().and()
                .authorizeRequests().antMatchers("/user/reset-password").permitAll().and()
                .authorizeRequests().antMatchers("/user/forget-password").permitAll().and()
                .authorizeRequests().antMatchers("/owner").permitAll().and()
                .authorizeRequests().antMatchers("/lodger").permitAll().and()
                .authorizeRequests().antMatchers("/city").permitAll().and()
                .authorizeRequests().antMatchers("/video/stream/**").permitAll().and()
                .authorizeRequests().antMatchers("/announcement/search").permitAll().and()
                .authorizeRequests().antMatchers("/announcement/details/**").permitAll().and()
                .authorizeRequests().antMatchers("/announcement/last-published").permitAll().and()
                .authorizeRequests().antMatchers("/**/filtred").permitAll().and()
                .authorizeRequests().antMatchers("/skill**").permitAll().and()
                .authorizeRequests().antMatchers("/package/by-skill-area").permitAll().and()
                .authorizeRequests().antMatchers("/**/import").permitAll().and()
                .authorizeRequests().antMatchers("/announcement/similar").permitAll().and()
                .authorizeRequests().antMatchers("/sobflous/**").permitAll().and()
                .authorizeRequests().antMatchers("/runpay/**").permitAll().and()
                .authorizeRequests().antMatchers("/newsletter").permitAll().and()
                .authorizeRequests().antMatchers("/expert-profile").permitAll().and()
                .authorizeRequests().antMatchers("/teacher-course/last-six-course").permitAll().and()
                .authorizeRequests().antMatchers("/test/**").permitAll().and()
                .authorizeRequests().antMatchers("/zoom-meeting/**").permitAll().and()
                .authorizeRequests().antMatchers("/teacher-course/details/**").permitAll().and()
                .authorizeRequests().antMatchers("/teacher-course/similar/**").permitAll().and()
                .authorizeRequests().antMatchers("/chatbot/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }
}
