package cn.kmbeast.utils;

import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本工具类方法
 */
public class TextUtils {

    /**
     * 判断指定ID是不是在字符串里面出现过
     *
     * @param text 源头字符串
     * @param id   指定ID
     * @return Boolean
     */
    public static Boolean exitId(String text, Integer id) {
        if (text == null) {
            return false;
        }
        return text.contains(String.valueOf(id));
    }

    /**
     * 在指定的字符串中，去除指定的ID
     *
     * @param text 源字符串
     * @param id   指定ID
     * @return String
     */
    public static String split(String text, Integer id) {
        String[] split = text.split(",");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            if (!s.equals(String.valueOf(id))) {
                list.add(s);
            }
        }
        if (list.isEmpty()){
            return null;
        }
        return Strings.join(list, ',');
    }

    /**
     * 向指定的字符串中，拼接指定的ID
     *
     * @param text 源字符串
     * @param id   指定ID
     * @return String
     */
    public static String join(String text, Integer id) {
//        String[] split = text.split(",");
//        List<String> strings = Arrays.asList(split);
//        strings.add(String.valueOf(id));
//        return Strings.join(strings, ',');
        if (text == null) {
            return String.valueOf(id);
        }
        return text + "," + id;
    }

    /**
     * 从富文本里面提取相关的文字
     *
     * @param text       富文本
     * @param limitCount 提取多少字
     * @return String
     */
    public static String extractText(String text, Integer limitCount) {
        if (text == null) {
            return null;
        }
        Document document = Jsoup.parse(text, "utf-8");
        String stringText = document.text();
        // 做截取
        if (stringText.length() < limitCount) {
            return stringText + "...";
        }
        return stringText.substring(0, limitCount) + "...";
    }

}
