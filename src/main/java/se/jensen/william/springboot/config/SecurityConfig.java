package se.jensen.william.springboot.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;


/**
 * Säkerhetskonfigurationen för applikationen.
 *
 * Denna konfigurationsklass hanterar all säkerhetsrelaterad konfiguration.
 * Den är konfigurerad JWT-baserad och CORS-inställningar som används för koppling med frontend,
 * Klassen har lösenordskryptering och samt har RSA-nyckel
 *
 * @author William
 * @author Patric
 */


@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175",
                "http://localhost:3000",
                "https://oral-ermina-duckduckdevs-f8201139.koyeb.app"

        ));

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(

            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/request-token").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }

    @Bean
    public KeyPair keyPair(
            @Value("${jwt.private_key}") String privateKey,
            @Value("${jwt.public_key}") String publicKey
    ) throws Exception {

        byte[] privateBytes = Base64.getDecoder().decode(privateKey);
        byte[] publicBytes = Base64.getDecoder().decode(publicKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privKey = keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(privateBytes)
        );

        PublicKey pubKey = keyFactory.generatePublic(
                new X509EncodedKeySpec(publicBytes)
        );

        return new KeyPair(pubKey, privKey);
    }

    // Source
    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID("jwt-key-1")
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);

        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    // JWT Encoder
    @Bean
    public JwtEncoder jwtEncoder (JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    // JWT Decoder
    @Bean
    public JwtDecoder jwtDecoder (KeyPair keyPair) {
        return NimbusJwtDecoder
                .withPublicKey((RSAPublicKey) keyPair.getPublic())
                .build();
    }

    // JWT Authentication
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter =
                new JwtGrantedAuthoritiesConverter();

        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter authenticationConverter =
                new JwtAuthenticationConverter();

        authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return authenticationConverter;
    }

    // AUTH Manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Password - Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
