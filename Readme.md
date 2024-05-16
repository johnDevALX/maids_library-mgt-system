# Library Management System API

This is a RESTful API built with Spring Boot for managing a library system. It allows librarians to manage books, patrons, and borrowing records.

## Features

- **Book Management**:
- Get a list of all books
- Get details of a specific book by ID
- Add a new book
- Update an existing book
- Remove a book
- **Patron Management**:
- Get a list of all patrons
- Get details of a specific patron by ID
- Add a new patron
- Update an existing patron
- Remove a patron
- **Borrowing Management**:
- Allow a patron to borrow a book
- Record the return of a borrowed book by a patron

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- JSON Web Token (JWT) for authentication and authorization
- Redis for caching

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21
- PostgreSQL installed and running
- Redis installed and running

### Installation

1. Clone the repository:

```bash
git clone https://github.com/johnDevALX/maids_library-mgt-system.git

2. Navigation, building running the application:

cd maids_library-mgt-system
mvn clean install
java -jar target/library-management-system-0.0.1-SNAPSHOT.jar
