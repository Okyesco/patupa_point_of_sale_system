# PaTuPa Point-of-Sale-System
This Point of Sale System is aimed for small convenience stores which are still unfamiliar to computerized system of running the business. This system is simple yet effective as the functions are easy to use for the users, that is, for the store owner or store manager and store cashiers.

In the admin panel, there are fields which are effective for the administration of the whole store. The admin can manage list of product items, product categories, cashier information, supplier information, view sale charts and generate reports.

In the cashier panel, the user will be provided with interfaces to sell items to customers. He/she can search the selling items by barcode or item-name.

This project is implemented with Model–view–controller (MVC) software design pattern.

For enduser use, check out the enduserguide.pdf file. 
Database .sql file is also included in the directory.

<hr>

# Libraries/Dependencies Used
- Java FX 22.0.1
- Jaspersoft iReport
- mySQL JDBC
- JDK 22.0.1
- IntelliJ IDEA

<hr>

# Features 
- User authentication and authorization (admin, cashier)
- Stock management (add/delete/modify inventory item)
- Supplier management (company info, supply date, supply amount)
- Track popular item over customer interest (i.e. Most selling item)
- Report generation
- Cashier records (i.e. Workers)

<hr>

# Setup
1. Download the project source code.
2. Import the Database.sql file to the database and run.
3. Create a database user with the following credentials and give all database privileges.
```
username: admin
password: admin
```
4. Import the source code project to the IDE. (I used IntelliJ IDEA to develop this project.)
5. Import the Jar library files to the project build path (i.e. Can be found in [resources/library] directory ).
6. Update the pom.xml to get the jasper report plugin. Install Jasper iReport (https://community.jaspersoft.com/project/ireport-designer) library. 
7. If Database connection is not establish, run the following command into your SQL console:
```
mysql> set @@global.show_compatibility_56=ON;
```
8. Admin username and password: username: admin; pw: admin .
9. Now, you're good to go!!! Modify it for your own use case. 
10. You can also run the PaTuPa POS.jar file to run the app.
<hr>

# Conclusion
This system is user-friendly and reliable computer based standalone system for mini-convenience stores. It has been designed to manage the whole store’s information and general reports (daily, monthly, popular items). It is capable of managing product items, product categories, cashier information, customer information, card information and supplier information. It is also available for viewing popular items and sale charts. The developed system provides solution to the manual convenience stores’ problems and so provides special functions such as viewing sale charts. The software offers stability, cost-effectiveness and usability. It provides the most flexible and adaptable standard management system solutions for convenience stores.


