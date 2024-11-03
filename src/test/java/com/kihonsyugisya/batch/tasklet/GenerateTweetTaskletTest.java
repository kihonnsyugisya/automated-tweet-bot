package com.kihonsyugisya.batch.tasklet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;

import com.kihonsyugisya.dto.OpenAiRequestDto;
import com.kihonsyugisya.dto.RakutenApiResponseDto.Item;
import com.kihonsyugisya.enums.ExecutionContextKeys;
import com.kihonsyugisya.service.OpenAiService;

class GenerateTweetTaskletTest {

    @Mock
    private OpenAiService openAiService;

    @InjectMocks
    private GenerateTweetTasklet generateTweetTasklet;

    private StepContribution stepContribution;
    private ChunkContext chunkContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setUpExecutionContext();
    }

    private void setUpExecutionContext() {
        // StepExecutionを作成し、それを使ってChunkContextを作成
        var stepExecution = MetaDataInstanceFactory.createStepExecution();
        chunkContext = new ChunkContext(new StepContext(stepExecution));
        stepContribution = new StepContribution(stepExecution);

        // テストデータの準備
        Item item1 = createItem("商品A", "商品Aの説明", "http://example.com/itemA", "1000");

        // ExecutionContextに楽天APIのアイテム情報をセット
        stepExecution.getExecutionContext().put(ExecutionContextKeys.FILTERED_ITEM_1.getKey(), item1);
    }

    private Item createItem(String name, String caption, String url, String price) {
        Item item = new Item();
        item.setItemName(name);
        item.setItemCaption(caption);
        item.setAffiliateUrl(url);
        item.setItemPrice(price);
        return item;
    }
    
    /**
     * GenerateTweetTasklet#execute(StepContribution, ChunkContext)の正常系テスト。
     * 楽天APIから取得したアイテム情報を基に、生成されるツイートが期待通りであることを検証します。
     * 入力: 商品Aの情報
     * 期待値: "商品A - 商品Aの説明 http://example.com/itemA #PR"
     *
     * @throws Exception テスト実行中に例外が発生した場合
     */
    @Test
    void testExecute_NormalCase() throws Exception {
        // モックの設定
        String expectedTweet = "商品A - 商品Aの説明 http://example.com/itemA #PR";
        when(openAiService.callOpenAiApi(any(OpenAiRequestDto.class))).thenReturn(expectedTweet);

        // Taskletの実行
        generateTweetTasklet.execute(stepContribution, chunkContext);

        // ExecutionContextから生成されたツイートを取得して検証
        String actualTweet = (String) chunkContext.getStepContext().getStepExecution().getExecutionContext().get("generatedTweet");
        assertEquals(expectedTweet, actualTweet, "生成されたツイートが期待通りではありません");
    }
}
