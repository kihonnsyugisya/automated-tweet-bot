package com.kihonsyugisya.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RakutenApiResponseDto {
	
	@JsonProperty("Items") // JSONのItemsにマッピング
    private List<Item> Items;

    @Data
    public static class Item {
        private int rank;
        private String itemName;
        private String catchcopy;
        private String itemCaption;
        private String affiliateUrl;
        private int availability;  // availabilityは数値なのでintに修正
        private int pointRate;
        private String pointRateStartTime;  // 空文字やnullの可能性があるためStringに
        private String pointRateEndTime;    // 同様にStringに
    }
}

