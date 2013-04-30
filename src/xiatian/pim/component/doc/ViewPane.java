package xiatian.pim.component.doc;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class ViewPane extends JEditorPane {
    private static final long serialVersionUID = 7675924741273914256L;
    private String srcText;

    public ViewPane() {
        super();
        this.setEditable(false);
        this.setContentType("text/html");

//        JScrollPane jScrollPane = new JScrollPane(jEditorPane);

        HTMLEditorKit kit = new HTMLEditorKit();
        Document doc = kit.createDefaultDocument();
        this.setDocument(doc);
        this.setEditorKit(kit);

        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {color:#000; font:11px Monaco; margin: 4px;}");
        styleSheet.addRule("h1 {color: blue;}");
        styleSheet.addRule("p {padding:2px;}");
        styleSheet.addRule("h2 {color: #ff0000;}");
    }

    @Override
    public void setText(String text) {
        this.srcText = text;
        int pos = text.toLowerCase().indexOf("<body>");
        if (pos < 0) {
            super.setText(text.replaceAll("\\n", "<br/>"));
        } else {
            super.setText(text);
        }
    }

    @Override
    public String getText() {
        return this.srcText;
    }
}
