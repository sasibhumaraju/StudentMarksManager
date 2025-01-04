# Student Marks Manager

## Description
The Student Marks Manager is a Console Based Java-based application designed to manage student marks data. It allows users to load student marks from a JSON file into a database, extract the data into a CSV file, and delete all student records from the database. The application leverages various Java techniques such as I/O operations, JDBC for database interactions, JSON parsing, and multithreading.

## Project Structure
- **src/**: Contains all the Java source files.
  - **App.java**: The main entry point of the application.
  - **StudentsData.java**: Contains the `StudentsData` class and the nested `Student` class.
  - **JdbcDB.java**: Handles database operations.
  - **JsonToCSVService.java**: Provides services to load JSON data into the database and extract data to a CSV file.
  - **JsonToCSVServiceV2.java**: Extends `JsonToCSVService` to provide functionality for loading multiple JSON files.
- **resources/**: Contains resource files.
  - **students.json**: Sample JSON file with student data.

## Functionalities
1. **Load JSON File**: Load student marks data from a JSON file into the database.
2. **Multi JSON Load**: Load student marks data from multiple JSON files into the database.
3. **Get CSV File**: Extract student marks data from the database and save it as a CSV file.
4. **Delete All Students in DB**: Delete all student records from the database.
5. **Exit**: Exit the application.

## How to Use
1. Run the application.
2. Follow the on-screen instructions to select an option:
   - **Load JSON File**: Provide the path to the JSON file when prompted.
   - **Multi JSON Load**: Provide the number of JSON files and their paths when prompted.
   - **Get CSV File**: The CSV file will be saved in your home directory.
   - **Delete All Students in DB**: All student records will be deleted from the database.
   - **Exit**: Exit the application.

## Java Techniques Used
- **Java I/O**: Reading from and writing to files using `BufferedReader`, `BufferedWriter`, `FileReader`, and `FileWriter`.
- **JDBC**: Connecting to and interacting with a database using `DriverManager`, `Connection`, `PreparedStatement`, `Statement`, and `ResultSet`.
- **JSON Parsing**: Parsing JSON data using the `Gson` library.
- **Multithreading**: Using `ExecutorService` to handle parallel database updates.
- **Optional**: Handling optional values using the `Optional` class.
- **Inner Classes**: Using an inner class (`Student`) within the `StudentsData` class.

## Example JSON File Structure
```json
{
  "students": [
    {
      "id": 1,
      "name": "Alice",
      "marks": 85
    },
    {
      "id": 2,
      "name": "Bob",
      "marks": 92
    }
    // ... more students ...
  ]
}
```

## Notes
- Ensure the database connection details are correctly set in the environment variables (`DB_URL`, `UNAME`, `PASS`).
- The application uses the `dotenv` library to load environment variables from a `.env` file.
