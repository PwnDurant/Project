package com.zqq.javaailangchain4j.config;


import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeServerlessIndexConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingStoreConfig {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(){
        //创建向量存储
        EmbeddingStore<TextSegment> embeddingStore = PineconeEmbeddingStore.builder()
                .apiKey("pcsk_3ZWQAY_8rfSBe5SniqALSfGQNrhrA6fVxKPUedt2k862Phay6RxNSjwWt6PLBvF6mPuyaS")
                .index("chase")//如果指定的索引不存在，将创建一个新的索引
                .nameSpace("knowledge") //如果指定的名称空间不存在，将创建一个新的名称空间
                .createIndex(PineconeServerlessIndexConfig.builder()
                                .cloud("AWS") //指定索引部署在 AWS 云服务上。
                                .region("us-east-1") //指定索引所在的 AWS 区域为 us-east-1。
                                .dimension(embeddingModel.dimension()) //指定索引的向量维度，该维度与 embeddedModel 生成的向量维度相同。
                                .build())
                .build();
        return embeddingStore;
    }

}
