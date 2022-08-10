package proj4.group7;

/*
 * 
 */

import java.awt.Dimension;
import java.awt.GridLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** 
 * TODO
 */
public class DisplayTablePanel extends JPanel implements IUpdateTableListener {
    private String mTableName;
    private MyTableModel mTableModel;
    private LoginPanel mLoginPanel;
    private JTable mTable;

    public DisplayTablePanel(LoginPanel pLoginPanel) {    
        mLoginPanel = pLoginPanel;
        mTableModel = new MyTableModel();

        mTable = new JTable(mTableModel);
        mTable.setFillsViewportHeight(true);
        mTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        

        //Create the scroll pane and add the mTable to it.
        JScrollPane lScrollPane = new JScrollPane(mTable);

        //Add the scroll pane to this panel.
        add(lScrollPane);
        add(mTableModel.getButton());
    }
    
    public void update(String name) {
        mTableName = name;
        mTableModel.load(mTableName);
        mTableModel.fireTableStructureChanged();
    }

    class MyTableModel extends AbstractTableModel implements ActionListener {        
        private ArrayList<Object> mRowData;
        private ArrayList<String> mColumnNames;
        private ArrayList<Integer> mColumnTypes;
        private ArrayList<String> mColumnTypeNames;
        private int mNumRows;
        private String mPreparedString;
        private String mErrorMsg;
        
        private JButton mDeleteButton;
        
        public MyTableModel() {
            mDeleteButton = new JButton("Deleted Selected Row");
            mDeleteButton.addActionListener(this);
            mRowData = new ArrayList<>();
            mColumnNames = new ArrayList<>();
            mColumnTypes = new ArrayList<>();
            mColumnTypeNames = new ArrayList<>();
        }
        
        public void actionPerformed(ActionEvent event) {
            int lSelectedRow = mTable.getSelectedRow();
            if(lSelectedRow == -1) {
                JOptionPane.showMessageDialog(DisplayTablePanel.this,
                    "You must select a row!", "No row selected",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }            
            
            if(lSelectedRow == mNumRows - 1) {
                JOptionPane.showMessageDialog(DisplayTablePanel.this,
                    "Can't delete an empty row", "Empty row error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {                   
                String lDeleteRowStr = String.format(
                    "DELETE FROM %s WHERE %s",
                    mTableName, whereClause(lSelectedRow));
                Statement lStmt =
                    mLoginPanel.getConnection().createStatement();
                
                if (lStmt.executeUpdate(lDeleteRowStr) > 0) {                    
                    JOptionPane.showMessageDialog(DisplayTablePanel.this,
                        "Tuple deleted!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
                mLoginPanel.getConnection().commit();
                lStmt.close();
                
            } catch(java.sql.SQLException se) {                              
                JOptionPane.showMessageDialog(DisplayTablePanel.this,
                se.getMessage(), "SQL Error",
               JOptionPane.ERROR_MESSAGE);
            }
            
            update(mTableName);
        }
        
        private void addExtraRow() {
            for (int col = 0; col < mColumnNames.size(); ++col)
                mRowData.add("");
            
            ++mNumRows;
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int pCol) {
            return getValueAt(0, pCol).getClass();
        }
        
        public JButton getButton() {
            return mDeleteButton;
        }

        public int getColumnCount() {
            return mColumnNames.size();
        }

        public int getRowCount() {
            return mNumRows;
        }

        public String getColumnName(int col) {
            return mColumnNames.get(col);
        }

        public Object getValueAt(int row, int col) {
            return mRowData.get(row * mColumnNames.size() + col);
        }
        
        public void load(String name)  {
            mNumRows = 0;
            mRowData.clear();
            mColumnNames.clear();
            mColumnTypes.clear();
            mColumnTypeNames.clear();
            
            try {
                Statement lStmt = mLoginPanel.getConnection().createStatement();
                ResultSet lRs = lStmt.executeQuery("Select * from " + name);
                ResultSetMetaData lMetaData = lRs.getMetaData();
                
                int lNumColumns = lMetaData.getColumnCount();
                for(int i = 1; i <= lNumColumns; ++i) {
                   mColumnNames.add(lMetaData.getColumnLabel(i));
                   mColumnTypes.add(lMetaData.getColumnType(i));
                   mColumnTypeNames.add(lMetaData.getColumnTypeName(i));
                }
                
                while (lRs.next()) {
                    ++mNumRows;
                    
                    for (int i = 1; i <= lNumColumns; ++i)
                        mRowData.add(lRs.getObject(i).toString());
                }
                
                if (lRs != null) lRs.close();
                lStmt.close();
                
                // Add empty row for possible row insertion
                /***************************************************/
                addExtraRow();
                /***************************************************/
            } catch(java.sql.SQLException pSE) {                                              
                JOptionPane.showMessageDialog(DisplayTablePanel.this,
                pSE.getMessage(), "SQL Error",
                JOptionPane.ERROR_MESSAGE);
            }
            
            mPreparedString = getPreparedInsertString();
        }

        /*
         * Make every cell editable
         */
        public boolean isCellEditable(int pRow, int pCol) {
            if(pRow == mNumRows - 1) return true;
            
            int lBorder = 1;
            switch(mTableName) {
                case "belongs_to":
                case "sales_item":
                case "return_item":
                    lBorder = 2;
                    
            }
            
            if(pCol >= lBorder) return true;
            
            JOptionPane.showMessageDialog(DisplayTablePanel.this,
                "Can't edit Primary Key column", "Edit PK Error",
                JOptionPane.ERROR_MESSAGE);
            
            return false;
        }

        /*
         * Updates cell at (pRow, pCol) to pValue, if pValue is valid
         *
         */
        public void setValueAt(Object pValue, int pRow, int pCol) {
            // Remove leading and trailing spaces from user's string
            String lCellString = ((String) pValue).strip();

            // Empty strings only allowed on the last row
            if(pRow != mNumRows - 1 && lCellString.equals("")) {                
                JOptionPane.showMessageDialog(DisplayTablePanel.this,
                "No field may be empty!", "Empty field error",
                JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate cell
            if(!lCellString.equals("") &&
               !isCellValid(pRow, pCol, lCellString)) {
                JOptionPane.showMessageDialog(DisplayTablePanel.this,
                "This cell must be of Type: " + mColumnTypeNames.get(pCol) +
                    mErrorMsg, "Wrong Type", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String lOldValue = getValueAt(pRow, pCol).toString();
            mRowData.set(pRow * mColumnNames.size() + pCol, lCellString);
            
            // Inserting at last row
            if(pRow == mNumRows - 1) {
                if(isLastRowFull()) {
                    insertTuple();
                    update(mTableName);
                }
            } else if(!lCellString.equals(lOldValue)) {
                updateTuple(pRow, pCol, lCellString);
                    update(mTableName);
            }
        }
        
        private String getPreparedInsertString() {            
            StringBuilder lPreparedString = new StringBuilder(50);

            lPreparedString.append("INSERT INTO ");
            lPreparedString.append(mTableName);
            lPreparedString.append(" VALUES (?");
            
            for(int col = 1; col < mColumnNames.size(); ++col) {
                lPreparedString.append(", ?");
            }
   
            lPreparedString.append(")");
            
            return lPreparedString.toString();
        }
        
        private boolean isCellValid(int pRow, int pCol, String pCellString) {
            mErrorMsg = "";
            switch(mColumnTypes.get(pCol)) {
                case Types.INTEGER:
                    try {
                        if(Integer.parseInt(pCellString) <= 0) {
                            mErrorMsg = "\nCell must be positive!";
                            throw new NumberFormatException();
                        }
                    } catch(NumberFormatException nfe) {
                        return false;
                    }
                break;
                
                case Types.DECIMAL:
                    try {
                        if((new BigDecimal(pCellString)).doubleValue() <= 0) {
                            mErrorMsg = "\nCell must be positive!";
                            throw new NumberFormatException();
                        }
                    } catch(NumberFormatException nfe) {
                        return false;
                    }
                break;
                
                case Types.VARCHAR:
                case Types.CHAR:
                break;
                        
                case Types.TIMESTAMP:
                    try {
                        Timestamp lDateTime = Timestamp.valueOf(pCellString);
                    } catch(IllegalArgumentException nfe) {
                        mErrorMsg = "Format: yyyy-[m]m-[d]d hh:mm:ss[.f...]"; 
                        return false;
                    }
                break;
                
                default:
                    // Code should not reach here because we have
                    // handled all types in our database
                    System.out.println("TODO: " + mColumnTypeNames.get(pCol));
            }
            
            return true;
        }
        
        private void insertTuple() {            
            try {
                PreparedStatement lPs =
                    mLoginPanel.getConnection().prepareStatement(
                        mPreparedString);                    
                           
                for(int col = 0; col < mColumnTypes.size(); ++col) {
                    String lCellString = getValueAt(
                        mNumRows - 1, col).toString();
                        
                    switch(mColumnTypes.get(col)) {
                        case Types.INTEGER:
                            lPs.setInt(col + 1, Integer.parseInt(lCellString));
                        break;
                        
                        case Types.DECIMAL:
                            lPs.setBigDecimal(col + 1, (
                                new BigDecimal(lCellString).setScale(2,
                                    RoundingMode.FLOOR)));
                        break;
                        
                        case Types.VARCHAR:
                        case Types.CHAR:
                            lPs.setString(col + 1, lCellString);
                        break;
                        
                        case Types.TIMESTAMP:
                            lPs.setTimestamp(col + 1,
                                Timestamp.valueOf(lCellString));
                        break;
                
                        default:
                            // Code should not reach here because we have
                            // handled all types in our database
                            System.out.println("TODOx: " +
                                mColumnTypeNames.get(col));
                            return;
                    }
                }
                
                if (lPs.executeUpdate() > 0) {                    
                    JOptionPane.showMessageDialog(DisplayTablePanel.this,
                        "Tuple Inserted!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    addExtraRow();
                }
                
                mLoginPanel.getConnection().commit();
                lPs.close();
                
            } catch(java.sql.SQLException se) {                              
                JOptionPane.showMessageDialog(DisplayTablePanel.this,
                    se.getMessage(), "SQL Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private boolean isLastRowFull() {            
            for(int col = 0; col < mColumnNames.size(); ++col) {
                if(getValueAt(mNumRows - 1, col).equals(""))
                    return false;
            }
            
            return true;
        }
        
        private void updateTuple(int pRow, int pCol, String pNewString) {
            try {                   
                String lUpdateStr = String.format(
                    "UPDATE %s SET %s=? WHERE %s", mTableName,
                    mColumnNames.get(pCol), whereClause(pRow));
                PreparedStatement lPs =
                    mLoginPanel.getConnection().prepareStatement(lUpdateStr);              
                        
                switch(mColumnTypes.get(pCol)) {
                    case Types.INTEGER:
                        lPs.setInt(1, Integer.parseInt(pNewString));
                    break;
                    
                    case Types.DECIMAL:
                        lPs.setBigDecimal(1, (
                            new BigDecimal(pNewString).setScale(2,
                                RoundingMode.FLOOR)));
                    break;
                    
                    case Types.VARCHAR:
                    case Types.CHAR:
                        lPs.setString(1, pNewString);
                    break;
                    
                    case Types.TIMESTAMP:
                        lPs.setTimestamp(1,
                            Timestamp.valueOf(pNewString));
                    break;
            
                    default:
                        // Code should not reach here because we have
                        // handled all types in our database
                        System.out.println("TODO: " +
                            mColumnTypeNames.get(pCol));
                        return;
                }
                
                if (lPs.executeUpdate() > 0) {                    
                    JOptionPane.showMessageDialog(DisplayTablePanel.this,
                        "Tuple updated!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
                mLoginPanel.getConnection().commit();
                lPs.close();
                
            } catch(java.sql.SQLException se) {                              
                JOptionPane.showMessageDialog(DisplayTablePanel.this,
                se.getMessage(), "SQL Error",
               JOptionPane.ERROR_MESSAGE);
            }
        }
        
        public String whereClause(int pRow) {
            switch(mTableName) {
                case "belongs_to":
                    return String.format("StoreID=%s AND ProductID=%s",
                            getValueAt(pRow, 0), getValueAt(pRow, 1));
                               
                case "customer":
                    return String.format("RewardsID=%s", getValueAt(pRow, 0));
                               
                case "cust_sales":
                    return String.format("InvoiceID='%s'", getValueAt(pRow, 0));
                               
                case "manufacturer":
                    return String.format("MName='%s'", getValueAt(pRow, 0));
                               
                case "product":
                    return String.format("ProductID=%s", getValueAt(pRow, 0));
                               
                case "return_item":
                    return String.format("InvoiceID='%s' AND ProductID=%s",
                        getValueAt(pRow, 0), getValueAt(pRow, 1));
                               
                case "sales":
                    return String.format(
                        "InvoiceID='%s'", getValueAt(pRow, 0));
                               
                case "sales_item":
                    return String.format("InvoiceID='%s' AND ProductID=%s",
                        getValueAt(pRow, 0), getValueAt(pRow, 1));
                               
                case "store":
                    return String.format("StoreID=%s", getValueAt(pRow, 0));
            }
            
            return "";
        }
    }
}
