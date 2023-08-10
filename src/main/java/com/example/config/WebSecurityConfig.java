package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * ログイン認証用の設定クラス
 */
@Configuration
public class WebSecurityConfig {

	/**
	 * 静的リソースに対して、セキュリティの設定を無効にする
	 * @return セキュリティを無効にする静的リソース
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**");
	}


	/**
	 * 認可の設定、ログイン・ログインに関わる設定をする
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// ログインの必要があるURLを設定
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/", "/insert", "/toInsert").permitAll()
						.anyRequest().authenticated())

				// ログインに関する設定
				.formLogin((form) -> form
						.loginPage("/")
						.loginProcessingUrl("/login")
						.failureUrl("/?error=true")
						.defaultSuccessUrl("/employee/showList", true)
						.usernameParameter("mailAddress")
						.passwordParameter("password")
						.permitAll())

				// ログアウトに関する設定
				.logout((logout) -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout**"))
						.logoutSuccessUrl("/")
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(true));

		return http.build();
	}

	/**
	 * bcryptアルゴリズムでハッシュ化
	 * @return bcryptアルゴリズムでハッシュ化する実装オブジェクト
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
