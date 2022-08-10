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
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel dbNameLabel;
    private JLabel driverNameLabel;
    
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField dbNameField;
    private JTextField driverNameField;
    
    private JButton loginButton;
    
    private IDoneListener donePanel;
    
    private Connection conn = null;
    
    public LoginPanel(IDoneListener donePanel) {
        super(new GridLayout(5, 2, 10, 5));
        
        this.donePanel = donePanel;
        
        initComponents();
    }
    
    public void actionPerformed(ActionEvent event) {
        // Remove leading and trailing spaces from all fields
        usernameField.setText(usernameField.getText().strip());
        passwordField.setText(passwordField.getText().strip());
        dbNameField.setText(dbNameField.getText().strip());
        driverNameField.setText(driverNameField.getText().strip());
        
        // Check for empty fields
        if(usernameField.getText().equals("") ||
           passwordField.getText().equals("") ||
           dbNameField.getText().equals("") ||
           driverNameField.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "No field may be empty!",
            "Empty field error", JOptionPane.ERROR_MESSAGE);
            return;
        }        
        
        loginButton.setText("Connecting to Database...");
        loginButton.setEnabled(false);       
        
        connect(this);
    }
    
    public void cleanUp() {    
        try {
            if (conn != null) {
                // check if the connection is open, and if so do a rollback
                // to avoid a transaction context sitting open on the server
                conn.rollback();
                conn.close();                    
            }
        } catch (Throwable t2) {
            JOptionPane.showMessageDialog(this, "Oh-oh! Connection leaked!",
                "Connection error", JOptionPane.ERROR_MESSAGE);
        }
    }    
    
    private void connect(java.awt.Component parent) {
		SwingWorker sw1 = new SwingWorker<Void, Void>() {
            
			@Override
			protected Void doInBackground() throws Exception {                
                try {
                    // Load the JDBC driver
                    Class.forName(driverNameField.getText());
                    
                    String url = "jdbc:mysql://localhost/" +
                        dbNameField.getText() +
                        "?autoReconnect=true&useSSL=false";

                    // Make a connection
                    conn = DriverManager.getConnection(url,
                        usernameField.getText(), passwordField.getText());
                    conn.setAutoCommit(false);
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
                if(conn == null) {
                    loginButton.setText("Login");
                    loginButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(parent,
                        "Connection successful.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    donePanel.done();
                }
			}
		};

		// Executes the swingworker on worker thread
		sw1.execute();
	}

    public Connection getConnection() { return conn; }
    
    private void initComponents() {        
        usernameLabel = new JLabel("Username:");
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        passwordLabel = new JLabel("Password:");
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        dbNameLabel = new JLabel("Database:");
        dbNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        driverNameLabel = new JLabel("Driver:");
        driverNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        usernameField = new JTextField(20);
        passwordField = new JTextField(20);
        dbNameField = new JTextField(20);
        driverNameField = new JTextField(20);
        
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(dbNameLabel);
        add(dbNameField);
        add(driverNameLabel);
        add(driverNameField);
        add(new JLabel());
        add(loginButton);
        
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.black), "Login"));
            
            
        /** for debugging remove later **/
        usernameField.setText("root");
        passwordField.setText("dragon77");
        dbNameField.setText("group7db");
        driverNameField.setText("com.mysql.jdbc.Driver");
    }
}