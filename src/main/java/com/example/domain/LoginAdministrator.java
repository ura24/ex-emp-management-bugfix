package com.example.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * 管理者のログイン情報を返すエンティティ
 */
public class LoginAdministrator extends User {

	/** 管理者情報 */
	private final Administrator administrator;

	/**
	 * 管理者情報に、認可用ロールを設定
	 * @param admin 管理者情報
	 * @param authorities 権限情報リスト
	 */
	public LoginAdministrator(Administrator administrator, Collection<GrantedAuthority> authorityList) {
		super(administrator.getMailAddress(), administrator.getPassword(), authorityList);
		this.administrator = administrator;
	}

	/**
	 * 管理者情報を返す
	 * @return 管理者情報
	 */
	public Administrator getAdministrator() {
		return administrator;
	}
	
}
