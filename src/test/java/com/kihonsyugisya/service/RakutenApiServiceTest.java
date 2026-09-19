package com.kihonsyugisya.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.kihonsyugisya.dto.RakutenApiResponseDto;
import com.kihonsyugisya.entity.RakutenApiParametersEntity;
import com.kihonsyugisya.properties.RakutenProperties;
import com.kihonsyugisya.repository.RakutenApiParametersMapper;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class RakutenApiServiceTest {

    @Autowired
    private RakutenApiService rakutenApiService;

    @MockBean
    private RakutenApiParametersMapper rakutenApiParametersMapper;

    @Autowired
    private RakutenProperties rakutenProperties;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        RakutenApiParametersEntity parametersEntity = new RakutenApiParametersEntity();
        parametersEntity.setAge(30);
        parametersEntity.setSex((short) 0);
        when(rakutenApiParametersMapper.findLatestRecord()).thenReturn(parametersEntity);
    }

    @Test
    public void testFetchTopProducts_sendsAccessKeyAndParsesResponse() {
        String rankingHost = "https://openapi.rakuten.co.jp/ichibaranking/api/IchibaItem/Ranking/20220601";
        String json = """
                {
                  "Items": [
                    {
                      "rank": 1,
                      "itemName": "テスト商品",
                      "catchcopy": "キャッチ",
                      "itemCaption": "説明文",
                      "affiliateUrl": "https://example.com/aff",
                      "availability": 1,
                      "pointRate": 1,
                      "pointRateStartTime": "2026-01-01 00:00",
                      "pointRateEndTime": "2026-12-31 23:59",
                      "itemPrice": "1000"
                    }
                  ]
                }
                """;

        mockServer.expect(requestTo(startsWith(rankingHost)))
                .andExpect(queryParam("applicationId", rakutenProperties.getApplicationId()))
                .andExpect(queryParam("accessKey", rakutenProperties.getAccessKey()))
                .andExpect(queryParam("age", "30"))
                .andExpect(queryParam("sex", "0"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        RakutenApiResponseDto result = rakutenApiService.fetchTopProducts();

        assertNotNull(result);
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());
        assertEquals("テスト商品", result.getItems().get(0).getItemName());
        mockServer.verify();
    }
}
