/*
 * 07/29/2009
 *
 * TipWindow.java - The actual window component representing the tool tip.
 * Copyright (C) 2009 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.rsyntaxtextarea.focusabletip;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;


/**
 * The actual tool tip component.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class TipWindow extends JWindow implements ActionListener {

    private FocusableTip ft;
    private JEditorPane textArea;
    private String text;
    private TipListener tipListener;
    private HyperlinkListener userHyperlinkListener;

    private static TipWindow visibleInstance;


    /**
     * Constructor.
     *
     * @param owner The parent window.
     * @param msg   The text of the tool tip.  This can be HTML.
     */
    public TipWindow(Window owner, FocusableTip ft, String msg) {

        super(owner);
        this.ft = ft;
        this.text = msg;
        tipListener = new TipListener();

        JPanel cp = new JPanel(new BorderLayout());
        cp.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createLineBorder(Color.BLACK), BorderFactory
                .createEmptyBorder()));
        cp.setBackground(TipUtil.getToolTipBackground());
        textArea = new JEditorPane("text/html", msg);
        TipUtil.tweakTipEditorPane(textArea);
        if (ft.getImageBase() != null) { // Base URL for images
            ((HTMLDocument) textArea.getDocument()).setBase(ft.getImageBase());
        }
        textArea.addMouseListener(tipListener);
        textArea.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    TipWindow.this.ft.possiblyDisposeOfTipWindow();
                }
            }
        });
        cp.add(textArea);

        setFocusableWindowState(false);
        setContentPane(cp);
        setBottomPanel(); // Must do after setContentPane()
        pack();

        // InputMap/ActionMap combo doesn't work for JWindows (even when
        // using the JWindow's JRootPane), so we'll resort to KeyListener
        KeyAdapter ka = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    TipWindow.this.ft.possiblyDisposeOfTipWindow();
                }
            }
        };
        addKeyListener(ka);
        textArea.addKeyListener(ka);

        // Ensure only 1 TipWindow is ever visible.  If the caller does what
        // they're supposed to and only creates these on the EDT, the
        // synchronization isn't necessary, but we'll be extra safe.
        synchronized (TipWindow.class) {
            if (visibleInstance != null) {
                visibleInstance.dispose();
            }
            visibleInstance = this;
        }

    }


    public void actionPerformed(ActionEvent e) {

        if (!getFocusableWindowState()) {
            setFocusableWindowState(true);
            setBottomPanel();
            textArea.removeMouseListener(tipListener);
            pack();
            addWindowFocusListener(new WindowAdapter() {
                public void windowLostFocus(WindowEvent e) {
                    ft.possiblyDisposeOfTipWindow();
                }
            });
            ft.removeListeners();
            if (e == null) { // Didn't get here via our mouseover timer
                requestFocus();
            }
        }

    }


    /**
     * Disposes of this window.
     */
    public void dispose() {
        //System.out.println("[DEBUG]: Disposing...");
        Container cp = getContentPane();
        for (int i = 0; i < cp.getComponentCount(); i++) {
            // Okay if listener is already removed
            cp.getComponent(i).removeMouseListener(tipListener);
        }
        ft.removeListeners();
        super.dispose();
    }


    /**
     * Workaround for JEditorPane not returning its proper preferred size
     * when rendering HTML until after layout already done.  See
     * http://forums.sun.com/thread.jspa?forumID=57&threadID=574810 for a
     * discussion.
     */
    void fixSize() {

        Dimension d = textArea.getPreferredSize();
        Rectangle r = null;
        try {

            r = textArea.modelToView(textArea.getDocument().getLength() - 1);
            d.height = r.y + r.height;

            // Ensure the text area doesn't start out too tall or wide.
            d = textArea.getPreferredSize();
            d.width = Math.min(d.width + 25, 320);
            d.height = Math.min(d.height, 150);

            textArea.setPreferredSize(d);

        } catch (BadLocationException ble) { // Never happens
            ble.printStackTrace();
        }

        pack(); // Must re-pack to calculate proper size.

    }


    public String getText() {
        return text;
    }


    private void setBottomPanel() {

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JSeparator(), BorderLayout.NORTH);

        boolean focusable = getFocusableWindowState();
        if (focusable) {
            SizeGrip sg = new SizeGrip();
            sg.applyComponentOrientation(sg.getComponentOrientation()); // Workaround
            panel.add(sg, BorderLayout.LINE_END);
            MouseInputAdapter adapter = new MouseInputAdapter() {
                private Point lastPoint;

                public void mouseDragged(MouseEvent e) {
                    Point p = e.getPoint();
                    SwingUtilities.convertPointToScreen(p, panel);
                    if (lastPoint == null) {
                        lastPoint = p;
                    } else {
                        int dx = p.x - lastPoint.x;
                        int dy = p.y - lastPoint.y;
                        setLocation(getX() + dx, getY() + dy);
                        lastPoint = p;
                    }
                }

                public void mousePressed(MouseEvent e) {
                    lastPoint = e.getPoint();
                    SwingUtilities.convertPointToScreen(lastPoint, panel);
                }
            };
            panel.addMouseListener(adapter);
            panel.addMouseMotionListener(adapter);
            // Don't add tipListener to the panel or SizeGrip
        } else {
            panel.setOpaque(false);
            JLabel label = new JLabel(FocusableTip.getString("FocusHotkey"));
            Color fg = UIManager.getColor("Label.disabledForeground");
            Font font = textArea.getFont();
            font = font.deriveFont(font.getSize2D() - 1.0f);
            label.setFont(font);
            if (fg == null) { // Non BasicLookAndFeel-derived Looks
                fg = Color.GRAY;
            }
            label.setOpaque(true);
            Color bg = TipUtil.getToolTipBackground();
            label.setBackground(bg);
            label.setForeground(fg);
            label.setHorizontalAlignment(SwingConstants.TRAILING);
            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            panel.add(label);
            panel.addMouseListener(tipListener);
        }

        // Replace the previous SOUTH Component with the new one.
        Container cp = getContentPane();
        if (cp.getComponentCount() == 2) { // Skip first time through
            Component comp = cp.getComponent(0);
            cp.remove(0);
            JScrollPane sp = new JScrollPane(comp);
            sp.setViewportBorder(BorderFactory.createEmptyBorder());
            sp.setBackground(textArea.getBackground());
            sp.getViewport().setBackground(textArea.getBackground());
            cp.add(sp);
            // What was component 1 is now 0.
            cp.getComponent(0).removeMouseListener(tipListener);
            cp.remove(0);
        }

        cp.add(panel, BorderLayout.SOUTH);

    }


    /**
     * Sets the listener for hyperlink events in this tip window.
     *
     * @param listener The new listener.  The old listener (if any) is
     *                 removed.  A value of <code>null</code> means "no listener."
     */
    public void setHyperlinkListener(HyperlinkListener listener) {
        // We've added a separate listener, so remove only the user's.
        if (userHyperlinkListener != null) {
            textArea.removeHyperlinkListener(userHyperlinkListener);
        }
        userHyperlinkListener = listener;
        if (userHyperlinkListener != null) {
            textArea.addHyperlinkListener(userHyperlinkListener);
        }
    }


    /**
     * Listens for events in this window.
     */
    private class TipListener extends MouseAdapter {

        public TipListener() {
        }

        public void mousePressed(MouseEvent e) {
            actionPerformed(null); // Manually create "real" window
        }

        public void mouseExited(MouseEvent e) {
            // Since we registered this listener on the child components of
            // the JWindow, not the JWindow iteself, we have to be careful.
            Component source = (Component) e.getSource();
            Point p = e.getPoint();
            SwingUtilities.convertPointToScreen(p, source);
            if (!TipWindow.this.getBounds().contains(p)) {
                ft.possiblyDisposeOfTipWindow();
            }
        }

    }

}