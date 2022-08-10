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
 * TODO
 */
public class DatabaseDemo extends JFrame implements IDoneListener {
    
    private LoginPanel loginPanel;    
    private ChooseTablePanel choosePanel;    
    private DisplayTablePanel dPanel;

    public DatabaseDemo() {
        //Create and set up the window.
        super("DatabaseDemo");
        
        
        setLayout(new GridBagLayout());
        GridBagConstraints loginConstraints = new GridBagConstraints();
        loginConstraints.fill = GridBagConstraints.HORIZONTAL;
        loginConstraints.gridx = 0;
        loginConstraints.gridy = 0;
        
        loginPanel = new LoginPanel(this);
        add(loginPanel, loginConstraints);
        
        GridBagConstraints dPanelConstraints = new GridBagConstraints();
        dPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        dPanelConstraints.gridx = 0;
        dPanelConstraints.gridy = 1;
        
        dPanel = new DisplayTablePanel(loginPanel);
        add(dPanel, dPanelConstraints);        
        dPanel.setVisible(false);
        
        GridBagConstraints chooseConstraints = new GridBagConstraints();
        chooseConstraints.fill = GridBagConstraints.HORIZONTAL;
        chooseConstraints.gridx = 0;
        chooseConstraints.gridy = 0;
        
        choosePanel = new ChooseTablePanel(dPanel);
        add(choosePanel, chooseConstraints);        
        choosePanel.setVisible(false);
        
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loginPanel.cleanUp();
                System.exit(0);
            }
        });       
               
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    public void done() {
        loginPanel.setVisible(false);
        choosePanel.setVisible(true);
        dPanel.setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DatabaseDemo();
            }
        });
    }
}
