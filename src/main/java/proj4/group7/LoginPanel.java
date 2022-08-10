package proj4.group7;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.util.concurrent.ExecutionException;

/*
 * Handles the credentials to log into a database
 */
public class LoginPanel extends JPanel implements ActionListener {
    private JLabel mUsernameLabel;
    private JLabel mPasswordLabel;
    private JLabel mDbNameLabel;
    private JLabel mDriverNameLabel;
    
    private JTextField mUsernameField;
    private JTextField mPasswordField;
    private JTextField mDbNameField;
    private JTextField mDriverNameField;
    
    private JButton mLoginButton;    
    private IDoneListener mDonePanel;    
    private Connection mConnection = null;
    
    public LoginPanel(IDoneListener pDonePanel) {
        super(new GridLayout(5, 2, 10, 5));
        
        mDonePanel = pDonePanel;
        
        initComponents();
    }
    
    public void actionPerformed(ActionEvent event) {
        // Remove leading and trailing spaces from all fields
        mUsernameField.setText(mUsernameField.getText().strip());
        mPasswordField.setText(mPasswordField.getText().strip());
        mDbNameField.setText(mDbNameField.getText().strip());
        mDriverNameField.setText(mDriverNameField.getText().strip());
        
        // Check for empty fields
        if(mUsernameField.getText().equals("") ||
           mPasswordField.getText().equals("") ||
           mDbNameField.getText().equals("") ||
           mDriverNameField.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "No field may be empty!",
            "Empty field error", JOptionPane.ERROR_MESSAGE);
            return;
        }        
        
        mLoginButton.setText("Connecting to Database...");
        mLoginButton.setEnabled(false);       
        
        connect(this);
    }
    
    public void cleanUp() {    
        try {
            if (mConnection != null) {
                // check if the connection is open, and if so do a rollback
                // to avoid a transaction context sitting open on the server
                mConnection.rollback();
                mConnection.close();                    
            }
        } catch (Throwable t2) {
            JOptionPane.showMessageDialog(this, "Oh-oh! Connection leaked!",
                "Connection error", JOptionPane.ERROR_MESSAGE);
        }
    }    
    
    private void connect(java.awt.Component parent) {
		SwingWorker lSwingWorker = new SwingWorker<Void, Void>() {
            
			@Override
			protected Void doInBackground() throws Exception {                
                try {
                    // Load the JDBC driver
                    Class.forName(mDriverNameField.getText());
                    
                    String url = "jdbc:mysql://localhost/" +
                        mDbNameField.getText() +
                        "?autoReconnect=true&useSSL=false";

                    // Make a connection
                    mConnection = DriverManager.getConnection(url,
                        mUsernameField.getText(), mPasswordField.getText());
                    mConnection.setAutoCommit(false);
                } catch(ClassNotFoundException cnfe) {            
                    JOptionPane.showMessageDialog(parent,
                        "The driver cannot be loaded!", "Driver error",
                        JOptionPane.ERROR_MESSAGE);
                } catch(SQLException se) {            
                    JOptionPane.showMessageDialog(parent, se.getMessage(),
                    "SQL error", JOptionPane.ERROR_MESSAGE);
                } 
                
                return null;
			}
			
			@Override
            protected void done() {        
                if(mConnection == null) {
                    mLoginButton.setText("Login");
                    mLoginButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(parent,
                        "Connection successful.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    mDonePanel.done();
                }
			}
		};

		// Executes the swingworker on worker thread
		lSwingWorker.execute();
	}

    public Connection getConnection() { return mConnection; }
    
    private void initComponents() {        
        mUsernameLabel = new JLabel("Username:");
        mUsernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        mPasswordLabel = new JLabel("Password:");
        mPasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        mDbNameLabel = new JLabel("Database:");
        mDbNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        mDriverNameLabel = new JLabel("Driver:");
        mDriverNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        mUsernameField = new JTextField(20);
        mPasswordField = new JTextField(20);
        mDbNameField = new JTextField(20);
        mDriverNameField = new JTextField(20);
        
        mLoginButton = new JButton("Login");
        mLoginButton.addActionListener(this);
        
        add(mUsernameLabel);
        add(mUsernameField);
        add(mPasswordLabel);
        add(mPasswordField);
        add(mDbNameLabel);
        add(mDbNameField);
        add(mDriverNameLabel);
        add(mDriverNameField);
        add(new JLabel());
        add(mLoginButton);
        
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.black), "Login"));
            
            
        /** for debugging remove later **/
        mUsernameField.setText("root");
        mPasswordField.setText("dragon77");
        mDbNameField.setText("group7db");
        mDriverNameField.setText("com.mysql.jdbc.Driver");
    }
}