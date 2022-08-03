package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Client application that is used to access the Station Inventory Manager database
 * 
 * @author Steven Kosanke, Group 7
 * @version 1.0
 */
public class Client { 
    static Scanner scan = new Scanner(System.in);
    static String url, user, pwd; //Connection credentials, driver must also be installed on OS.
    public static void main(String[] args) {
        int start = startup();
        if (start == 0) {
            menu();
        } else {
            while (start != 0) 
                start = startup();          
        }
    }
    
    /**
     * Used to initialize URL, username, password used to connect to database
     *
     * @return 0 if successful connection
     * @return -1 if conection fails due to url, user, pwd combo
     * @return -2 if SQLException occurs
     */
    public static int startup() {       
        System.out.println("URL: ");
        url = scan.nextLine();
        System.out.println();
        System.out.println("user: ");
        user = scan.nextLine();
        System.out.println();
        System.out.println("pwd: ");
        pwd = scan.nextLine();
        System.out.println("Connecting to " + url );
        System.out.println("Username :" + user);
        Connection connect;
        //Attempt to connect to Server
        try {
            connect = DriverManager.getConnection(url, user, pwd);
            if (connect != null) {
                System.out.println("Connection successful");
                connect.close();
            } else {
                System.out.println("Unable to connect with credentials.  Please try again.");
                scan.close();
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error occured during connection.");
            e.printStackTrace();
            //return -2; Remove when connection to server is established
        }
        return 0;
    }
    
    /**
     * Main menu which loops indefinitely, used to query the database
     */
    public static void menu() {
        System.out.println(System.lineSeparator().repeat(40));
        System.out.println("Please choose an option");
        System.out.println("1- Product list by supplier");
        System.out.println("2- Second query");
        System.out.println("3- Third query");
        System.out.println("q- Close application");
        String command = scan.nextLine();
        
        if (command.equals("q")) {
            scan.close();
            return;
        } else if (command.equals("1")) {
            query1();
        } else if (command.equals("2")) {
            query2();
        } else if (command.equals("3")) {
            query2();
        } else {
            System.out.println("Invalid query, please choose 1,2,3");
            menu();
        }
    }
    
    /**
     * List all products from a specified supplier
     */
    public static void query1 () {
        try {
            System.out.println("Please type in the supplier name:");
            String name = scan.nextLine();
            //Connect to server, and execute query
            Connection connect = DriverManager.getConnection(url, user, pwd);
            PreparedStatement query = connect.prepareStatement("SELECT ProductName FROM MANUFACTURER NATURAL JOIN PRODUCT where MName = ?");
            query.setString(1, name);
            ResultSet rs = query.executeQuery();
            connect.close();

            //Display result
            System.out.println("Name of product");
            while (rs.next()) {
                System.out.format("%32s", rs.getString(1));
            }System.out.println("End of list");
        } catch (SQLException e) {
                System.out.println("SQL Exception- Unable to access server");
                e.printStackTrace();
        }

        System.out.println("Please type enter to return to main menu");
        String name = scan.nextLine();
        menu(); //return to main menu
    }
    
    /**
     * Example method for a query
     */
    public static void query2 () {
        try {
            System.out.println("Please type input parameters:");
            String name = scan.nextLine();
            Connection connect = DriverManager.getConnection(url, user, pwd);
            
            //Execute query
            //PreparedStatement query = connect.prepareStatement("SELECT something FROM something");
            //ResultSet rs = query.executeQuery();
            connect.close();
            
            //Display result
            System.out.println("Results:");
            
        } catch (SQLException e) {
                System.out.println("SQL Exception- SQL data unavailable");
                e.printStackTrace();
        }
        menu(); //return to main menu
    }
}
