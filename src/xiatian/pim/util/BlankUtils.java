package xiatian.pim.util;

/**
 * 判断对象是否为空
 *
 * @author <a href="mailto:iamxiatian@gmail.com">xiatian</a>
 */
public class BlankUtils {
    public static boolean isBlank(String s) {
        return s == null || s.trim().equals("");
    }
}
