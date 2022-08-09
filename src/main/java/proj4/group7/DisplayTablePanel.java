package proj4.group7;

/*
 * 
 */

import java.awt.Dimension;
import java.awt.GridLayout;

import java.math.BigDecimal;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/** 
 * TODO
 */
public class DisplayTablePanel extends JPanel implements IUpdateTable {    
    private String tableName;
    private MyTableModel tableModel;
    private LoginPanel mLoginPanel;

    public DisplayTablePanel(LoginPanel mLoginPanel) {
        super(new GridLayout(1, 0));
    
        this.mLoginPanel = mLoginPanel;
        tableModel = new MyTableModel();

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }
    
    public void update(String name) {
        tableName = name;
        tableModel.load(tableName);
        tableModel.fireTableStructureChanged();
    }

    class MyTableModel extends AbstractTableModel {        
        private ArrayList<Object> rowData;
        private ArrayList<String> columnNames;
        private ArrayList<Integer> columnTypes;
        private ArrayList<String> columnTypeNames;
        private int mNumRows;
        private String mPreparedString;
        private String mErrorMsg;
        
        public MyTableModel() {            
            rowData = new ArrayList<>();
            columnNames = new ArrayList<>();
            columnTypes = new ArrayList<>();
            columnTypeNames = new ArrayList<>();
        }
        
        private void addExtraRow() {
            for (int col = 0; col < columnNames.size(); ++col)
                rowData.add("");
            
            ++mNumRows;
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public int getColumnCount() {
            return columnNames.size();
        }

        public int getRowCount() {
            return mNumRows;
        }

        public String getColumnName(int col) {
            return columnNames.get(col);
        }

        public Object getValueAt(int row, int col) {
            return rowData.get(row * columnNames.size() + col);
        }
        
        public void load(String name)  {
            mNumRows = 0;
            rowData.clear();
            columnNames.clear();
            columnTypes.clear();
            columnTypeNames.clear();
            
            try {
                Statement stmt = mLoginPanel.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("Select * from " + name);
                ResultSetMetaData metaData = rs.getMetaData();
                
                int numColumns = metaData.getColumnCount();
                for(int i = 1; i <= numColumns; ++i) {
                   columnNames.add(metaData.getColumnLabel(i));
                   columnTypes.add(metaData.getColumnType(i));
                   columnTypeNames.add(metaData.getColumnTypeName(i));
                }
                
                while (rs.next()) {
                    ++mNumRows;
                    
                    for (int i = 1; i <= numColumns; ++i)
                        rowData.add(rs.getObject(i).toString());
                }
                
                if (rs != null) rs.close();
                stmt.close();
                
                // Add empty row for possible row insertion
                /***************************************************/
                addExtraRow();
                /***************************************************/
            } catch(java.sql.SQLException se) {                                              
                JOptionPane.showMessageDialog(DisplayTablePanel.this,
                se.getMessage(), "SQL Error",
                JOptionPane.ERROR_MESSAGE);
            }
            
            mPreparedString = getPreparedInsertString();
        }

        /*
         * Make every cell editable
         */
        public boolean isCellEditable(int row, int col) {
            if(row == mNumRows - 1) return true;
            
            int border = 1;
            switch(tableName) {
                case "belongs_to":
                case "sales_item":
                case "return_item":
                    border = 2;
                    
            }
            
            if(col >= border) return true;
            
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
                "This cell must be of Type: " + columnTypeNames.get(pCol) +
                    mErrorMsg, "Wrong Type", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String lOldValue = getValueAt(pRow, pCol).toString();
            rowData.set(pRow * columnNames.size() + pCol, lCellString);
            
            // Inserting at last row
            if(pRow == mNumRows - 1) {
                if(isLastRowFull()) {
                    insertTuple();
                    fireTableStructureChanged();
                }
            } else if(!lCellString.equals(lOldValue)) {
                updateTuple(pRow, pCol, lCellString);
                fireTableCellUpdated(pRow, pCol);
            }
        }
        
        private String getPreparedInsertString() {            
            StringBuilder lPreparedString = new StringBuilder(50);

            lPreparedString.append("INSERT INTO ");
            lPreparedString.append(tableName);
            lPreparedString.append(" VALUES (?");
            
            for(int col = 1; col < columnNames.size(); ++col) {
                lPreparedString.append(", ?");
            }
   
            lPreparedString.append(")");
            
            return lPreparedString.toString();
        }
        
        private boolean isCellValid(int pRow, int pCol, String pCellString) {
            mErrorMsg = "";
            switch(columnTypes.get(pCol)) {
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
                    System.out.println("TODO: " + columnTypeNames.get(pCol));
            }
            
            return true;
        }
        
        private void insertTuple() {            
            try {
                PreparedStatement lPs =
                    mLoginPanel.getConnection().prepareStatement(
                        mPreparedString);                    
                           
                for(int col = 0; col < columnTypes.size(); ++col) {
                    String lCellString = getValueAt(
                        mNumRows - 1, col).toString();
                        
                    switch(columnTypes.get(col)) {
                        case Types.INTEGER:
                            lPs.setInt(col + 1, Integer.parseInt(lCellString));
                        break;
                        
                        case Types.DECIMAL:
                            lPs.setBigDecimal(col + 1, (
                                new BigDecimal(lCellString).setScale(2)));
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
                                columnTypeNames.get(col));
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
                se.getMessage(), "SQL Error",
                JOptionPane.ERROR_MESSAGE);
            } 
        }
        
        private boolean isLastRowFull() {            
            for(int col = 0; col < columnNames.size(); ++col) {
                if(getValueAt(mNumRows - 1, col).equals(""))
                    return false;
            }
            
            return true;
        }
        
        private void updateTuple(int pRow, int pCol, String pNewString) {
            try {                   
                String lUpdateStr = String.format(
                    "UPDATE %s SET %s=? WHERE %s", tableName,
                    columnNames.get(pCol), whereClause(pRow));
                PreparedStatement lPs =
                    mLoginPanel.getConnection().prepareStatement(lUpdateStr);              
                        
                switch(columnTypes.get(pCol)) {
                    case Types.INTEGER:
                        lPs.setInt(1, Integer.parseInt(pNewString));
                    break;
                    
                    case Types.DECIMAL:
                        lPs.setBigDecimal(1, (
                            new BigDecimal(pNewString).setScale(2)));
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
                            columnTypeNames.get(pCol));
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
            switch(tableName) {
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
