package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.domain.Employee;
import com.example.form.InsertEmployeeForm;
import com.example.form.SearchForm;
import com.example.form.UpdateEmployeeForm;
import com.example.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@GetMapping("/showList")
	public String showList(Model model, SearchForm form) {
		List<Employee> employeeList = employeeService.showList();
		model.addAttribute("employeeList", employeeList);
		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id    リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@GetMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form 従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@PostMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員名で検索する
	/////////////////////////////////////////////////////
	/**
	 * 従業員名で検索した結果を、一覧画面に表示します。
	 * @param form 従業員名フォーム
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@PostMapping("/search")
	public String search(SearchForm form, Model model) {
		List<Employee> employeeList = new ArrayList<>();

		if (form.getName() == null) {
			// 値がない場合は、従業員一覧を出力
			employeeList = employeeService.showList();
		
		} else {
			// 入力された従業員名で検索された従業員一覧を出力
			employeeList = 	employeeService.search(form.getName());

			// 検索結果が0件だった場合、従業員一覧を出力
			if (employeeList.isEmpty()) {
				model.addAttribute("noSearchResult", "１件もありませんでした");
				employeeList = employeeService.showList();
			}
		}

		model.addAttribute("employeeList", employeeList);
		return "employee/list";
	}

	/**
	 * 従業員登録画面を出力します.
	 * 
	 * @return 従業員登録画面
	 */
	@GetMapping("/toInsert")
	public String toInsert(InsertEmployeeForm insertEmployeeForm, Model model) {
		// 性別マップを作成
		Map<String, String> genderMap = new LinkedHashMap<>();
		genderMap.put("男性", "男性");
		genderMap.put("女性", "女性");
		genderMap.put("その他", "その他");
		model.addAttribute("genderMap", genderMap);

		return "employee/insert";
	}

	/**
	 * 従業員情報を登録します
	 * @param insertEmployeeForm 従業員登録フォーム
	 * @param result バインディング結果
	 * @param model モデル
	 * @return 従業員一覧画面
	 * @throws IOException
	 */
	@PostMapping("/insert")
	public String insert(@Validated InsertEmployeeForm insertEmployeeForm, BindingResult result, Model model) throws IOException {
		// メールアドレスの重複チェック
		if (employeeService.searchByMailAddress(insertEmployeeForm.getMailAddress()) != null) {
			result.rejectValue("mailAddress", "", "そのメールアドレスは既に登録されています");
		}

		MultipartFile image = insertEmployeeForm.getImage();
		String fileExtension = null;
		// 画像のバリデーション
        if (image.isEmpty()) {
			result.rejectValue("image", "", "画像を選択してください");
        } else {
			int point = image.getOriginalFilename().lastIndexOf(".");
			fileExtension = image.getOriginalFilename().substring(point + 1);
			if (!fileExtension.equals("jpg") && !fileExtension.equals("png")) {
				result.rejectValue("image", "", "拡張子は.jpgか.pngのみに対応しています");
			}
		}

		// エラーがある場合、従業員登録画面を再表示
		if (result.hasErrors()) {
			return toInsert(insertEmployeeForm, model);
		}

		employeeService.insert(insertEmployeeForm, fileExtension);
		
		return "redirect:/employee/showList";
	}
}
