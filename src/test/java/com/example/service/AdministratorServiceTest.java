package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;

@SpringBootTest
public class AdministratorServiceTest {

    // テスト対象のクラス
    @Autowired
    private AdministratorService administratorService;

    // 依存するクラス
    @MockBean
    private AdministratorRepository administratorRepository;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Test
    public void insertTest() {
        // テストデータの作成
        Administrator admin = new Administrator(1, "山田太郎", "test@example.com", "test1234");
        
        // インサートを検索の実行
        administratorService.insert(admin);
        when(administratorRepository.findByMailAddress("test@example.com")).thenReturn(admin);

        // 結果を取得
        Administrator result = administratorService.findByMailAddress("test@example.com");

        // 検証
        assertEquals(1, result.getId(), "IDが一致しません");
        assertEquals("山田太郎", result.getName(), "名前が一致しません");
    }

    // @Test
    // void testLoginSuccess() {
    //     // テストデータの作成
    //     String mailAddress = "test@example.com";
    //     String password = "test1234";
    //     String encodedPassword = passwordEncoder.encode(password);
    //     Administrator admin = new Administrator(1, "山田太郎", mailAddress, encodedPassword);

    //     // モックの振る舞いを設定
    //     when(administratorRepository.findByMailAddress(mailAddress)).thenReturn(admin);

    //     // ログイン実行
    //     Administrator result = administratorService.login(mailAddress, password);

    //     // 検証
    //     assertNotNull(result, "ログイン成功");
    // }

    // @Test
    // void testLoginFailure() {
    //     // テストデータの作成
    //     String mailAddress = "test@example.com";
    //     String password = "test1234";
    //     String encodedPassword = passwordEncoder.encode("failurePassword");
    //     Administrator admin = new Administrator(1, "山田太郎", mailAddress, encodedPassword);

    //     // モックの振る舞いを設定
    //     when(administratorRepository.findByMailAddress(mailAddress)).thenReturn(admin);

    //     // ログイン実行
    //     Administrator result = administratorService.login(mailAddress, password);

    //     // 検証
    //     assertNull(result, "ログイン失敗");
    // }
}
