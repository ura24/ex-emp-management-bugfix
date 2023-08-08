package com.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.Administrator;
import com.example.form.InsertAdministratorForm;
import com.example.service.AdministratorService;

/**
 * 管理者情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/")
public class AdministratorController {

	@Autowired
	private AdministratorService administratorService;

//	@Autowired
//	private HttpSession session;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public InsertAdministratorForm setUpInsertAdministratorForm() {
		return new InsertAdministratorForm();
	}

	// (SpringSecurityに任せるためコメントアウトしました)
//	@ModelAttribute
//	public LoginForm setUpLoginForm() {
//		return new LoginForm();
//	}

	/////////////////////////////////////////////////////
	// ユースケース：管理者を登録する
	/////////////////////////////////////////////////////
	/**
	 * 管理者登録画面を出力します.
	 * 
	 * @return 管理者登録画面
	 */
	@GetMapping("/toInsert")
	public String toInsert() {
		return "administrator/insert";
	}

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param form 管理者情報用フォーム
	 * @return ログイン画面へリダイレクト
	 */
	@PostMapping("/insert")
	public String insert(@Validated InsertAdministratorForm form, BindingResult result) {

		// パスワード確認
		if (!form.getPassword().equals(form.getConfirmationPassword())) {
			result.rejectValue("password", "", "パスワードが一致していません");
			result.rejectValue("confirmationPassword", "", "");
		}

		// メールアドレスが重複している場合の処理
		Administrator existAdministrator = administratorService.findByMailAddress(form.getMailAddress());
		if (existAdministrator != null) {
			result.rejectValue("mailAddress", "", "そのメールアドレスは既に登録されています");
		}

		// エラーが一つでもあれば入力画面に戻る
		if (result.hasErrors()) {
			return toInsert();
		}

		Administrator administrator = new Administrator();
		// フォームからドメインにプロパティ値をコピー
		BeanUtils.copyProperties(form, administrator);

		// 登録処理
		administratorService.insert(administrator);
		return "redirect:/";
	}

	/////////////////////////////////////////////////////
	// ユースケース：ログインをする
	/////////////////////////////////////////////////////
	/**
	 * ログイン画面を出力します.
	 * 
	 * @return ログイン画面
	 */
	@GetMapping("/")
	public String toLogin(Model model, @RequestParam(required = false) String error) {
		System.err.println("login error:" + error);
		if (error != null) {
			System.err.println("login failed");
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
		}
		return "administrator/login";
	}

//	/**
//	 * ログインします. (SpringSecurityに任せるためコメントアウトしました)
//	 * 
//	 * @param form
//	 *            管理者情報用フォーム
//	 * @param result
//	 *            エラー情報格納用オブッジェクト
//	 * @return ログイン後の従業員一覧画面
//	 */
//	@PostMapping("/login")
//	public String login(LoginForm form, BindingResult result) {
//		Administrator administrator = administratorService.login(form.getMailAddress(), form.getPassword());
//		if (administrator == null) {
//			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
//			return toLogin();
//		}
//		session.setAttribute("administratorName", administrator.getName());
//		return "forward:/employee/showList";
//	}

	/////////////////////////////////////////////////////
	// ユースケース：ログアウトをする
	/////////////////////////////////////////////////////
	/**
	 * ログアウトをします. (SpringSecurityに任せるためコメントアウトしました)
	 * 
	 * @return ログイン画面
	 */
//	@GetMapping(value = "/logout")
//	public String logout() {
//		session.invalidate();
//		return "redirect:/";
//	}

}
