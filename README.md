# Organization Analyzer

The **Organization Analyzer** is a Java-based application designed to process organizational hierarchy data from CSV files. It analyzes employee records, validates salaries, and generates reports on organizational structure and salary discrepancies.

## Features

- Parse and validate organizational hierarchy from CSV files.
- Detect salary discrepancies for managers based on their subordinates average salaries.
- Identify excessively long reporting lines.
- Generate detailed analysis reports.
- Handle invalid or missing data gracefully with meaningful error messages.

## Project Structure

## Prerequisites

- Java 17 or higher
- Maven 3.8 or higher

## Dependencies

The project uses the following dependencies:

- [OpenCSV](https://opencsv.sourceforge.net/) for CSV parsing.
- [JUnit 5](https://junit.org/junit5/) for unit testing.

## Getting Started

### Run Tests

Run the unit tests using Maven:

```sh
mvn test
```

### Build and Package

Package the application into a JAR file with dependencies:

```sh
mvn clean package
```

### Run the Application

Run the application with a CSV file as input:java -jar

```sh
java -jar target/organization-analyzer-1.0.0-jar-with-dependencies.jar <path-to-csv-file>
```
