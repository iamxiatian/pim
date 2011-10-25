package xiatian.pim.component.doc.style;

import java.awt.Color;
import java.util.Hashtable;

import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class PimStyledDocument extends DefaultStyledDocument {
  private static final long serialVersionUID = -7924681473277766181L;
  private DefaultStyledDocument doc;
  private Element rootElement;
  private boolean multiLineComment;
  private MutableAttributeSet normal;
  private MutableAttributeSet comment;
  private MutableAttributeSet urlStyle;
  private MutableAttributeSet quote;
  private Hashtable<String, MutableAttributeSet> keywords;
  private Hashtable<String, MutableAttributeSet> headers;

  public PimStyledDocument() {
    doc = this;
    rootElement = doc.getDefaultRootElement();
    putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");

    urlStyle = new SimpleAttributeSet();
    StyleConstants.setForeground(urlStyle, Color.BLUE);
    StyleConstants.setUnderline(urlStyle, true);
    StyleConstants.setItalic(urlStyle, true);

    normal = new SimpleAttributeSet();
    StyleConstants.setForeground(normal, Color.black);
    comment = new SimpleAttributeSet();
    Color green = new Color(0, 120, 0);

    StyleConstants.setForeground(comment, green);
    // StyleConstants.setItalic(comment, true);

    // StyleConstants.setBold(keyword, true);
    quote = new SimpleAttributeSet();
    Color red = new Color(140, 0, 0);

    StyleConstants.setForeground(quote, red);

    keywords = new Hashtable<String, MutableAttributeSet>();
    MutableAttributeSet keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, Color.BLUE);
    StyleConstants.setBold(keyword, true);
    keywords.put("TODO", keyword);

    keyword = new SimpleAttributeSet();
    StyleConstants.setBold(keyword, true);
    StyleConstants.setForeground(keyword, Color.RED);
    keywords.put("TASK", keyword);
    
    Color keywordColor = new Color(153, 0, 0);
    keyword = new SimpleAttributeSet();
    StyleConstants.setBold(keyword, true);
    StyleConstants.setForeground(keyword, keywordColor);
    keywords.put("public", keyword);
        
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("private", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    keywords.put("protected", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setBold(keyword, true);
    StyleConstants.setForeground(keyword, keywordColor);
    keywords.put("class", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("import", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("package", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setBold(keyword, true);
    StyleConstants.setForeground(keyword, keywordColor);
    keywords.put("extends", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("implements", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("boolean", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("int", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("long", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("double", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("float", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("static", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("final", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setBold(keyword, true);
    StyleConstants.setForeground(keyword, Color.MAGENTA);
    keywords.put("return", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, Color.DARK_GRAY);
    keywords.put("String", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, keywordColor);
    StyleConstants.setBold(keyword, true);
    keywords.put("function", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, Color.RED);
    keywords.put("$", keyword);
    
    keyword = new SimpleAttributeSet();
    StyleConstants.setForeground(keyword, Color.GRAY);
    keywords.put("//", keyword);
    
    headers = new Hashtable<String, MutableAttributeSet>();
    MutableAttributeSet c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, Color.BLUE);
    headers.put("BLUE", c);

    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, Color.RED);
    headers.put("RED", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, Color.BLACK);
    headers.put("BLACK", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, Color.WHITE);
    headers.put("WHITE", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, Color.YELLOW);
    headers.put("YELLOW", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, Color.GREEN);
    headers.put("GREEN", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, Color.PINK);
    headers.put("PINK", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, Color.ORANGE);
    headers.put("ORANGE", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, Color.GRAY);
    headers.put("GRAY", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, Color.MAGENTA);
    headers.put("MAGENTA", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setBold(c, true);
    headers.put("BOLD", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setItalic(c, true);
    headers.put("ITALIC", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, keywordColor);
    StyleConstants.setBold(c, true);
    headers.put("TITLE:", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, keywordColor);
    StyleConstants.setBold(c, true);
    headers.put("AUTHOR:", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, keywordColor);
    StyleConstants.setBold(c, true);
    headers.put("DATE:", c);
    
    c = new SimpleAttributeSet();
    StyleConstants.setForeground(c, keywordColor);
    StyleConstants.setBold(c, true);
    headers.put("_______________", c);
  }

  /*
   * Override to apply syntax highlighting after the document has been updated
   */
  public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
    if (str.equals("{"))
      str = addMatchingBrace(offset);
    super.insertString(offset, str, a);
    processChangedLines(offset, str.length());
  }

  /*
   * Override to apply syntax highlighting after the document has been updated
   */
  public void remove(int offset, int length) throws BadLocationException {
    super.remove(offset, length);
    processChangedLines(offset, 0);
  }

  /*
   * Determine how many lines have been changed, then apply highlighting to each
   * line
   */
  private void processChangedLines(int offset, int length) throws BadLocationException {
    String content = doc.getText(0, doc.getLength());
    // The lines affected by the latest document update
    int startLine = rootElement.getElementIndex(offset);
    int endLine = rootElement.getElementIndex(offset + length);
    // Make sure all comment lines prior to the start line are commented
    // and determine if the start line is still in a multi line comment
    setMultiLineComment(commentLinesBefore(content, startLine));
    // Do the actual highlighting
    for (int i = startLine; i <= endLine; i++)
      applyHighlighting(content, i);
    // Resolve highlighting to the next end multi line delimiter
    if (isMultiLineComment())
      commentLinesAfter(content, endLine);
    else
      highlightLinesAfter(content, endLine);
  }

  /*
   * Highlight lines when a multi line comment is still 'open' (ie. matching end
   * delimiter has not yet been encountered)
   */
  private boolean commentLinesBefore(String content, int line) {
    int offset = rootElement.getElement(line).getStartOffset();
    // Start of comment not found, nothing to do
    int startDelimiter = lastIndexOf(content, getStartDelimiter(), offset - 2);
    if (startDelimiter < 0)
      return false;
    // Matching start/end of comment found, nothing to do
    int endDelimiter = indexOf(content, getEndDelimiter(), startDelimiter);
    if (endDelimiter < offset & endDelimiter != -1)
      return false;
    // End of comment not found, highlight the lines
    doc.setCharacterAttributes(startDelimiter, offset - startDelimiter + 1, comment, false);
    return true;
  }

  /*
   * Highlight comment lines to matching end delimiter
   */
  private void commentLinesAfter(String content, int line) {
    int offset = rootElement.getElement(line).getEndOffset();
    // End of comment not found, nothing to do
    int endDelimiter = indexOf(content, getEndDelimiter(), offset);
    if (endDelimiter < 0)
      return;
    // Matching start/end of comment found, comment the lines
    int startDelimiter = lastIndexOf(content, getStartDelimiter(), endDelimiter);
    if (startDelimiter < 0 || startDelimiter <= offset) {
      doc.setCharacterAttributes(offset, endDelimiter - offset + 1, comment, false);
    }
  }

  /*
   * Highlight lines to start or end delimiter
   */
  private void highlightLinesAfter(String content, int line) throws BadLocationException {
    int offset = rootElement.getElement(line).getEndOffset();
    // Start/End delimiter not found, nothing to do
    int startDelimiter = indexOf(content, getStartDelimiter(), offset);
    int endDelimiter = indexOf(content, getEndDelimiter(), offset);
    if (startDelimiter < 0)
      startDelimiter = content.length();
    if (endDelimiter < 0)
      endDelimiter = content.length();
    int delimiter = Math.min(startDelimiter, endDelimiter);
    if (delimiter < offset)
      return;
    // Start/End delimiter found, reapply highlighting
    int endLine = rootElement.getElementIndex(delimiter);
    for (int i = line + 1; i < endLine; i++) {
      Element branch = rootElement.getElement(i);
      Element leaf = doc.getCharacterElement(branch.getStartOffset());
      AttributeSet as = leaf.getAttributes();
      if (as.isEqual(comment))
        applyHighlighting(content, i);
    }
  }

  /*
   * Parse the line to determine the appropriate highlighting
   */
  private void applyHighlighting(String content, int line) throws BadLocationException {
    int startOffset = rootElement.getElement(line).getStartOffset();
    int endOffset = rootElement.getElement(line).getEndOffset() - 1;
    int lineLength = endOffset - startOffset;
    int contentLength = content.length();
    if (endOffset >= contentLength)
      endOffset = contentLength - 1;
    // check for multi line comments
    // (always set the comment attribute for the entire line)
    if (endingMultiLineComment(content, startOffset, endOffset) || isMultiLineComment()
        || startingMultiLineComment(content, startOffset, endOffset)) {
      doc.setCharacterAttributes(startOffset, endOffset - startOffset + 1, comment, false);
      return;
    }
    // set normal attributes for the line
    doc.setCharacterAttributes(startOffset, lineLength, normal, true);
    // check for single line comment
    int index = content.indexOf(getSingleLineDelimiter(), startOffset);
    if ((index > -1) && (index < endOffset)) {
      doc.setCharacterAttributes(index, endOffset - index + 1, comment, false);
      endOffset = index - 1;
    }
    // check for tokens
    checkForTokens(content, startOffset, endOffset);
  }

  /*
   * Does this line contain the start delimiter
   */
  private boolean startingMultiLineComment(String content, int startOffset, int endOffset) throws BadLocationException {
    int index = indexOf(content, getStartDelimiter(), startOffset);
    if ((index < 0) || (index > endOffset))
      return false;
    else {
      setMultiLineComment(true);
      return true;
    }
  }

  /*
   * Does this line contain the end delimiter
   */
  private boolean endingMultiLineComment(String content, int startOffset, int endOffset) throws BadLocationException {
    int index = indexOf(content, getEndDelimiter(), startOffset);
    if ((index < 0) || (index > endOffset))
      return false;
    else {
      setMultiLineComment(false);
      return true;
    }
  }

  /*
   * We have found a start delimiter and are still searching for the end
   * delimiter
   */
  private boolean isMultiLineComment() {
    return multiLineComment;
  }

  private void setMultiLineComment(boolean value) {
    multiLineComment = value;
  }

  private void checkForTokens(String content, int startOffset, int endOffset) {
    //*
    if(startOffset>=endOffset) return;
    
    String line = content.substring(startOffset, endOffset).toUpperCase();  
    for(int startPos = 0; startPos<line.length(); startPos++){        
      for(String c: headers.keySet()){
        String header = c.toUpperCase();
        String tail = "[/" + c.toUpperCase() + "]";
        if(!c.endsWith(":")){
          header = "[" + c + "]";
        }
        if(!line.substring(startPos).startsWith(header)) continue;
                       
        SimpleAttributeSet tagAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(tagAttr, Color.GRAY);
        //先查找有无对应的结尾标记，如［red]，则对应为[/red]
        int endPos = line.indexOf(tail, startPos);
        if(endPos>0){
          doc.setCharacterAttributes(startOffset + startPos + header.length(), (endPos-startPos) -header.length(), headers.get(c), false);
          //标记本身的颜色设置成白色
          doc.setCharacterAttributes(startOffset + startPos, header.length(), tagAttr, false);
          doc.setCharacterAttributes(startOffset + endPos, tail.length(), tagAttr, false);
        }else{
          if(c.endsWith(":")){            
            doc.setCharacterAttributes(startOffset + startPos, line.length()-startPos, headers.get(c), false);        
          }else{           
            doc.setCharacterAttributes(startOffset + startPos + header.length(), line.length()-startPos-header.length(), headers.get(c), false);
            //标记本身的颜色设置成白色
            doc.setCharacterAttributes(startOffset + startPos, header.length(), tagAttr, false);
          }
        }          
      
      }
    }
    
    //关键词区分大小写
    String keyLine = content.substring(startOffset, endOffset);  
    for (String key : keywords.keySet()) {
      int pos = keyLine.indexOf(key);
      if (pos >= 0) {
        doc.setCharacterAttributes(startOffset + pos, key.length(), keywords.get(key), false);
      }
    }

    // check for url:
    int pos = line.indexOf("HTTP://");
    if (pos >= 0) {
      int pos2 = line.indexOf(" ", pos + 1);
      if (pos2 < 0) {
        pos2 = line.length();
      }
      doc.setCharacterAttributes(startOffset + pos, pos2 - pos + 1, urlStyle, false);      
    }
    
    //startOffset = endOffset + 1;
    //*/
    
    /*
    while (startOffset <= endOffset) {
      
      // skip the delimiters to find the start of a new token
      while (isDelimiter(content.substring(startOffset, startOffset + 1))) {
        if (startOffset < endOffset)
          startOffset++;
        else
          return;
      }
      // Extract and process the entire token
      if (isQuoteDelimiter(content.substring(startOffset, startOffset + 1)))
        startOffset = getQuoteToken(content, startOffset, endOffset);
      else
        startOffset = getOtherToken(content, startOffset, endOffset);
    }
    //*/
  }

  /*
  private int getOtherToken(String content, int startOffset, int endOffset) {       
    String line = content.substring(startOffset, endOffset).toLowerCase();    
    for(String c: colors.keySet()){
      if(line.startsWith(c.toLowerCase())){
        int pos = line.indexOf("\n");
        doc.setCharacterAttributes(startOffset, endOffset-startOffset, colors.get(c), false); 
        return endOffset;
      }
    }

    // check for url:
    int pos = line.indexOf("http://");
    if (pos >= 0) {
      int pos2 = line.indexOf(" ", pos + 1);
      if (pos2 < 0) {
        pos2 = line.length() - 1;
      }
      doc.setCharacterAttributes(startOffset + pos, pos2-pos, urlStyle, false);   
      return endOffset;
    }
    
    int endOfToken = startOffset + 1;
    while (endOfToken <= endOffset) {
      if (isDelimiter(content.substring(endOfToken, endOfToken + 1)))
        break;
      endOfToken++;
    }
    String token = content.substring(startOffset, endOfToken);
    if (isKeyword(token)){
      doc.setCharacterAttributes(startOffset, endOfToken - startOffset, keywords.get(token.toUpperCase()), false);
    }
    
    return endOfToken + 1;
  }

  /*
   * Parse the line to get the quotes and highlight it
   */
  int getQuoteToken(String content, int startOffset, int endOffset) {
    String quoteDelimiter = content.substring(startOffset, startOffset + 1);
    String escapeString = getEscapeString(quoteDelimiter);
    int index;
    int endOfQuote = startOffset;
    // skip over the escape quotes in this quote
    index = content.indexOf(escapeString, endOfQuote + 1);
    while ((index > -1) && (index < endOffset)) {
      endOfQuote = index + 1;
      index = content.indexOf(escapeString, endOfQuote);
    }
    // now find the matching delimiter
    index = content.indexOf(quoteDelimiter, endOfQuote + 1);
    if ((index < 0) || (index > endOffset))
      endOfQuote = endOffset;
    else
      endOfQuote = index;
    doc.setCharacterAttributes(startOffset, endOfQuote - startOffset + 1, quote, false);
    return endOfQuote + 1;
  }

  /*
   * This updates the colored text and prepares for undo event
   */
  protected void fireInsertUpdate(DocumentEvent evt) {

    super.fireInsertUpdate(evt);

    try {
      processChangedLines(evt.getOffset(), evt.getLength());
    } catch (BadLocationException ex) {
      System.out.println("" + ex);
    }
  }

  /*
   * This updates the colored text and does the undo operation
   */
  protected void fireRemoveUpdate(DocumentEvent evt) {

    super.fireRemoveUpdate(evt);

    try {
      processChangedLines(evt.getOffset(), evt.getLength());
    } catch (BadLocationException ex) {
      System.out.println("" + ex);
    }
  }

  /*
   * Assume the needle will the found at the start/end of the line
   */
  private int indexOf(String content, String needle, int offset) {
    int index;
    while ((index = content.indexOf(needle, offset)) != -1) {
      String text = getLine(content, index).trim();
      if (text.startsWith(needle) || text.endsWith(needle))
        break;
      else
        offset = index + 1;
    }
    return index;
  }

  /*
   * Assume the needle will the found at the start/end of the line
   */
  private int lastIndexOf(String content, String needle, int offset) {
    int index;
    while ((index = content.lastIndexOf(needle, offset)) != -1) {
      String text = getLine(content, index).trim();
      if (text.startsWith(needle) || text.endsWith(needle))
        break;
      else
        offset = index - 1;
    }
    return index;
  }

  private String getLine(String content, int offset) {
    int line = rootElement.getElementIndex(offset);
    Element lineElement = rootElement.getElement(line);
    int start = lineElement.getStartOffset();
    int end = lineElement.getEndOffset();
    return content.substring(start, end - 1);
  }

  /*
   * Override for other languages
   */
  protected boolean isDelimiter(String character) {
    String operands = ";:{}()[]+-/%<=>!&|^~*";
    if (Character.isWhitespace(character.charAt(0)) || operands.indexOf(character) != -1)
      return true;
    else
      return false;
  }

  /*
   * Override for other languages
   */
  protected boolean isQuoteDelimiter(String character) {
    String quoteDelimiters = "\"'";
    if (quoteDelimiters.indexOf(character) < 0)
      return false;
    else
      return true;
  }

  /*
   * Override for other languages
   */
  protected boolean isKeyword(String token) {
    Object o = keywords.get(token.toUpperCase());
    return o == null ? false : true;
  }

  /*
   * Override for other languages
   */
  protected String getStartDelimiter() {
    return "/*";
  }

  /*
   * Override for other languages
   */
  protected String getEndDelimiter() {
    return "*/";
  }

  /*
   * Override for other languages
   */
  protected String getSingleLineDelimiter() {
    return "--";
  }

  /*
   * Override for other languages
   */
  protected String getEscapeString(String quoteDelimiter) {
    return "\\" + quoteDelimiter;
  }

  /*
   * Overide bracket matching for other languages
   */
  protected String addMatchingBrace(int offset) throws BadLocationException {
    StringBuffer whiteSpace = new StringBuffer();
    int line = rootElement.getElementIndex(offset);
    int i = rootElement.getElement(line).getStartOffset();
    while (true) {
      String temp = doc.getText(i, 1);
      if (temp.equals(" ") || temp.equals("\t")) {
        whiteSpace.append(temp);
        i++;
      } else
        break;
    }
    return "{\n" + whiteSpace.toString() + whiteSpace.toString() + "\n" + whiteSpace.toString() + "}";
  }
}