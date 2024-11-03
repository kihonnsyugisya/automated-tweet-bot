package com.kihonsyugisya.enums;

public enum ExecutionContextKeys {
    FILTERED_ITEM_1("filteredItem1"),
    GENERATED_TWEET("generatedTweet");

    private final String key;

    ExecutionContextKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}