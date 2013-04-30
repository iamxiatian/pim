package xiatian.pim.component.doc.example;

import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

public class CaretListenerLabel extends JLabel implements CaretListener {
    private static final long serialVersionUID = -3749260909312423096L;
    private static String newline = "\n";
    private JTextPane textPane = null;

    public CaretListenerLabel(String label) {
        super(label);
    }

    public void setJTextPane(JTextPane textPane) {
        this.textPane = textPane;
    }

    // Might not be invoked from the event dispatch thread.
    public void caretUpdate(CaretEvent e) {
        displaySelectionInfo(e.getDot(), e.getMark());
    }

    // This method can be invoked from any thread. It
    // invokes the setText and modelToView methods, which
    // must run on the event dispatch thread. We use
    // invokeLater to schedule the code for execution
    // on the event dispatch thread.
    protected void displaySelectionInfo(final int dot, final int mark) {

        if (textPane == null) return;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (dot == mark) { // no selection
                    try {
                        Rectangle caretCoords = textPane.modelToView(dot);
                        // Convert it to view coordinates.
                        setText("caret: text position: " + dot + ", view location = [" + caretCoords.x + ", " + caretCoords.y + "]"
                                + newline);
                    } catch (BadLocationException ble) {
                        setText("caret: text position: " + dot + newline);
                    }
                } else if (dot < mark) {
                    setText("selection from: " + dot + " to " + mark + newline);
                } else {
                    setText("selection from: " + mark + " to " + dot + newline);
                }
            }
        });
    }
}