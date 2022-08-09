package proj4.group7;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class ChooseTablePanel extends JPanel
                             implements ActionListener {
    private final static String belongsToStr = "belongs_to";
    private final static String customerStr = "customer";
    private final static String custSalesStr = "cust_sales";
    private final static String manStr = "manufacturer";
    private final static String productStr = "product";
    private final static String returnItemStr = "return_item";
    private final static String salesStr = "sales";
    private final static String salesItemStr = "sales_item";
    private final static String storeStr = "store";    
    
    private IUpdateTable updateTable;

    public ChooseTablePanel(IUpdateTable updateTable) {
        this.updateTable = updateTable;
        
        //Create the radio buttons.
        JRadioButton belongsToButton = new JRadioButton(belongsToStr);
        belongsToButton.setMnemonic(KeyEvent.VK_B);
        belongsToButton.setActionCommand(belongsToStr);
        belongsToButton.setSelected(true);

        JRadioButton customerButton = new JRadioButton(customerStr);
        customerButton.setMnemonic(KeyEvent.VK_C);
        customerButton.setActionCommand(customerStr);

        JRadioButton custSalesButton = new JRadioButton(custSalesStr);
        custSalesButton.setMnemonic(KeyEvent.VK_U);
        custSalesButton.setActionCommand(custSalesStr);

        JRadioButton manButton = new JRadioButton(manStr);
        manButton.setMnemonic(KeyEvent.VK_M);
        manButton.setActionCommand(manStr);

        JRadioButton productButton = new JRadioButton(productStr);
        productButton.setMnemonic(KeyEvent.VK_P);
        productButton.setActionCommand(productStr);

        JRadioButton returnItemButton = new JRadioButton(returnItemStr);
        returnItemButton.setMnemonic(KeyEvent.VK_R);
        returnItemButton.setActionCommand(returnItemStr);

        JRadioButton salesButton = new JRadioButton(salesStr);
        salesButton.setMnemonic(KeyEvent.VK_S);
        salesButton.setActionCommand(salesStr);

        JRadioButton salesItemButton = new JRadioButton(salesItemStr);
        salesItemButton.setMnemonic(KeyEvent.VK_A);
        salesItemButton.setActionCommand(salesItemStr);

        JRadioButton storeButton = new JRadioButton(storeStr);
        storeButton.setMnemonic(KeyEvent.VK_T);
        storeButton.setActionCommand(storeStr);

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(belongsToButton);
        group.add(customerButton);
        group.add(custSalesButton);
        group.add(manButton);
        group.add(productButton);
        group.add(returnItemButton);
        group.add(salesButton);
        group.add(salesItemButton);
        group.add(storeButton);
        group.clearSelection();

        //Register a listener for the radio buttons.
        belongsToButton.addActionListener(this);
        customerButton.addActionListener(this);
        custSalesButton.addActionListener(this);
        manButton.addActionListener(this);
        productButton.addActionListener(this);
        returnItemButton.addActionListener(this);
        salesButton.addActionListener(this);
        salesItemButton.addActionListener(this);
        storeButton.addActionListener(this);

        //Put the radio buttons in a row in a panel.
        JPanel radioPanel = new JPanel(new GridLayout(1, 0));
        radioPanel.add(belongsToButton);
        radioPanel.add(customerButton);
        radioPanel.add(custSalesButton);
        radioPanel.add(manButton);
        radioPanel.add(productButton);
        radioPanel.add(returnItemButton);
        radioPanel.add(salesButton);
        radioPanel.add(salesItemButton);
        radioPanel.add(storeButton);

        add(radioPanel, BorderLayout.LINE_START);
        // add(picture, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(java.awt.Color.black), "Choose Your Table"));
    }

    /** Listens to the radio buttons. */
    public void actionPerformed(ActionEvent e) {
        updateTable.update(e.getActionCommand());
    }
}
