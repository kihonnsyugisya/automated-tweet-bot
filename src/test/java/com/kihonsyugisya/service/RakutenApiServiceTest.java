package com.kihonsyugisya.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.kihonsyugisya.dto.RakutenApiResponseDto;
import com.kihonsyugisya.entity.RakutenApiParametersEntity;
import com.kihonsyugisya.properties.RakutenProperties;
import com.kihonsyugisya.repository.RakutenApiParametersMapper;

@SpringBootTest
public class RakutenApiServiceTest {

    @Autowired
    private RakutenApiService rakutenApiService;

//    @MockBean
//    private RakutenApiParametersMapper rakutenApiParametersMapper;
    
    @Autowired
    private RakutenApiParametersMapper rakutenApiParametersMapper;

    @Autowired
    private RakutenProperties rakutenProperties;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        // ここでは特に何もする必要がありません。
    }

    /**
     * fetchTopProductsメソッドのテスト。
     * 
     * このテストは、最新の楽天APIパラメータを取得し、それを基にAPIを呼び出して
     * 結果がnullでないことを確認します。
     * 
     * 期待される結果：
     * - RakutenApiResponseDtoがnullでないこと
     * - APIが正しく設定されている場合、取得したデータの内容が期待される形式であること
     * 
     * 注意：
     * 実際のAPIを呼び出すため、APIが正しくセットアップされている必要があります。
     */
    @Test
    public void testFetchTopProducts() {
        // Arrange
        RakutenApiParametersEntity parametersEntity = new RakutenApiParametersEntity();
        parametersEntity.setAge(20);
        parametersEntity.setSex((short) 1);

        // モックの設定
//        when(rakutenApiParametersMapper.findLatestRecord()).thenReturn(parametersEntity);

        // Act
        RakutenApiResponseDto result = rakutenApiService.fetchTopProducts();
        
        System.out.println("-- result --------------------------------------");
        System.out.println(result);
        // Assert
        assertNotNull(result, "APIのレスポンスがnullであってはならない");
        // ここでresultの内容を検証する
    }
}
