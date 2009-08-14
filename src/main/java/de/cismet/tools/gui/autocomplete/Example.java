/*
 * Example.java
 *
 * Created on October 25, 2005, 2:45 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package de.cismet.tools.gui.autocomplete;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimeZone;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 */
public class Example {

    /** Creates a new instance of Example */
    public Example() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(_createPanel());
        frame.setBounds(100, 100, 450, 350);
        frame.setVisible(true);
    }

    private JPanel _createPanel() {
        _tf = new CompleterTextField(TimeZone.getAvailableIDs(), false);

        final JCheckBox caseCheck = new JCheckBox("Case-sensitive");
        caseCheck.setSelected(_tf.isCaseSensitive());

        final JCheckBox correctCheck = new JCheckBox("Correct case");
        correctCheck.setSelected(_tf.isCorrectingCase());
        correctCheck.setEnabled(!caseCheck.isSelected());
        correctCheck.setToolTipText("Will change the case of the entered string to match the case of the matched string");

        caseCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                _tf.setCaseSensitive(caseCheck.isSelected());
                correctCheck.setEnabled(!caseCheck.isSelected());
                if (caseCheck.isSelected()) {
                    correctCheck.setSelected(false);
                    _tf.setCorrectCase(false);
                }
            }
        });

        correctCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                _tf.setCorrectCase(correctCheck.isSelected());
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Auto-complete text field:"));
        panel.add(_tf);

        panel.add(Box.createGlue());
        panel.add(caseCheck);

        panel.add(Box.createGlue());
        panel.add(correctCheck);

        panel.add(Box.createVerticalStrut(20));
        panel.add(Box.createVerticalStrut(20));

        _tfww = new CompleterTextField(TimeZone.getAvailableIDs(), true);
        final JCheckBox caseCheck2 = new JCheckBox("Case-sensitive");
        caseCheck2.setSelected(_tfww.isCaseSensitive());

        final JCheckBox correctCheck2 = new JCheckBox("Correct case");
        correctCheck2.setSelected(_tfww.isCorrectingCase());
        correctCheck2.setEnabled(!caseCheck2.isSelected());
        correctCheck2.setToolTipText("Will change the case of the entered string to match the case of the matched string");

        caseCheck2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                _tfww.setCaseSensitive(caseCheck2.isSelected());
                correctCheck2.setEnabled(!caseCheck2.isSelected());
                if (caseCheck2.isSelected()) {
                    correctCheck2.setSelected(false);
                    _tfww.setCorrectCase(false);
                }
            }
        });

        correctCheck2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                _tfww.setCorrectCase(correctCheck2.isSelected());
            }
        });

        panel.add(new JLabel("Auto-complete text field with window:"));
        panel.add(_tfww);

        panel.add(Box.createGlue());
        panel.add(caseCheck2);

        panel.add(Box.createGlue());
        panel.add(correctCheck2);

        panel.add(Box.createVerticalStrut(20));
        panel.add(Box.createVerticalStrut(20));
        final JButton tbn = new JButton(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(_combo.getSelectedItem() + " " + _combo.getSelectedIndex() + " " + _combo.getModel());
            }
        });
        tbn.setText("getSelected");
        panel.add(tbn);
//    _combo = new CompleterComboBox(new String[]{""," ","aa","Aa","aA","AA","Spielplatz"});
        final DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
        _combo = new JComboBox(new TT[]{new TT("aa"), new TT("Aa"), new TT("aA"), new TT("AA"), new TT("Spielplatz"), null});
        _combo.setRenderer(new ListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component ret = dlcr.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    ((JLabel) ret).setText("nix");
                }
                return ret;
            }
        });
        final ComboCompleterFilter filter = ComboCompleterFilter.addCompletionMechanism(_combo);
//    _combo = new CompleterComboBox(new String[]{"x"," ","aa","Aa","aA","AA","Spielplatz"});
        filter.setStrict(false);
        filter.setNullRespresentation("nix");
//    _combo = new CompleterComboBox(TimeZone.getAvailableIDs());
        final JCheckBox caseCheck3 = new JCheckBox("Case-sensitive");
        caseCheck3.setSelected(filter.isCaseSensitive());

        final JCheckBox correctCheck3 = new JCheckBox("Correct case");
        correctCheck3.setSelected(filter.isCorrectingCase());
        correctCheck3.setEnabled(!caseCheck3.isSelected());
        correctCheck3.setToolTipText("Will change the case of the entered string to match the case of the matched string");

        caseCheck3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                filter.setCaseSensitive(caseCheck3.isSelected());
                correctCheck3.setEnabled(!caseCheck3.isSelected());
                if (caseCheck3.isSelected()) {
                    correctCheck3.setSelected(false);
                    filter.setCorrectCase(false);
                }
            }
        });

        correctCheck3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                filter.setCorrectCase(correctCheck3.isSelected());
            }
        });

        panel.add(new JLabel("Auto-complete combo:"));
        panel.add(_combo);

        panel.add(Box.createGlue());
        panel.add(caseCheck3);

        panel.add(Box.createGlue());
        panel.add(correctCheck3);

        return panel;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Example();
    }
    private CompleterTextField _tf;
    private CompleterTextField _tfww;
    private JComboBox _combo;
}

class TT {

    public TT(String bdy) {
        this.bdy = bdy;
    }
    final String bdy;

    @Override
    public String toString() {
        return bdy;
    }
}
