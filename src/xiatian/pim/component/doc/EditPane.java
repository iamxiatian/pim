package xiatian.pim.component.doc;

import java.awt.Color;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JMenu;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import xiatian.pim.component.border.LineNumberedBorder;
import xiatian.pim.component.doc.highlighter.UnderlineHighlighter;
import xiatian.pim.component.doc.highlighter.WordSearcher;
import xiatian.pim.component.doc.highlighter.WordSearcher.Word;
import xiatian.pim.component.doc.style.PimStyledDocument;
import xiatian.pim.conf.PimConf;
import xiatian.pim.domain.Journal;
import xiatian.pim.io.PimDb;
import xiatian.pim.listener.PimLog;

/**
 * 编辑视图
 * 
 * @author xiatian
 * 
 */
public class EditPane extends RSyntaxTextArea {

    private static final long serialVersionUID = 7945241122831275570L;

    HashMap<Object, Action> actions;

    public static Map<String, String> STYLES = new HashMap<String, String>();
    static {
        STYLES.put("NONE", SyntaxConstants.SYNTAX_STYLE_NONE);
        STYLES.put("XML", SyntaxConstants.SYNTAX_STYLE_XML);
        STYLES.put("JAVA", SyntaxConstants.SYNTAX_STYLE_JAVA);
        STYLES.put("HTML", SyntaxConstants.SYNTAX_STYLE_HTML);
        STYLES.put("CSS", SyntaxConstants.SYNTAX_STYLE_CSS);
        STYLES.put("C", SyntaxConstants.SYNTAX_STYLE_C);
        STYLES.put("C++", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
    }

    // undo helpers
    protected UndoAction undoAction;
    protected RedoAction redoAction;
    protected UndoManager undoManager = new UndoManager();

    private Journal journal = null;

    private JMenu editMenu = null;
    private JMenu styleMenu = null;

    private Border border = null;

    public static Highlighter highlighter = new UnderlineHighlighter(null);

    public EditPane() {
//        this.setStyledDocument(new PimStyledDocument());
        this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        this.setLineWrap(true);

        // 增加LineNumber
        LineNumberedBorder lnb = new LineNumberedBorder(LineNumberedBorder.LEFT_SIDE, LineNumberedBorder.RIGHT_JUSTIFY);
        this.border = javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(0, 5, 2, 2),
                javax.swing.BorderFactory.createCompoundBorder(lnb, javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1), javax.swing.BorderFactory.createEmptyBorder(0, 5, 5, 5))));

        if (PimConf.getInstance().getBoolean("show.linenumber", true)) {
            this.setBorder(border);
        }
        // 编辑器的增强处理
        this.actions = createActionTable();
        addBindings();
        // this.getStyledDocument().addUndoableEditListener(new
        // MyUndoableEditListener());
        editMenu = createEditMenu();
        styleMenu = createStyleMenu();

        // 高亮设置
        // this.setHighlighter(highlighter);
        final WordSearcher searcher = new WordSearcher(this);
        final List<Word> words = new ArrayList<Word>();
        words.add(new Word("TODO", Color.GREEN));
        words.add(new Word("TASK", Color.RED));
        this.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent evt) {
                searcher.search(words);
            }

            public void removeUpdate(DocumentEvent evt) {
                searcher.search(words);
            }

            public void changedUpdate(DocumentEvent evt) {
                displayEditInfo(evt);
            }

            private void displayEditInfo(DocumentEvent e) {
                Document document = e.getDocument();
                int changeLength = e.getLength();
                PimLog.getInstance().showStatusMessage(
                        e.getType().toString() + ": " + changeLength + " character" + ((changeLength == 1) ? ". " : "s. ") + " Text length = "
                                + document.getLength() + ".");
            }
        });
    }

    public void showLineNumber(boolean show) {
        PimConf.getInstance().setBoolean("show.linenumber", show);
        if (show) {
            this.setBorder(border);
        } else {
            this.setBorder(null);
        }
    }

    public void save() {
        int pos = this.getCaretPosition();
        this.setText(this.getText().replaceAll("\t", "    "));
        if (journal != null && journal.isOpened()) {
            if (!journal.getContent().equals(this.getText())) {
                journal.setContent(this.getText());
                PimDb.getInstance().updateJournalContent(journal.getId(), journal.getContent());
            }
            PimLog.getInstance().showStatusMessage(journal.getTitle() + " has saved.");
        } else {
            PimLog.getInstance().showStatusMessage("WARNING: current note is not opened.");
        }
        this.setCaretPosition(pos);
    }

    /**
     * 保存原来已经打开的笔记，并且设置当前内容为新笔记
     * 
     * @param newJournal
     */
    public void setJournal(Journal newJournal) {
        if (journal != null && journal.getId() != newJournal.getId() && !journal.getContent().equals(this.getText()) && journal.isOpened()) {
            journal.setContent(this.getText().replaceAll("\t", "    "));
            PimDb.getInstance().updateJournalContent(journal.getId(), journal.getContent());
        }
        this.journal = newJournal;
        if (journal.isOpened()) {
            super.setText(newJournal.getContent());
        } else {
            super.setText("当前记录已加密，尚未打开.");
        }
        this.setCaretPosition(0);

    }

    public Journal getCurrentJournal() {
        return this.journal;
    }

    // //////////////////////////////////////////////
    // 以下是编辑器的增强处理
    // ///////////////////////////////////////////////

    public JMenu getEditMenu() {
        return this.editMenu;
    }

    public JMenu getStyleMenu() {
        return this.styleMenu;
    }

    private HashMap<Object, Action> createActionTable() {
        HashMap<Object, Action> actions = new HashMap<Object, Action>();
        Action[] actionsArray = this.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
        return actions;
    }

    protected void addBindings() {
        InputMap inputMap = this.getInputMap();

        // Ctrl-b to go backward one character
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.backwardAction);

        // Ctrl-f to go forward one character
        key = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.forwardAction);

        // Ctrl-p to go up one line
        key = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.upAction);

        // Ctrl-n to go down one line
        key = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.downAction);
    }

    private Action getActionByName(String name) {
        return actions.get(name);
    }

    // Create the edit menu.
    protected JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");

        // Undo and redo are actions of our own creation.
        undoAction = new UndoAction();
        menu.add(undoAction);

        redoAction = new RedoAction();
        menu.add(redoAction);

        menu.addSeparator();

        // These actions come from the default editor kit.
        // Get the ones we want and stick them in the menu.
        menu.add(getActionByName(DefaultEditorKit.cutAction));
        menu.add(getActionByName(DefaultEditorKit.copyAction));
        menu.add(getActionByName(DefaultEditorKit.pasteAction));

        menu.addSeparator();

        menu.add(getActionByName(DefaultEditorKit.selectAllAction));
        return menu;
    }

    // Create the style menu.
    protected JMenu createStyleMenu() {
        JMenu menu = new JMenu("Style");

        Action action = new StyledEditorKit.BoldAction();
        action.putValue(Action.NAME, "Bold");
        menu.add(action);

        action = new StyledEditorKit.ItalicAction();
        action.putValue(Action.NAME, "Italic");
        menu.add(action);

        action = new StyledEditorKit.UnderlineAction();
        action.putValue(Action.NAME, "Underline");
        menu.add(action);

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontSizeAction("12", 12));
        menu.add(new StyledEditorKit.FontSizeAction("14", 14));
        menu.add(new StyledEditorKit.FontSizeAction("18", 18));

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontFamilyAction("Serif", "Serif"));
        menu.add(new StyledEditorKit.FontFamilyAction("SansSerif", "SansSerif"));

        menu.addSeparator();

        menu.add(new StyledEditorKit.ForegroundAction("Red", Color.red));
        menu.add(new StyledEditorKit.ForegroundAction("Green", Color.green));
        menu.add(new StyledEditorKit.ForegroundAction("Blue", Color.blue));
        menu.add(new StyledEditorKit.ForegroundAction("Black", Color.black));

        return menu;
    }

    class UndoAction extends AbstractAction {
        private static final long serialVersionUID = -8806625699705941778L;

        public UndoAction() {
            super("Undo");
            setEnabled(false);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Z"));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            if (undoManager.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undoManager.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction {
        private static final long serialVersionUID = 842722452817352115L;

        public RedoAction() {
            super("Redo");
            setEnabled(false);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Y"));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (undoManager.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undoManager.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

    protected class MyUndoableEditListener implements UndoableEditListener {
        public void undoableEditHappened(UndoableEditEvent e) {
            // Remember the edit and update the menus.
            undoManager.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }
}
