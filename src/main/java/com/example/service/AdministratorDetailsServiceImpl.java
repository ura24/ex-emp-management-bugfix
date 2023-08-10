package com.example.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.domain.Administrator;
import com.example.domain.LoginAdministrator;
import com.example.repository.AdministratorRepository;

/**
 * ログイン後の管理者情報に権限情報を付与するサービスクラス
 */
@Service
public class AdministratorDetailsServiceImpl implements UserDetailsService {

	/** 管理者テーブルから情報を取得するレポジトリ */
	@Autowired
	private AdministratorRepository administratorRepository;

	/**
	 * 管理者テーブルから検索して、ログイン情報をログイン情報を構成して返す
	 * @return ログイン情報
	 */
	@Override
	public UserDetails loadUserByUsername(String mailAddress) throws UsernameNotFoundException {
		Administrator admin = administratorRepository.findByMailAddress(mailAddress);
		if (admin == null) {
			throw new UsernameNotFoundException("そのメールアドレスは登録されていません。");
		}

		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		// ユーザ権限付与
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

		return new LoginAdministrator(admin, authorityList);
	}

}
