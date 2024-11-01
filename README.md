# Restaurant Management System

*A Java-based application for managing restaurant orders and menus, allowing users to place orders, view order history, and manage menu items. Data is stored in an Excel file (restaurant_data.xlsx) using Apache POI for file manipulation, making the system accessible and easy to modify.*

## Features

#### Order Management:
       Create, view, and store orders with itemized details.

#### Menu Management:
       Manage restaurant menu items and their pricing.

#### Excel-Based Data Storage: 
       Stores menu and order data in restaurant_data.xlsx for easy access and flexibility.

#### Error Logging: 
       Logs errors to error_log.txt for debugging and tracking issues.

## Table of Contents

##### 1.Prerequisites

##### 2.Setup

##### 3.Running the Project

##### 4.Project Structure

##### 5.File Structure in restaurant_data.xlsx

##### 6.Usage

##### 7.Testing

##### 8.Troubleshooting

##### 9.Contributing

##### 10.License

## 1.Prerequisites

Ensure you have the following installed on your system:

  Java JDK: Version 8 or later. Download from Oracle.

  Apache Maven: To manage dependencies and build the project. Install from Maven’s official website.

  Apache POI Libraries: Included in the pom.xml and managed by Maven.


## 2.Setup

##### (i). Clone the Repository
    Clone the repository to your local machine:

    *bash
        git clone <repository-url>
        cd    restaurant-management-system
*
##### 2. Install Dependencies

Navigate to the project directory and install the dependencies specified in pom.xml:

  *bash
      mvn clean install*
      
## 3. Ensure the Excel File Structure

*Create or ensure the existence of restaurant_data.xlsx in src/main/resources with the following sheets and columns.*

Sheet Structure for restaurant_data.xlsx
Menu:

Stores the restaurant's menu items.
Columns:
Dish ID (e.g., 1)
Dish Name (e.g., Pizza)
Price (e.g., 10.00)
Description (optional, e.g., Cheese Pizza with toppings)
Example:

Dish ID	Dish Name	Price	Description
1	Pizza	10.00	Cheese Pizza
2	Burger	8.00	Double Cheeseburger
3	Salad	5.00	Fresh Garden Salad
Orders:

Stores information about each order.
Columns:
Order ID (e.g., 1001)
Table Number (e.g., 5)
Total Price (e.g., 25.00)
Date (e.g., 2024-01-01 13:45:00)
Example:

Order ID	Table Number	Total Price	Date
1001	5	25.00	2024-01-01 13:45:00
Order_Items:

Stores detailed information for each item within an order.
Columns:
Order ID (links to Orders sheet, e.g., 1001)
Dish ID (links to Menu sheet, e.g., 1)
Quantity (e.g., 2)
Item Total (calculated as Price * Quantity)
Example:

Order ID	Dish ID	Quantity	Item Total
1001	1	2	20.00
1001	2	1	8.00
Running the Project
To run the project, use Maven’s exec:java command:

bash
Copy code
mvn exec:java -Dexec.mainClass="com.example.restaurant.RestaurantManagementSystem"
This command will start the GUI application.

Project Structure
Here’s the structure of the project:

bash
Copy code
restaurant-management-system/
├── pom.xml                         # Maven project configuration file
├── README.md                       # Project documentation
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── restaurant/
│   │   │               └── RestaurantManagementSystem.java # Main application code
│   │   └── resources/
│   │       └── restaurant_data.xlsx # Excel file for data storage
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── restaurant/
│                       └── RestaurantManagementSystemTest.java # Test file
Usage
1. Place an Order
Enter the table number and select items from the menu.
Click Place Order to submit the order. The system will calculate the total and save the order details to restaurant_data.xlsx.
2. Cancel an Order
If you need to cancel an order, click Cancel Order before confirming. This will reset the current order.
3. View Order History
Order history is saved in restaurant_data.xlsx under the Orders sheet. You can view or modify orders directly within this file.
4. Modify the Menu
To add or update menu items, modify the Menu sheet in restaurant_data.xlsx. Add rows with new items or edit existing items.
Testing
Running Tests
Tests for RestaurantManagementSystem are located in RestaurantManagementSystemTest.java. Run tests using Maven:

bash
Copy code
mvn test
Test Cases
The test suite includes:

File Creation Tests: Ensures restaurant_data.xlsx and error_log.txt are created if they don’t exist.
Order Functionality Tests: Verifies that totals and summaries are calculated correctly.
Error Logging Tests: Checks that errors are logged properly to error_log.txt.
Troubleshooting
File Locking Issue on restaurant_data.xlsx:

Ensure restaurant_data.xlsx is not open in any other application (like Excel).
Close any program that might be using the file and try again.
Dependency Issues:

If dependencies are not resolved, run mvn clean install to force Maven to re-download dependencies.
Maven Build Errors:

If Maven reports errors, try deleting the target directory and running mvn clean install again.
Contributing
Contributions are welcome! Here’s how you can contribute:

Fork the repository.
Create a feature branch:
bash
Copy code
git checkout -b feature-name
Commit your changes and push the branch:
bash
Copy code
git commit -m "Add new feature"
git push origin feature-name
Open a pull request, and describe your changes in detail.
License
This project is licensed under the MIT License - see the LICENSE file for details.
