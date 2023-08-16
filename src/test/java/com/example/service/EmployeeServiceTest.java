package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;

@SpringBootTest
public class EmployeeServiceTest {

    // テスト対象のクラス
    @Autowired
    private EmployeeService employeeService;

    // 依存するクラス
    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    void testShowDetail() {
        // テストデータ作成
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("山田太郎");
        employee.setMailAddress("yamada@example.com");

        // モックの振る舞いを設定
        when(employeeRepository.load(1)).thenReturn(employee);

        // メソッドを呼び出す
        Employee result = employeeService.showDetail(1);

        // 検証
        assertEquals(1, result.getId(), "IDが一致しません");
        assertEquals("山田太郎", result.getName(), "名前が一致しません");
        assertEquals("yamada@example.com", result.getMailAddress(), "メールアドレスが一致しません");
    }

    @Test
    void testShowList() {
        // テストデータ作成
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setName("山田太郎");
        employee1.setMailAddress("yamada@example.com");
        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setName("佐藤二郎");
        employee2.setMailAddress("satou@example.com");

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);

        // モックの振る舞いを設定
        when(employeeRepository.findAll()).thenReturn(employeeList);

        // メソッド呼び出す
        List<Employee> result = employeeService.showList();

        // 検証
        assertEquals(2, result.size());
        assertEquals("山田太郎", result.get(0).getName(), "名前が一致しません");
        assertEquals("satou@example.com", result.get(1).getMailAddress(), "メールアドレスが一致しません");
    }
}
