package pt.unl.fct.iadi.novaevents.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.NullSecurityContextRepository
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import pt.unl.fct.iadi.novaevents.service.DatabaseUserDetailsManager

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val cookieAuthenticationEntryPoint: CookieAuthenticationEntryPoint,
    private val loginRedirectCookieService: LoginRedirectCookieService,
    private val jwtService: JwtService,
) {
    @Bean
    fun authenticationProvider(
        databaseUserDetailsManager: DatabaseUserDetailsManager,
        passwordEncoder: PasswordEncoder,
    ): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(databaseUserDetailsManager)
        provider.setPasswordEncoder(passwordEncoder)
        return provider
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, authenticationProvider: AuthenticationProvider): SecurityFilterChain {
        val csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse().apply {
            setCookiePath("/")
        }

        http
            .csrf { csrf -> csrf.csrfTokenRepository(csrfTokenRepository) }
            .securityContext { securityContext ->
                securityContext.securityContextRepository(NullSecurityContextRepository())
            }
            .sessionManagement { sessions ->
                sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .requestCache { cache -> cache.disable() }
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, "/", "/clubs", "/clubs/*", "/clubs/*/events/*", "/events", "/login", "/403", "/404", "/error").permitAll()
                    .requestMatchers(HttpMethod.POST, "/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/clubs/*/events/new").hasAnyRole("EDITOR", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/clubs/*/events").hasAnyRole("EDITOR", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/clubs/*/events/*/edit").hasAnyRole("EDITOR", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/clubs/*/events/*").hasAnyRole("EDITOR", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/clubs/*/events/*/delete").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/clubs/*/events/*").authenticated()
                    .requestMatchers(HttpMethod.POST, "/logout").authenticated()
                    .anyRequest().authenticated()
            }
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint(cookieAuthenticationEntryPoint)
                exceptions.accessDeniedHandler { _, response, _ ->
                    response.sendError(HttpServletResponse.SC_FORBIDDEN)
                }
            }
            .logout { logout ->
                logout.logoutUrl("/logout")
                logout.logoutSuccessHandler { _, response, _ ->
                    jwtService.clearJwtCookie(response)
                    loginRedirectCookieService.clear(response)
                    response.sendRedirect("/login?logout")
                }
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
