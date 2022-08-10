##Station Inventory Manager (SIM)

**Group 7** 
Summer B 2022 

A inventory manager for gas station owners.  The Station Inventory Manager (SIM) 
saves related data about products, sales, customers, and suppliers for the station.

To run Station Inventory Manager,
open command prompt and navigate to the directory

run
    javac -d classes src/proj4/group7/*.java
to compile

and

run the command
    java -cp classes;lib/mysql_connector.jar proj4.group7.DatabaseDemo


If your username='james', password='smith', database name = 'testingDB', you will enter the following on the JTextFields in the Login Panel

Username: james

Password: smith

Database: testingDB (or whatever the name of your database is)

Driver: com.mysql.jdbc.Driver (or whatever the name of your mysql driver is)

The lib/ folder already contains a jar file (mysql_connector.jar, the same one from class. I just renamed it)

**Note:**

The `testingDB` database must already exist, and you should have run the files `sql/project-3_group-7_create.sql` and `sql/project-3_group-7_insert.sql` to populate the database with the needed data

**Inserting**

To insert a tuple in a row, simply go the last empty row, and edit the cells. Once the last cell is inputted, the tuple will be automatically added.

**Updating**

Simply edit a cell to update. For simplicity, I disabled editing of the Primary Key cells. To simulate such effect, simply delete the tuple and re-enter the values on the last row. Note that deleting a tuple might cause others to be deleted in other tables because of cascading.

**Deleting**

Choose a row and then click on the delete button
