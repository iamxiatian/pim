/*
 *  NachoCalendar
 *
 * Project Info:  http://nachocalendar.sf.net
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * Changes
 * -------
 *
 * CustomizerDemo.java
 *
 * Created on December 28, 2005, 2:35 PM
 */

package net.sf.nachocalendar.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import net.sf.nachocalendar.components.CalendarPanel;
import net.sf.nachocalendar.components.DateField;
import net.sf.nachocalendar.components.DatePanel;
import net.sf.nachocalendar.customizer.Customizer;
import net.sf.nachocalendar.customizer.CustomizerFactory;
import net.sf.nachocalendar.customizer.DirectSetter;
import net.sf.nachocalendar.customizer.PropertiesCustomizer;
import net.sf.nachocalendar.customizer.PropertiesSetter;
import net.sf.nachocalendar.customizer.XMLCustomizer;

/**
 * @author Ignacio Merani
 */
public class CustomizerDemo extends javax.swing.JDialog {
    private JFileChooser chooser;

    /**
     * Creates new form CustomizerDemo
     */
    public CustomizerDemo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JLabel jLabel2;
        javax.swing.JPanel jPanel1;
        javax.swing.JPanel jPanel2;
        javax.swing.JPanel jPanel3;

        jPanel1 = new javax.swing.JPanel();
        bClose = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tCustomizer = new javax.swing.JTextField();
        bBrowse = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        bDateField = new javax.swing.JButton();
        bDatePanel = new javax.swing.JButton();
        bCalendarPanel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Customizer Demo");
        bClose.setText("Close");
        bClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseActionPerformed(evt);
            }
        });

        jPanel1.add(bClose);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Customization");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(tCustomizer, gridBagConstraints);

        bBrowse.setText("Browse...");
        bBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBrowseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(bBrowse, gridBagConstraints);

        bDateField.setText("DateField");
        bDateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDateFieldActionPerformed(evt);
            }
        });

        jPanel3.add(bDateField);

        bDatePanel.setText("DatePanel");
        bDatePanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDatePanelActionPerformed(evt);
            }
        });

        jPanel3.add(bDatePanel);

        bCalendarPanel.setText("CalendarPanel");
        bCalendarPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCalendarPanelActionPerformed(evt);
            }
        });

        jPanel3.add(bCalendarPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel3, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void bBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBrowseActionPerformed
        if (chooser == null) createChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            tCustomizer.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_bBrowseActionPerformed

    private void bCalendarPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCalendarPanelActionPerformed
        File f = verifyFile();
        if (f == null) return;
        CustomizerFactory factory = null;
        try {
            factory = new CustomizerFactory(f);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ComponentForm form = new ComponentForm(this, true);
        CalendarPanel calendarpanel = factory.createCalendarPanel();
        form.addComponent(calendarpanel);
        form.setVisible(true);
    }//GEN-LAST:event_bCalendarPanelActionPerformed

    private void bDatePanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDatePanelActionPerformed
        File f = verifyFile();
        if (f == null) return;
        CustomizerFactory factory = null;
        try {
            factory = new CustomizerFactory(f);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ComponentForm form = new ComponentForm(this, true);
        DatePanel datepanel = factory.createDatePanel();
        form.addComponent(datepanel);
        form.setVisible(true);
    }//GEN-LAST:event_bDatePanelActionPerformed

    private void bDateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDateFieldActionPerformed
        File f = verifyFile();
        if (f == null) return;
        CustomizerFactory factory = null;
        try {
            factory = new CustomizerFactory(f);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ComponentForm form = new ComponentForm(this, true);
        DateField datefield = factory.createDateField();
        form.addComponent(datefield);
        form.setVisible(true);
    }//GEN-LAST:event_bDateFieldActionPerformed

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_bCloseActionPerformed

    private File verifyFile() {
        File f = new File(tCustomizer.getText());
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "File doesn't exist!");
            return null;
        }
        return f;
    }

    private void createChooser() {
        chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                if (f.getName().toLowerCase().endsWith(".xml")) return true;
                return false;
            }

            public String getDescription() {
                return "XML files (*.xml)";
            }

        });
        chooser.addChoosableFileFilter(new FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                if (f.getName().toLowerCase().endsWith(".properties")) return true;
                return false;
            }

            public String getDescription() {
                return "Properties files (*.properties)";
            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBrowse;
    private javax.swing.JButton bCalendarPanel;
    private javax.swing.JButton bClose;
    private javax.swing.JButton bDateField;
    private javax.swing.JButton bDatePanel;
    private javax.swing.JTextField tCustomizer;
    // End of variables declaration//GEN-END:variables

}
