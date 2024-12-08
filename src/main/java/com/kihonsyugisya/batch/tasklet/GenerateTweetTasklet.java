package com.kihonsyugisya.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kihonsyugisya.dto.OpenAiRequestDto;
import com.kihonsyugisya.dto.RakutenApiResponseDto.Item;
import com.kihonsyugisya.enums.ExecutionContextKeys;
import com.kihonsyugisya.service.BatchService;
import com.kihonsyugisya.service.OpenAiService;

@Component
public class GenerateTweetTasklet implements Tasklet {

    @Autowired
    private OpenAiService openAiService;
    
    @Autowired
    private BatchService batchService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    	// FetchProductInfoTaskletで保存された楽天APIの結果をExecutionContextから取得
    	Item item = (Item) chunkContext
    			.getStepContext()
    			.getStepExecution()
    			.getJobExecution()
    			.getExecutionContext()
    			.get(ExecutionContextKeys.FILTERED_ITEM_1.getKey());
    	
	    // OpenAI APIリクエストの作成
	    StringBuilder promptBuilder = new StringBuilder("ツイート情報:\n");
	    promptBuilder.append("商品名: ").append(item.getItemName()).append("\n");
	    promptBuilder.append("ランク: ").append(item.getRank()).append("\n");  
	    promptBuilder.append("キャッチコピー: ").append(item.getCatchcopy()).append("\n");  
	    promptBuilder.append("説明: ").append(item.getItemCaption()).append("\n");
	    promptBuilder.append("アフィリエイトリンク: ").append(item.getAffiliateUrl()).append("\n");
	    promptBuilder.append("価格: ").append(item.getItemPrice()).append("円\n");            
	    promptBuilder.append("ポイント還元率: ").append(item.getPointRate()).append("%\n");  
	    promptBuilder.append("ポイント還元開始: ").append(item.getPointRateStartTime()).append("\n");  
	    promptBuilder.append("ポイント還元終了: ").append(item.getPointRateEndTime()).append("\n\n");  

        
        OpenAiRequestDto requestDto = new OpenAiRequestDto();
        // Messageリストを作成し、Messageオブジェクトを追加
        List<OpenAiRequestDto.Message> messages = new ArrayList<>();
        messages.add(new OpenAiRequestDto.Message("user", promptBuilder.toString()));
        
        // リストをrequestDtoにセット
        requestDto.setMessages(messages);
        

        // OpenAI APIの呼び出し
        String tweetContent;
        try {
            tweetContent = openAiService.callOpenAiApi(requestDto);
        } catch (Exception e) {
            System.err.println("OpenAI API呼び出し中にエラーが発生しました: " + e.getMessage());
            throw e;  // 必要であれば例外をスローする
        }

     // 生成されたツイートをJobスコープのExecutionContextに保存
        chunkContext.getStepContext()
            .getStepExecution()
            .getJobExecution() // JobExecutionにアクセス
            .getExecutionContext()
            .put(ExecutionContextKeys.GENERATED_TWEET.getKey(), tweetContent);

        // 生成されたツイートに#PRが付いているかチェックし、なければ追加
        if (!tweetContent.contains("#PR")) {
            tweetContent += " #PR";
        }
        
        // アフィリエイトURLをDBに保存
        batchService.registerAffiliateUrl(item.getAffiliateUrl());

        System.out.println("生成されたツイート内容: " + tweetContent);

        return RepeatStatus.FINISHED;
    }
}
