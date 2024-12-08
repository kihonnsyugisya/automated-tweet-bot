package com.kihonsyugisya.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RakutenApiResponseDto implements Serializable {
    private static final long serialVersionUID = 1L; // シリアルバージョンUIDを追加

    @JsonProperty("Items") // JSONのItemsにマッピング
    private List<Item> items;

    @Data
    public static class Item implements Serializable {
        private static final long serialVersionUID = 1L; // シリアルバージョンUIDを追加

        private int rank;
        private String itemName;
        private String catchcopy;
        private String itemCaption;
        private String affiliateUrl;
        private int availability; 
        private int pointRate;
        private String pointRateStartTime;
        private String pointRateEndTime;
        private String itemPrice;
    }
}
