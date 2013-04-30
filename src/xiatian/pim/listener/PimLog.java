package xiatian.pim.listener;

import javax.swing.JLabel;

import xiatian.pim.component.bar.JStatusBar;
import xiatian.pim.component.bar.MyToolBar;

public class PimLog {
    private static PimLog instance = new PimLog();

    private JStatusBar statusBar;
    private MyToolBar toolBar;
    private JLabel statusMessageLabel;

    public static PimLog getInstance() {
        return instance;
    }

    private PimLog() {
    }

    public void setStatusBar(JStatusBar statusBar) {
        this.statusBar = statusBar;
    }

    public void setStatusMessageLabel(JLabel statusMessageLabel) {
        this.statusMessageLabel = statusMessageLabel;
    }

    public void showStatusMessage(String msg) {
        statusMessageLabel.setText(msg);
    }

    public void showStatusBar(boolean show) {
        statusBar.setVisible(show);
    }

    public boolean isShowStatusBar() {
        return statusBar.isVisible();
    }

    public void setToolBar(MyToolBar bar) {
        this.toolBar = bar;
    }

    public void showToolBar(boolean show) {
        this.toolBar.setVisible(show);
    }
}
