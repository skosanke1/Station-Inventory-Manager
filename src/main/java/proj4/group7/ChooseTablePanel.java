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

/*
 * This Component allows us to choose our Tables
 */
public class ChooseTablePanel extends JPanel implements ActionListener {
    private final static String mBelongsToStr = "belongs_to";
    private final static String mCustomerStr = "customer";
    private final static String mCustSalesStr = "cust_sales";
    private final static String mManStr = "manufacturer";
    private final static String mProductStr = "product";
    private final static String mReturnItemStr = "return_item";
    private final static String mSalesStr = "sales";
    private final static String mSalesItemStr = "sales_item";
    private final static String mStoreStr = "store";    
    
    private IUpdateTableListener updateTableListener;

    public ChooseTablePanel(IUpdateTableListener updateTableListener) {
        this.updateTableListener = updateTableListener;
        
        //Create the radio buttons.
        JRadioButton lBelongsToButton = new JRadioButton(mBelongsToStr);
        lBelongsToButton.setMnemonic(KeyEvent.VK_B);
        lBelongsToButton.setActionCommand(mBelongsToStr);
        lBelongsToButton.setSelected(true);

        JRadioButton lCustomerButton = new JRadioButton(mCustomerStr);
        lCustomerButton.setMnemonic(KeyEvent.VK_C);
        lCustomerButton.setActionCommand(mCustomerStr);

        JRadioButton lCustSalesButton = new JRadioButton(mCustSalesStr);
        lCustSalesButton.setMnemonic(KeyEvent.VK_U);
        lCustSalesButton.setActionCommand(mCustSalesStr);

        JRadioButton lManButton = new JRadioButton(mManStr);
        lManButton.setMnemonic(KeyEvent.VK_M);
        lManButton.setActionCommand(mManStr);

        JRadioButton lProductButton = new JRadioButton(mProductStr);
        lProductButton.setMnemonic(KeyEvent.VK_P);
        lProductButton.setActionCommand(mProductStr);

        JRadioButton lReturnItemButton = new JRadioButton(mReturnItemStr);
        lReturnItemButton.setMnemonic(KeyEvent.VK_R);
        lReturnItemButton.setActionCommand(mReturnItemStr);

        JRadioButton lSalesButton = new JRadioButton(mSalesStr);
        lSalesButton.setMnemonic(KeyEvent.VK_S);
        lSalesButton.setActionCommand(mSalesStr);

        JRadioButton lSalesItemButton = new JRadioButton(mSalesItemStr);
        lSalesItemButton.setMnemonic(KeyEvent.VK_A);
        lSalesItemButton.setActionCommand(mSalesItemStr);

        JRadioButton lStoreButton = new JRadioButton(mStoreStr);
        lStoreButton.setMnemonic(KeyEvent.VK_T);
        lStoreButton.setActionCommand(mStoreStr);

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(lBelongsToButton);
        group.add(lCustomerButton);
        group.add(lCustSalesButton);
        group.add(lManButton);
        group.add(lProductButton);
        group.add(lReturnItemButton);
        group.add(lSalesButton);
        group.add(lSalesItemButton);
        group.add(lStoreButton);
        group.clearSelection();

        //Register a listener for the radio buttons.
        lBelongsToButton.addActionListener(this);
        lCustomerButton.addActionListener(this);
        lCustSalesButton.addActionListener(this);
        lManButton.addActionListener(this);
        lProductButton.addActionListener(this);
        lReturnItemButton.addActionListener(this);
        lSalesButton.addActionListener(this);
        lSalesItemButton.addActionListener(this);
        lStoreButton.addActionListener(this);

        //Put the radio buttons in a row in a panel.
        JPanel radioPanel = new JPanel(new GridLayout(1, 0));
        radioPanel.add(lBelongsToButton);
        radioPanel.add(lCustomerButton);
        radioPanel.add(lCustSalesButton);
        radioPanel.add(lManButton);
        radioPanel.add(lProductButton);
        radioPanel.add(lReturnItemButton);
        radioPanel.add(lSalesButton);
        radioPanel.add(lSalesItemButton);
        radioPanel.add(lStoreButton);

        add(radioPanel, BorderLayout.LINE_START);
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(java.awt.Color.black),
            "Choose Your Table"));
    }

    /** Listens to the radio buttons. */
    public void actionPerformed(ActionEvent e) {
        updateTableListener.update(e.getActionCommand());
    }
}
