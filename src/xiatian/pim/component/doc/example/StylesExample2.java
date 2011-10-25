package xiatian.pim.component.doc.example;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class StylesExample2 {
  public static void main(String[] args) {
    try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception evt) {}
  
    JFrame f = new JFrame("Styles Example 2");
    
    // Create the StyleContext, the document and the pane
    StyleContext sc = new StyleContext();
    final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
    JTextPane pane = new JTextPane(doc);
    
    // Create and add the main document style
    Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
    final Style mainStyle = sc.addStyle("MainStyle", defaultStyle);
    StyleConstants.setLeftIndent(mainStyle, 16);
    StyleConstants.setRightIndent(mainStyle, 16);
    StyleConstants.setFirstLineIndent(mainStyle, 16);
    StyleConstants.setFontFamily(mainStyle, "serif");
    StyleConstants.setFontSize(mainStyle, 12);

    // Create and add the constant width style
    final Style cwStyle = sc.addStyle("ConstantWidth", null);
    StyleConstants.setFontFamily(cwStyle, "monospaced");
    StyleConstants.setForeground(cwStyle, Color.green);

    // Create and add the heading style
    final Style heading2Style = sc.addStyle("Heading2", null);
    StyleConstants.setForeground(heading2Style, Color.red);
    StyleConstants.setFontSize(heading2Style, 16);
    StyleConstants.setFontFamily(heading2Style, "serif");
    StyleConstants.setBold(heading2Style, true);
    StyleConstants.setLeftIndent(heading2Style, 8);
    StyleConstants.setFirstLineIndent(heading2Style, 0);

    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          try {
            // Set the logical style
            doc.setLogicalStyle(0, mainStyle);

            // Add the text to the document
            doc.insertString(0, text, null);

            // Apply the character attributes
            doc.setCharacterAttributes(49, 13, cwStyle, false);
            doc.setCharacterAttributes(223, 14, cwStyle, false);
            doc.setCharacterAttributes(249, 14, cwStyle, false);
            doc.setCharacterAttributes(286, 8, cwStyle, false);
            doc.setCharacterAttributes(475, 14, cwStyle, false);
            doc.setCharacterAttributes(497, 21, cwStyle, false);
            doc.setCharacterAttributes(557, 9, cwStyle, false);
            doc.setCharacterAttributes(639, 12, cwStyle, false);
            doc.setCharacterAttributes(733, 21, cwStyle, false);
            doc.setCharacterAttributes(759, 9, cwStyle, false);

            // Finally, apply the style to the heading
            doc.setParagraphAttributes(0, 1, heading2Style, false);
          } catch (BadLocationException e) {
          }
        }
      });
    } catch (Exception e) {
      System.out.println("Exception when constructing document: " + e);
      System.exit(1);
    }

    f.getContentPane().add(new JScrollPane(pane));
    f.setSize(400, 300);
    f.setVisible(true);
  }

  public static final String text = 
          "Attributes, Styles and Style Contexts\n" +
          "The simple PlainDocument class that you saw in the previous " + 
          "chapter is only capable of holding text. The more complex text " +
          "components use a more sophisticated model that implements the " +
          "StyledDocument interface. StyledDocument is a sub-interface of " +
          "Document that contains methods for manipulating attributes that " +
          "control the way in which the text in the document is displayed. " +
          "The Swing text package contains a concrete implementation of " +
          "StyledDocument called DefaultStyledDocument that is used as the " +
          "default model for JTextPane and is also the base class from which " +
          "more specific models, such as the HTMLDocument class that handles " +
          "input in HTML format, can be created. In order to make use of " +
          "DefaultStyledDocument and JTextPane, you need to understand how " +
          "Swing represents and uses attributes.\n";

}