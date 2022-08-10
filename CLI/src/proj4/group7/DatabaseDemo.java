package proj4.group7;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** 
 * Mainclass to display the JFrame
 */
public class DatabaseDemo extends JFrame implements IDoneListener {
    
    private LoginPanel mLoginPanel;    
    private ChooseTablePanel mChoosePanel;    
    private DisplayTablePanel mDPanel;

    public DatabaseDemo() {
        // Create and set up the window.
        super("DatabaseDemo");        
        
        setLayout(new GridBagLayout());
        GridBagConstraints lLoginConstraints = new GridBagConstraints();
        lLoginConstraints.fill = GridBagConstraints.HORIZONTAL;
        lLoginConstraints.gridx = 0;
        lLoginConstraints.gridy = 0;
        
        mLoginPanel = new LoginPanel(this);
        add(mLoginPanel, lLoginConstraints);
        
        GridBagConstraints lDPanelConstraints = new GridBagConstraints();
        lDPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        lDPanelConstraints.gridx = 0;
        lDPanelConstraints.gridy = 1;
        
        mDPanel = new DisplayTablePanel(mLoginPanel);
        add(mDPanel, lDPanelConstraints);        
        mDPanel.setVisible(false);
        
        GridBagConstraints lChooseConstraints = new GridBagConstraints();
        lChooseConstraints.fill = GridBagConstraints.HORIZONTAL;
        lChooseConstraints.gridx = 0;
        lChooseConstraints.gridy = 0;
        
        mChoosePanel = new ChooseTablePanel(mDPanel);
        add(mChoosePanel, lChooseConstraints);        
        mChoosePanel.setVisible(false);
        
        getRootPane().setDefaultButton(mLoginPanel.getDefaultButton());
        
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent pWe) {
                mLoginPanel.cleanUp();
                System.exit(0);
            }
        });       
               
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    public void done() {
        mLoginPanel.setVisible(false);
        mChoosePanel.setVisible(true);
        mDPanel.setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] pArgs) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DatabaseDemo();
            }
        });
    }
}