package com.kihonsyugisya.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kihonsyugisya.dto.RakutenApiResponseDto;
import com.kihonsyugisya.enums.ExecutionContextKeys;
import com.kihonsyugisya.service.BatchService;
import com.kihonsyugisya.service.RakutenApiService;

@Component
public class FetchProductInfoTasklet implements Tasklet {

    @Autowired
    private RakutenApiService rakutenApiService;    
    
    @Autowired
    private BatchService batchService;
    
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {     
        // Rakuten APIを呼び出して結果を取得
        RakutenApiResponseDto response = rakutenApiService.fetchTopProducts();
        
        // 取得したアイテムの数をログに出力
        if (response.getItems() != null) {
            System.out.println("Fetched items count: " + response.getItems().size());
        } else {
            System.out.println("Fetched items is null.");
            return RepeatStatus.FINISHED;
        }

        // 条件を満たすアイテムを格納する変数
        RakutenApiResponseDto.Item filteredItem = null;
        // アイテムをリストから条件に基づいてフィルタリング
        for (RakutenApiResponseDto.Item item : response.getItems()) {
            System.out.println("Checking item: " + item.getItemName());

            // アフィリエイトURLの存在確認。いままで投稿したことのあるURLは避けたい
            boolean affiliateUrlExists = batchService.isAffiliateUrlExists(item.getAffiliateUrl());

            // 条件をチェック
            if (item.getAffiliateUrl() != null && !item.getAffiliateUrl().isEmpty() &&
                item.getItemCaption() != null && !item.getItemCaption().isEmpty() &&
                item.getAvailability() == 1 &&
                !affiliateUrlExists) { 

                filteredItem = item;  // 条件を満たす場合、変数に格納
                System.out.println("Filtered item found: " + filteredItem.getItemName());
                break;  // 最初のアイテムを見つけたら終了
            } else {
                System.out.println("Item does not match criteria: " + item.getItemName());
            }
        }


        // ExecutionContextに保存
        if (filteredItem != null) {
            chunkContext
            .getStepContext()
            .getStepExecution()
            .getJobExecution()
            .getExecutionContext()
            .put(ExecutionContextKeys.FILTERED_ITEM_1.getKey(), filteredItem);
            System.out.println("Filtered item saved to ExecutionContext: " + filteredItem.getItemName());
        } else {
            System.out.println("No item found matching the criteria.");
        }

        return RepeatStatus.FINISHED;
    }


}
