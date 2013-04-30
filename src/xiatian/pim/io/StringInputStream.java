package xiatian.pim.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * minimal InputStream subclass to fetch bytes form a String
 *
 * @author fredt@users
 * @version 1.7.0
 */
public class StringInputStream extends InputStream {

    protected int strOffset = 0;
    protected int byteOffset = 0;
    protected int available;
    protected byte[] content;

    public StringInputStream(String s) {
        content = s.getBytes();
        available = content.length;
    }

    public int read() throws java.io.IOException {

        if (available == 0) {
            return -1;
        }

        available--;

        return content[byteOffset++];
    }

    public int available() throws IOException {
        return available;
    }
}
