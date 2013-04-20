package my.lib.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StringUtil {

    private StringUtil() {
    }

    /**
     * テキストをUTF-8でURLエンコーディングする。
     * @param text
     * @return
     */
    public static String encodeUrl(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UtilException("URLエンコーディングに失敗しました: テキスト=" + text, e);
        }
    }

    /**
     * 文字列前後の空白と、改行コードを削除する。
     * @param text
     * @return
     */
    public static String normalize(String text) {
        String normalizedText = text.trim().replaceAll("\n", "");

        return normalizedText;
    }
}
