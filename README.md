# Library Management System

## Deployment status ☸️🚀:

[![Test pipeline](https://github.com/Abrohamovich/library/actions/workflows/test-pipeline.yaml)](https://github.com/abrohamovich/library/actions/workflows/test-pipeline.yaml)

## Overview

The **Library Management System** is a Java-based application designed to manage library operations, including books,
genres, categories, authors, patrons, and book lending processes. It provides a robust backend with a modular
architecture and a user-friendly graphical interface built using **JavaFX**. The system is designed to handle library
inventory (books as works and physical instances), patron registration, and book borrowing/returning workflows.

This project does **not** use the Spring Framework, relying instead on a custom architecture with **Hibernate** for ORM,
**MapStruct** for object mapping, and **JavaFX** for the GUI. It supports both **PostgreSQL** for production and **H2**
for testing.

## Features

- **Book Management**:
    - Manage physical book instances (`Book`) with attributes like title, ISBN, authors, genres, categories, publisher,
      page count, receipt date, status,
      and format.
- **Author Management**:
    - Store author details including name, date of birth, sex, and nationality.
- **Genre and Category Management**:
    - Organize books by genres and categories, each with unique names and descriptions.
- **Patron Management**:
    - Register library members (`Patron`) with details like card ID, contact information, and borrowing history.
    - Support book borrowing and returning with validation to prevent duplicate book instance assignments.
- **Publisher Management**:
    - Track publisher information including name, foundation date, address, email, and website.
- **Graphical User Interface**:
    - Built with **JavaFX** for an intuitive and responsive user experience.
- **Data Persistence**:
    - Uses **Hibernate** for ORM to interact with **PostgreSQL** (production) and **H2** (testing).
- **Object Mapping**:
    - Utilizes **MapStruct** for efficient mapping between entities and DTOs.
- **Testing**:
    - Unit tests with **JUnit 5**.
    - Code coverage reporting with **JaCoCo**.

## Project Structure

The project follows a clean, modular architecture:

- **Entities**: JPA-annotated classes (`Author`, `Book`, `Category`, `Genre`, `Patron`, `Publisher`)
  representing the data model.
- **Repositories**: Interfaces and implementations for data access using Hibernate.
- **Services**: Business logic for managing entities, including CRUD operations and book lending workflows.
- **DTOs**: Data Transfer Objects for communication between layers.
- **Mappers**: **MapStruct**-based mappers to convert between entities and DTOs.
- **JavaFX GUI**: Frontend components for user interaction, including views for managing books, patrons, and lending
  operations.

## Technologies

- **Java**: Version 23
- **Hibernate**: 6.6.13.Final for ORM
- **MapStruct**: 1.6.3 for object mapping
- **JavaFX**: For the graphical user interface
- **PostgreSQL**: 42.7.5 for production database
- **H2**: 2.3.232 for testing
- **JUnit 5**: 5.12.1 for unit testing
- **JaCoCo**: 0.8.12 for code coverage
- **Lombok**: 1.18.36 for reducing boilerplate code
- **Maven**: Build and dependency management

## Prerequisites

- **Java 23** JDK
- **Maven** 3.9.8 or higher
- **PostgreSQL** database (for production)
- **JavaFX** SDK (for GUI development and execution)

## Usage

- **Add Books and Instances**: Create book entries with authors, genres, and categories, and add physical book instances
  with publisher and status details.
- **Manage Patrons**: Register new patrons and assign unique card IDs.
- **Lend Books**: Assign book instances to patrons, ensuring no duplicate assignments.
- **View Reports**: Use the JavaFX interface to view book inventory, patron borrowing history, and more.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

For questions or feedback, please contact the project maintainer at \[herasymov.mykyta@gmail.com\].