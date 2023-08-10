package com.example.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * 管理者のログイン情報を返すエンティティ
 */
public class LoginAdministrator  extends User{

	/** 管理者情報 */
	private final Administrator admin;

	/**
	 * 管理者情報に、認可用ロールを設定
	 * @param admin 管理者情報
	 * @param authorities 権限情報リスト
	 */
	public LoginAdministrator(Administrator admin, Collection<? extends GrantedAuthority> authorities) {
		super(admin.getMailAddress(), admin.getPassword(), authorities);
		this.admin = admin;
	}

	/**
	 * 管理者情報を返す
	 * @return 管理者情報
	 */
	public Administrator geAdministrator() {
		return admin;
	}
	
}
