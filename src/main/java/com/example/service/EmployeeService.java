package com.example.service;

import java.io.IOException;
import java.sql.Date;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Employee;
import com.example.form.InsertEmployeeForm;
import com.example.repository.EmployeeRepository;

/**
 * 従業員情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * 従業員情報を全件取得します.
	 * 
	 * @return 従業員情報一覧
	 */
	public List<Employee> showList() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}

	/**
	 * 従業員情報を取得します.
	 * 
	 * @param id ID
	 * @return 従業員情報
	 * @throws org.springframework.dao.DataAccessException 検索されない場合は例外が発生します
	 */
	public Employee showDetail(Integer id) {
		Employee employee = employeeRepository.load(id);
		return employee;
	}

	/**
	 * 従業員情報を更新します.
	 * 
	 * @param employee 更新した従業員情報
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}

	/**
	 * 従業員名で検索します。
	 * @param name 検索した従業員情報一覧
	 * @return
	 */
	public List<Employee> search(String name) {
		return employeeRepository.search(name);
	}

	/**
	 * 従業員情報を登録します
	 * @param insertEmployeeForm 従業員登録フォーム
	 * @param fileExtension 画像のファイル拡張子
	 * @throws IOException 入出力例外
	 */
	public void insert(InsertEmployeeForm insertEmployeeForm, String fileExtension) throws IOException {
		// 画像ファイルをBase64エンコードして、適切なデータURIを生成
		String base64FileString = Base64.getEncoder().encodeToString(insertEmployeeForm.getImage().getBytes());
		if ("jpg".equals(fileExtension)) {
			base64FileString = "data:image/jpeg;base64," + base64FileString;
		} else if ("png".equals(fileExtension)) {
			base64FileString = "data:image/png;base64," + base64FileString;
		}

	    // 従業員情報を作成
		Employee employee = new Employee(
								insertEmployeeForm.getId(), 
								insertEmployeeForm.getName(), 
								base64FileString, 
								insertEmployeeForm.getGender(), 
								Date.valueOf(insertEmployeeForm.getHireDate()), 
								insertEmployeeForm.getMailAddress(), 
								insertEmployeeForm.getZipCode(), 
								insertEmployeeForm.getAddress(), 
								insertEmployeeForm.getTelephone(), 
								Integer.parseInt(insertEmployeeForm.getSalary()), 
								insertEmployeeForm.getCharacteristics(), 
								Integer.parseInt(insertEmployeeForm.getDependentsCount()));

		employeeRepository.insert(employee);
	}

	/**
	 * メールアドレスから従業員情報を検索します
	 * @param mailAddress メールアドレス
	 * @return メールアドレスに一致する従業員情報
	 */
	public Employee searchByMailAddress(String mailAddress) {
		return employeeRepository.findByMailAddress(mailAddress);
	}
}
