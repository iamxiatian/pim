package xiatian.pim.markdown;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Markdown Main Class
 * User: xiatian
 * Date: 4/30/13
 * Time: 2:02 PM
 */
public class Markdown {
    public static String parseMarkdownSource(String source) {
        StringWriter writer = new StringWriter();
        writer.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n" +
                "</head>\n" +
                "<body>\n");

        MarkdownProcessor processor = new MarkdownProcessor();
        String body = processor.markdown(source);
        writer.write(body);

        writer.write("\n</body>\n</html>");
        writer.flush();
        return writer.toString();
    }

}
