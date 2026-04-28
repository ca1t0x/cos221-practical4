**COS 221 - PRACTICAL 4 README

Student 1: Caitlin Moodley(u25128443)
Student 2: Kiara Ajodhaparsadh (u25395344)
Database:  u25395344_u25128443_chinook
**
----------------------------------------------------------------
**GITHUB REPOSITORY**

https://github.com/funkypigeon-cyber/cos221-practical4

**----------------------------------------------------------------**
DATABASE CONNECTION SETUP

The application reads credentials from environment variables.
Set these before running:

   export CHINOOK_DB_PROTO=jdbc:mariadb
   export CHINOOK_DB_HOST=172.25.48.1
   export CHINOOK_DB_PORT=3306
   export CHINOOK_DB_NAME=u25395344_u25128443_chinook
   export CHINOOK_DB_USERNAME=root
   export CHINOOK_DB_PASSWORD=th@th@15
**----------------------------------------------------------------**
**HOW TO BUILD AND RUN**
1. Open a terminal in the project root directory.
2. Navigate to the Maven project:

   cd ChinookApp

3. Compile and execute the application:

   mvn exec:java -Dexec.mainClass="Main"
