package com.kihonsyugisya.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kihonsyugisya.dto.RakutenApiResponseDto;
import com.kihonsyugisya.service.RakutenApiService;

@Component
public class FetchProductInfoTasklet implements Tasklet {

    @Autowired
    private RakutenApiService rakutenApiService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // Rakuten APIを呼び出して結果を取得
        RakutenApiResponseDto response = rakutenApiService.fetchTopProducts();

        // 条件を満たすアイテムを格納するリスト
        List<RakutenApiResponseDto.Item> filteredItems = new ArrayList<>();

        // アイテムをリストから条件に基づいてフィルタリング
        for (RakutenApiResponseDto.Item item : response.getItems()) {
            if (filteredItems.size() >= 2) {
                break;  // 2件見つかったら終了
            }
            // 条件をチェック
            if (item.getAffiliateUrl() != null && !item.getAffiliateUrl().isEmpty() &&
                item.getItemCaption() != null && !item.getItemCaption().isEmpty() &&
                item.getAvailability() == 1) {
                
                filteredItems.add(item);  // 条件を満たす場合、リストに追加
            }
        }

        // ExecutionContextに保存
        for (int i = 0; i < filteredItems.size(); i++) {
            chunkContext.getStepContext().getStepExecution().getExecutionContext().put("filteredItem" + (i + 1), filteredItems.get(i));
        }

        if (!filteredItems.isEmpty()) {
            System.out.println(filteredItems.size() + " filtered items saved to ExecutionContext.");
        } else {
            System.out.println("No items found matching the criteria.");
            // TODO: 2件見つからなかった場合の処理を追加する（例: エラーメッセージのログ出力、リトライ処理、異常フローの管理など）
        }

        return RepeatStatus.FINISHED;
    }
}
