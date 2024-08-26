package com.mashibing.strategy.util;

import cn.hutool.dfa.WordTree;
import com.hmm.common.constant.CacheConstant;
import com.mashibing.strategy.client.BeaconCacheClient;

import java.util.List;
import java.util.Set;

/**
 * Hutool是国内现在很流行的一个工具类，类似Commons-lang3这种大型的工具了，内部封装了很多常用的功能，直接导入依赖使用即可~
 */
public class HutoolDFAUtil {

    private static WordTree wordTree = new WordTree();

    /**
     * 初始化敏感词树
     */
    static {
        // 获取Spring容器中的cacheClient
        BeaconCacheClient cacheClient = (BeaconCacheClient) SpringUtil.getBeanByClass(BeaconCacheClient.class);
        // 获取存储在Redis中的全部敏感词
        Set<String> dirtyWords = cacheClient.smember(CacheConstant.DIRTY_WORD);
        // 调用WordTree的add方法，将dfaMap的敏感词树构建
        wordTree.addWords(dirtyWords);
    }


    public static List<String> getDirtyWord(String text){
        return wordTree.matchAll(text);
    }

}