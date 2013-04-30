package xiatian.pim.listener;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;

import xiatian.pim.component.doc.DocView;
import xiatian.pim.component.tree.ContentTree;
import xiatian.pim.io.PimDb;

public abstract class BaseController implements Controller {
    protected DocView docView;
    protected ContentTree tree;
    protected JFrame frame;
    protected PimDb pimDb;

    protected void setDocView(DocView docView) {
        this.docView = docView;
    }

    protected void setJFrame(JFrame frame) {
        this.frame = frame;
    }

    protected void setContentTree(ContentTree tree) {
        this.tree = tree;
    }

    public JFrame getJFrame() {
        return this.frame;
    }

    public DocView getDocView() {
        return docView;
    }

    public ContentTree getTree() {
        return tree;
    }

    public PimDb getPimDb() {
        return pimDb;
    }

    protected void setPimDb(PimDb db) {
        this.pimDb = db;
    }

    public void setBackground(Color color) {
        if (color != null) {
            docView.getEditPane().setBackground(color);
//      MutableAttributeSet attr = new SimpleAttributeSet();      
//      StyleConstants.setBackground(attr, color);
//      docText.setCharacterAttributes(attr, false); 
        }
    }

    public Color getBackground() {
        return docView.getEditPane().getBackground();
    }

    public void setFont(Font font) {
        if (font != null) {
//      MutableAttributeSet attr = new SimpleAttributeSet();      
//      StyleConstants.setFontFamily(attr, font.getFamily());
//      StyleConstants.setFontSize(attr, font.getSize());
//      
//      docText.setCharacterAttributes(attr, false); 
            docView.getEditPane().setFont(font);
        }

    }

    public Font getFont() {
        return docView.getEditPane().getFont();
    }

    public void setForeground(Color color) {
        if (color != null) {
            docView.getEditPane().setForeground(color);
//      MutableAttributeSet attr = new SimpleAttributeSet();      
//      StyleConstants.setForeground(attr, color);
//      docText.setCharacterAttributes(attr, false); 
        }
    }

    public Color getForeground() {
        return docView.getEditPane().getForeground();
    }

}
