package com.dingding.ddframe.ddbase.utils;

/**
 * Created by zzz on 15/8/26.
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 敏感字符过滤
 *
 * @author huangzp
 * @date 2015-6-2
 */
public class SensitiveWord {

    // 以DFA算法模型存储敏感词库
    private static Map<String, Map<String, String>> sensitiveWordMap = null;

    private static final int MIN_MATCH_TYPE = 1; // 最小匹配规则
    private static final int MAX_MATCH_TYPE = 2; // 最大匹配规则

    // 当前使用的匹配规则
    private static final int CURRENT_MATCH_TYPE = 1;//Integer.valueOf(SpringContextListener.getContextProValue("sensitive.word.match.type", "1"));

    // 词组结尾标识 1-是，0-否
    private static final String ISEND_TRUE = "1";
    private static final String ISEND_FALSE = "0";

    static {
        try {
            // 将敏感词库加载为DFA模型
            addSensitiveWordToDFAModel();
            //LogUtil.log.info("###### 敏感词库加载完毕 #########");
        } catch (Exception e) {
            //LogUtil.log.error(e.getMessage(), e);
        }
    }

    /**
     * 以DFA算法模型存储敏感词库 中 = { isEnd = 0 国 = { isEnd = 1 人 = {isEnd = 0 民 = {isEnd
     * = 1} } 男 = { isEnd = 0 人 = { isEnd = 1 } } } } 五 = { isEnd = 0 星 = {
     * isEnd = 0 红 = { isEnd = 0 旗 = { isEnd = 1 } } } }
     *
     * @throws Exception
     */
    private static void addSensitiveWordToDFAModel() throws Exception {

        if (null == sensitiveWordMap || sensitiveWordMap.size() == 0) {

            // 读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();

            sensitiveWordMap = new HashMap<String, Map<String, String>>(keyWordSet.size()); // 初始化敏感词容器，减少扩容操作
            Map nowMap = null;
            Map<String, String> newWorMap = null;
            // 迭代keyWordSet
            Iterator<String> iterator = keyWordSet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next(); // 关键字
                nowMap = sensitiveWordMap;
                for (int i = 0; i < key.length(); i++) {
                    char keyChar = key.charAt(i); // 转换成char型
                    Object wordMap = nowMap.get(keyChar); // 获取

                    if (null != wordMap) { // 如果存在该key，直接赋值
                        nowMap = (Map) wordMap;
                    } else { // 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                        newWorMap = new HashMap<String, String>();
                        newWorMap.put("isEnd", ISEND_FALSE); // 不是最后一个
                        nowMap.put(keyChar, newWorMap);
                        nowMap = newWorMap;
                    }

                    if (i == key.length() - 1) {
                        nowMap.put("isEnd", ISEND_TRUE); // 最后一个
                    }
                }

            }
        }
    }

    /**
     * 读取敏感词库文件中的内容
     *
     * @throws Exception
     */
    private static Set<String> readSensitiveWordFile() throws Exception {

        Set<String> set = null;

        if (null == sensitiveWordMap || sensitiveWordMap.size() == 0) {
            InputStream is = SensitiveWord.class.getClassLoader().getResourceAsStream("sensitive_words.txt");
            if (null != is && is.available() > 0) {

                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    set = new HashSet<String>();
                    String txt = null;
                    while (null != (txt = br.readLine())) { // 读取文件，将文件内容放入到set中
                        set.add(txt);
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (null != br) {
                        br.close(); // 关闭文件流
                    }
                }

            } else {
                //LogUtil.log.error("敏感词库文件不存在");
            }

        }
        return set;
    }

    /**
     * 判断文字是否包含敏感字符
     */
    public static boolean isContaintSensitiveWord(String txt) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i); // 判断是否包含敏感字符
            if (matchFlag > 0) { // 大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt
     * @return
     */
    public static Set<String> getSensitiveWord(String txt) {
        Set<String> sensitiveWordList = new HashSet<String>();

        for (int i = 0; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i); // 判断是否包含敏感字符
            if (length > 0) { // 存在,加入list中
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1; // 减1的原因，是因为for会自增
            }
        }

        return sensitiveWordList;
    }

    /**
     * 替换敏感字字符，默认为 "*"
     */
    public static String replaceSensitiveWord(String txt) {
        return replaceSensitiveWord(txt, "*");
    }

    /**
     * 替换敏感字字符
     */
    public static String replaceSensitiveWord(String txt, String replaceChar) {
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt); // 获取所有的敏感词
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replace(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 获取替换字符串
     */
    private static String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * 检查文字中是否包含敏感字符
     *
     * @return，如果存在，则返回敏感词字符的长度，不存在返回0
     */
    public static int checkSensitiveWord(String txt, int beginIndex) {
        boolean flag = false; // 敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0; // 匹配标识数默认为0
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word); // 获取指定key
            if (null != nowMap) { // 存在，则判断是否为最后一个
                matchFlag++; // 找到相应key，匹配标识+1
                if (ISEND_TRUE.equals(nowMap.get("isEnd"))) { // 如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true; // 结束标志位为true
                    if (MIN_MATCH_TYPE == CURRENT_MATCH_TYPE) { // 最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else { // 不存在，直接返回
                break;
            }
        }
        if (matchFlag < 2 || !flag) { // 长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }


}