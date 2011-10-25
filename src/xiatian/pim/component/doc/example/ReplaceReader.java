package xiatian.pim.component.doc.example;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import xiatian.pim.io.StringInputStream;

public class ReplaceReader {
  public static void main(String[] args) {
 
    JFrame f = new JFrame("JEditorPane with Custom Reader");
    JEditorPane ep = new JEditorPane();
    ep.setText("<font color='red'>OK</font>");
    f.getContentPane().add(new JScrollPane(ep));
    f.setSize(400, 300);
    f.setVisible(true);

    HTMLEditorKit kit = new HTMLEditorKit() {
      public Document createDefaultDocument() {
        HTMLDocument doc = new CustomHTMLDocument(getStyleSheet());
        doc.setAsynchronousLoadPriority(4);
        doc.setTokenThreshold(100);
        return doc;
      }
    };
    ep.setEditorKit(kit);
    
    try {
      Document doc = ep.getDocument();
      doc.putProperty("IgnoreCharsetDirective", new Boolean(true));
      kit.read(new StringInputStream("<font color='red'>红色</font>"), doc, 0);
      //kit.read(new FileReader(args[0]), doc, 0);
    } catch (Exception e) {
      System.out.println("Exception while reading HTML " + e);
    }
  }
}

class CustomHTMLDocument extends HTMLDocument {
  CustomHTMLDocument(StyleSheet styles) {
    super(styles);
  }

  public HTMLEditorKit.ParserCallback getReader(int pos) {
    return new CustomReader(pos);
  }
          
  class CustomReader extends HTMLDocument.HTMLReader {
    public CustomReader(int pos) {
      super(pos);
    }

    public void flush() throws BadLocationException {
      System.out.println("flush called");
      super.flush();
    }

    public void handleText(char[] data, int pos) {
      indent();
      System.out.println("handleText <" + new String(data) + ">, pos " + pos);
      super.handleText(data, pos);
    }

    public void handleComment(char[] data, int pos) {
      indent();
      System.out.println("handleComment <" + new String(data) + ">, pos " + pos);
      super.handleComment(data, pos);
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
      indent();
      System.out.println("handleStartTag <" + t + ">, pos " + pos);
      indent();
      System.out.println("Attributes: " + a);
      tagLevel++;
      super.handleStartTag(t, a, pos);
    }

    public void handleEndTag(HTML.Tag t, int pos) {
      tagLevel--;
      indent();
      System.out.println("handleEndTag <" + t + ">, pos " + pos);
      super.handleEndTag(t, pos);
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
      indent();
      System.out.println("handleSimpleTag <" + t + ">, pos " + pos);
      indent();
      System.out.println("Attributes: " + a);
      super.handleSimpleTag(t, a, pos);
    }

    public void handleError(String errorMsg, int pos){
      indent();
      System.out.println("handleError <" + errorMsg + ">, pos " + pos);
      super.handleError(errorMsg, pos);
    }

    protected void indent() {
      for (int i = 0; i < tagLevel; i++) {
        System.out.print(" ");
      }
    }

    int tagLevel;
  }
}
