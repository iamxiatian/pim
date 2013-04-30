package xiatian.pim.component.doc;

import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import xiatian.pim.domain.Journal;
import xiatian.pim.io.PimDb;
import xiatian.pim.listener.PimLog;

/**
 * 一篇文档的显示控制
 *
 * @author xiatian
 */
public class DocView {
    /**
     * 当前显示区显示的日记对象
     */
    private Journal journal = null;

    /**
     * 本身是EditPane或ViewPane的引用
     */
//    private JTextComponent currentEditor = null;

    /**
     * 用于编辑的组件
     */
    private EditPane editPane = new EditPane();

    /**
     * 用于显示的组件
     */
    private ViewPane viewPane = new ViewPane();

    private DocPanel docPanel = null;

    private int currentStatus = Journal.STATE_READ;

    public DocView() {
        docPanel = new DocPanel(this);
        editPane.setViewPane(viewPane);
    }

    private boolean isEditView() {
        return currentStatus == Journal.STATE_EDIT;
    }

    // public void saveProperties(){
    //
    // }

    /**
     * 只有在编辑视图下方可保存当前内容
     */
    public void save() {
        if (!isEditView())
            return;

        int pos = editPane.getCaretPosition();
        //currentEditor.setText(currentEditor.getText());
        if (journal != null && journal.isOpened()) {
            if (!journal.getContent().equals(editPane.getText())) {
                journal.setContent(editPane.getText());
                PimDb.getInstance().updateJournalContent(journal.getId(), journal.getContent());
            }
            PimLog.getInstance().showStatusMessage(journal.getTitle() + " has saved.");
        } else {
            PimLog.getInstance().showStatusMessage("WARNING: current note is not opened.");
        }
        editPane.setCaretPosition(pos);
    }

    /**
     * 保存原来已经打开的笔记，并且设置当前内容为新笔记
     *
     * @param newJournal
     */
    public void setJournal(Journal newJournal) {
//        // 根据需要保存的上一个Journal对象的内容
//        if (journal != null && journal.getId() != newJournal.getId() && !journal.getContent().equals(editPane.getText()) && journal.isOpened() && isEditView()) {
//            journal.setContent(editPane.getText());
//            PimDb.getInstance().updateJournalContent(journal.getId(), journal.getContent());
//        }

        this.journal = newJournal;
        if (journal != null) {
            String style = journal.getProperty(Journal.Property_Style);
            if (EditPane.STYLES.containsKey(style)) {
                editPane.setSyntaxEditingStyle(EditPane.STYLES.get(style));
            } else {
                editPane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
            }
            toState(journal.getState());
        }
    }

    public void toState(int state) {
        if (journal == null)
            return;
        journal.setState(state);
        PimDb.getInstance().updateJournalState(journal.getId(), state);
        this.currentStatus = state;

        // 根据状态切换不同的编辑器
        switch (state) {
            case Journal.STATE_EDIT:
                docPanel.setSelectedIndex(0);
                break;
            case Journal.STATE_READ:
                docPanel.setSelectedIndex(1);
                break;
        }

        if (journal.isOpened()) {
            editPane.setText(journal.getContent());
        } else {
            editPane.setText("当前记录已加密，尚未打开.");
        }
        editPane.setCaretPosition(0);
    }

    public void changeState() {

    }

    public EditPane getEditPane() {
        return editPane;
    }

    public ViewPane getViewPane() {
        return viewPane;
    }

    public Journal getCurrentJournal() {
        return this.journal;
    }

    public DocPanel getDocPanel() {
        return docPanel;
    }
}
