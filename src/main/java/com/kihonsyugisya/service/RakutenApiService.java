package com.kihonsyugisya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.kihonsyugisya.dto.RakutenApiResponseDto;
import com.kihonsyugisya.entity.RakutenApiParametersEntity;
import com.kihonsyugisya.properties.RakutenProperties;
import com.kihonsyugisya.repository.RakutenApiParametersMapper;

@Service
public class RakutenApiService {

    private final RestTemplate restTemplate;
    private final RakutenProperties rakutenPropertie;

    public RakutenApiService(RestTemplate restTemplate, RakutenProperties rakutenPropertie) {
        this.restTemplate = restTemplate;
        this.rakutenPropertie = rakutenPropertie;
    }
    
    @Autowired
    private RakutenApiParametersMapper rakutenApiParametersMapper;

    public RakutenApiResponseDto fetchTopProducts() {
    	
    	// DBから最新のAPIパラメータを取得
    	RakutenApiParametersEntity rakutenApiRarametersEntity = rakutenApiParametersMapper.findLatestRecord();
    	
        // プロパティファイルからAPIのURLとIDを取得
        String apiUrl = rakutenPropertie.getApiUrl(); 
        String applicationId = rakutenPropertie.getApplicationId();
        String accessKey = rakutenPropertie.getAccessKey();
        String affiliateId = rakutenPropertie.getAffiliateId();

        // URIビルダーの設定
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("applicationId", applicationId)
                .queryParam("accessKey", accessKey)
                .queryParam("affiliateId", affiliateId)
                .queryParam("formatVersion", 2)
                .queryParam("page", 1)
//                .queryParam("carrier", 1) // レスポンスのアフィリエイトURLが機能しなくなる事象があったのでコメントアウト
                .queryParam("elements", "rank,itemName,itemCaption,catchcopy,affiliateUrl,availability,pointRate,pointRateStartTime,pointRateEndTime,itemPrice");

        // ageがnullでない場合のみクエリに追加
        if (rakutenApiRarametersEntity.getAge() != null) {
            uriBuilder.queryParam("age", rakutenApiRarametersEntity.getAge());
        }

        // sexがnullでない場合のみクエリに追加
        if (rakutenApiRarametersEntity.getSex() != null) {
            uriBuilder.queryParam("sex", rakutenApiRarametersEntity.getSex());
        }

        // 完成したURLを文字列として取得
        String url = uriBuilder.build().toUriString();
        
        // URLの確認用ログ出力（accessKeyはマスク）
        System.out.println("--- URL ----------------------------------------------------------");
        System.out.println(maskAccessKeyInUrl(url));

        RakutenApiResponseDto apiResponse = restTemplate.getForObject(url, RakutenApiResponseDto.class);

        // レスポンスの一部をログに出力して確認
        System.out.println(apiResponse.getItems().get(0).getPointRateStartTime() + ": response");
        System.out.println("--- RakutenResponse----------------------------------------------------------");
        System.out.println(maskAccessKeyInUrl(url));
        
        
        return apiResponse; // ProductDtoのリストを返す
    }

    private static String maskAccessKeyInUrl(String url) {
        return url.replaceAll("(accessKey=)[^&]*", "$1***");
    }
    
}
