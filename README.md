# cos221-practical4
# A readme.txt file explaining how to build the project, connect the application to the database, and run the
# application.

Student 1 Name: Caitlin
Student 1 Number: 25128443

Student 2 Name: Kiara Ajodhaparsadh
Student 2 Number: 25395344

BUILDING THE PROJECT

1. Open a terminal in the project root directory.
2. Navigate to the Maven project:
   cd chinook-app

3. Compile the project using Maven:
   mvn compile

----------------------------------------

HOW TO RUN THE PROGRAM:

1. Open terminal in the project directory:
   cd chinook-app

2. Compile the project:
   mvn compile

3. Run the application:
   mvn exec:java

----------------------------------------

DATABASE CONNECTION SETUP:

1. Ensure MySQL is installed and running.

2. Create a database named:
   u25395344_u25128443_chinook

3. Import the Chinook SQL file into MySQL.

4. Open the file:
   src/main/java/com/mycompany/database/DatabaseConnection.java

5. Add the database connection details:

# EDIT THESE
String url = "jdbc:mysql://localhost:3306/chinook";
String user = "root";
String password = "your_password";

6. Save the file. 

----------------------------------------

RUNNING THE APPLICATION:

1. Open a terminal in the chinook-app folder:
   cd chinook-app

2. Run the application using Maven:
   mvn exec:java

3. The GUI application will open.

----------------------------------------
                                                       
FEATURES IMPLEMENTED:

TASK 4.1 – 4.4 (CAITLIN) 
# EXPAND
- Employee management
- Track management
- Basic GUI structure

TASK 4.5 – CUSTOMER CRUD (KIARA)
- Add customer
- Update customer
- Delete customer
- View customers in JTable

TASK 4.6 – INACTIVE CUSTOMERS
- Identifies customers who have not made purchases recently
- Uses SQL with JOIN and date filtering
- Displays results in table

TASK 4.7 – RECOMMENDATIONS
- Suggests tracks based on customer purchase history
- Uses SQL aggregation and subqueries


TASK 5 – REPORTING

Implemented SQL-based reporting:

Revenue per Genre:
- Calculates total revenue per genre
- Uses JOIN, GROUP BY, and SUM


SQL QUERIES USED:

Revenue per Genre:
SELECT g.Name AS Genre, SUM(il.UnitPrice * il.Quantity) AS Revenue
FROM InvoiceLine il
JOIN Track t ON il.TrackId = t.TrackId
JOIN Genre g ON t.GenreId = g.GenreId
GROUP BY g.Name
ORDER BY Revenue DESC;

----------------------------------------

CONTRIBUTION SPLIT:

TOGETHER:
TASK 1-3

CAITLIN:
- Tasks 4.1 – 4.4
- Task 6
- TESTING
- Maven setup and integration
- README and PDF

KIARA:
- Tasks 4.5 – 4.7
- Task 5 (Reports)
- Task 6
- Maven setup and integration
- README and PDF

GITHUB REPOSITORY:

# [Paste repo link here]
