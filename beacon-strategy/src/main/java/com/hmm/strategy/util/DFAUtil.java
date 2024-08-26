package com.mashibing.strategy.util;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 17:44
 */

import com.hmm.common.constant.CacheConstant;
import com.mashibing.strategy.client.BeaconCacheClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 17:44
 */

public class DFAUtil {
    // 敏感词树
    private static Map dfaMap = new HashMap<>();

    private static final String IS_END = "isEnd";
    /**
     * 初始化敏感词树
     */
    static {
        // 获取Spring容器中的cacheClient
        BeaconCacheClient cacheClient = (BeaconCacheClient) SpringUtil.getBeanByClass(BeaconCacheClient.class);
        // 获取存储在Redis中的全部敏感词
        Set<String> dirtyWords = cacheClient.smember(CacheConstant.DIRTY_WORD);
        // 调用create，将dfaMap的敏感词树构建
        create(dirtyWords);
    }

    public static void main(String[] args) {
        // 敏感词库
        Set<String> dirtyWords = new HashSet<>();
        dirtyWords.add("三胖");
        dirtyWords.add("山炮");
        dirtyWords.add("三胖啊啊");
        dirtyWords.add("三胖啊啊");
        // 测试敏感词树的生成
        create(dirtyWords);
        // 输出结果
        for (Object o : dfaMap.entrySet()) {
            System.out.println(o);
        }
    }


    public static void create(Set<String> dirtyWords){
        //1、 声明一个Map作为临时存储
        Map nowMap;

        //2、遍历敏感词库
        for (String dirtyWord : dirtyWords) {
            nowMap = dfaMap;
            // 每个词，依次获取
            for (int i = 0; i < dirtyWord.length(); i++) {
                // 获取敏感词的每个字
                String word = String.valueOf(dirtyWord.charAt(i));
                // 判断当前的敏感词树中是否包含当前的字
                Map map = (Map) nowMap.get(word);
                if(map == null){
                    // 当前敏感词树中没有这个字
                    map = new HashMap();
                    // 将当前的敏感词存入
                    nowMap.put(word,map);
                }
                // 操作当前key对应的value的map
                nowMap = map;
                // 如果当前的字，已经有IS_END了，并且值为1，直接不管，
                if(nowMap.containsKey(IS_END) && nowMap.get(IS_END).equals("1")){
                    continue;
                }
                // 如果当前的isEnd没有，或者是0，需要考虑需要改为1
                if(i == dirtyWord.length() - 1){
                    // 到最后一个字了。
                    nowMap.put(IS_END,"1");
                }else{
                    // isEnd之前就是0，或者压根就没有isEnd
                    nowMap.putIfAbsent(IS_END,"0");
                }
            }
        }
    }
    /**
     * 基于敏感词树，对文字进行敏感词获取
     * @param text
     * @return
     */
    public static Set<String> getDirtyWord(String text) {
        // 1、作为返回结果存储敏感词的位置
        Set<String> dirtyWords = new HashSet<>();
        // 2、循环遍历文本内容
        for (int i = 0; i < text.length(); i++) {
            // 临时存储索引位置的变量
            int nextLength = 0;
            int dirtyLength = 0;
            // 获取最外层key的map
            Map nowMap = dfaMap;
            // 外层是索引向后动，匹配最外层的key
            // 内层是在匹配上一个后，继续向内部匹配内部的key
            for (int j = i; j < text.length(); j++) {
                // 获取当前索引位置的字
                String word = String.valueOf(text.charAt(j));
                // 先匹配最外层的key
                nowMap = (Map) nowMap.get(word);
                // 判断
                if (nowMap == null) {
                    // 没有这个字开头的敏感词
                    break;
                } else {
                    // 敏感词长度，从i开始算，现在的是dirtyLength
                    dirtyLength++;
                    // 出口即是，当前的map的isEnd是1，代表结束了。已经找到完整的敏感词
                    if ("1".equals(nowMap.get(IS_END))) {
                        // 代表敏感词匹配到一个完整的
                        nextLength = dirtyLength;
                        break;
                    }
                }

            }
            // 判断是否匹配上了敏感词
            if (nextLength > 0) {
                // 匹配上了，添加敏感词到set，同时移动外层索引
                dirtyWords.add(text.substring(i, i + nextLength));
                // -1的原因是，外层for循环，会对i进行++
                i = i + nextLength - 1;
            }
        }
        // 返回
        return dirtyWords;
    }

}
