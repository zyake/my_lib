package my.lib.net.mime;

/**
 * MIMEヘッダの一行分のパースに対応したパーサ。
 * 
 * パースは一行分のみであるため、
 * フォールディングには対応しない。
 * 
 * @author zyake
 *
 */
public interface MIMEHeaderParser {

	MIMEHeader parseHeader(String header);
}
