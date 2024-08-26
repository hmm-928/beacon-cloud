package com.mashibing.strategy.filter.impl;

import com.hmm.common.constant.CacheConstant;
import com.hmm.common.model.StandardSubmit;
import com.mashibing.strategy.client.BeaconCacheClient;
import com.mashibing.strategy.filter.StrategyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 这里就是针对发送的短信内容做敏感词的匹配校验。
 *
 * 短信内容分为通知类，营销类。这两种都需要校验。
 *
 * 同时整个策略就是将短信内容进行分词，基于IK分词器对短信内容进行分词。
 *
 * 将分词出来的内容通过数据库存储的数据进行匹配。
 *
 * 数据库效率比较低，将数据扔到Redis中存储。
 *
 * 方式一：将敏感词内容作为key，同步到Redis中，将短信内容分词后，基于get方法去Redis中做匹配，如果有匹配上的内容，直接就无法通过敏感词校验。
 *
 * 方式一的缺点比较明显，如果短信文字比较多，OpenFeign的IO成本还有Redis的IO成本都是比较高。这种方式可以实现功能，但是效率一般。
 *
 * 方式二：将敏感词整理为Set集合，存储到Redis的Set结构中。在做校验时，可以将短信内容进行分词，将分词的内容整理为Set集合存储到Redis的Set结构中。将短信内容的Set集合与敏感词的Set集合做交集操作，如果交集后有内容，证明有敏感词的。如果交集后，返回了一个空的Set，证明没有交集。
 *
 * 方式二，最多有两次网络IO。但是效率相比方式一高很多。所有敏感词需要存储在一个key中，如果敏感词内容比较多，可能会造成数据的倾斜，大量的数据都存储在了一个Redis节点中。（可以解决）
 *
 */
//敏感词校验
@Service(value = "dirtyword")
@Slf4j
public class DirtyWordStrategyFilter implements StrategyFilter {
    @Autowired
    private BeaconCacheClient cacheClient;
    @Override
    public void strategy(StandardSubmit submit) {

        log.info("【策略模块-敏感词校验】   校验ing…………");
        //1、 获取短信内容
        String text = submit.getText();

        //2、 对短信内容进行分词，并且将分析内容存储到集合中
        Set<String> contents = new HashSet<>();
        StringReader reader = new StringReader(text);
        //IK分词
        IKSegmenter ik = new IKSegmenter(reader,false);
        Lexeme lex = null;
        while(true){
            try {
                if ((lex = ik.next()) == null) break;
            } catch (IOException e) {
                log.info("【策略模块-敏感词校验】   IK分词器在处理短信内容时，出现异常 e = {}" ,e.getMessage());
            }
            contents.add(lex.getLexemeText());
        }


        //3、 调用Cache缓存模块的交集方法，拿到结果

        Set<Object> dirtyWords = cacheClient.sinterStr(UUID.randomUUID().toString(), CacheConstant.DIRTY_WORD, contents.toArray(new String[]{}));

        //4、 根据返回的set集合，判断是否包含敏感词
        if(dirtyWords != null && dirtyWords.size() > 0){
            //5、 如果有敏感词，抛出异常 / 其他操作。。
            log.info("【策略模块-敏感词校验】   短信内容包含敏感词信息， dirtyWords = {}",dirtyWords);
            // 还需要做其他处理
        }
        /**
         * ### 基于Redis的交集操作实现
         *
         * 第一点：Redis完成交集操作，至少需要两个操作。
         *
         * - 需要将分词后的短信内容添加到Redis的Set结构中
         * - 将短信内容和敏感词的set进行sinter交集操作
         *
         * 但是，存储的短信内容，需要在执行完毕交集操作后，删除掉。
         *
         * 需要让缓存模块提供这种交集操作：增，交，删
         */
        /**
         * 第二点：策略模块需要将短信内容进行分词。选择IK分词器对短信内容进行分词，将分词好的短信内容存储到一个集合中，
         * 去调用上面提供的增，交，删操作，得到一个返回的set集合。基于返回的set集合判断当前短信内容是否包含敏感词。
         */
    }
}