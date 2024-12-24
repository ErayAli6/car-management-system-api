# Car Management System API

This repository contains the Car Management System API, which provides endpoints for managing cars, maintenance, and other related operations. 

## Features

- Manage cars and related information.
- Handle maintenance records.

## Getting Started

### Prerequisites

- [Node.js](https://nodejs.org/)
- [Java 21](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/ErayAli6/car-management-system-api.git
    cd car-management-system-api
    ```

### How to Run the Program

There are two ways to run the application:

#### Option 1: Using Maven Commands
1. **Run the Maven build**:
    ```bash
    mvn clean install
    ```
   This will compile the code and package the application into a `.jar` file.

2. **Run the packaged application**:
   Navigate to the `target` folder:
    ```bash
    cd target
    ```
   Run the JAR file:
    ```bash
    java -jar *.jar
    ```
   The application should now be running and accessible.

---

#### Option 2: Using IntelliJ IDEA with Maven Delegation
1. Open IntelliJ IDEA.
2. Go to `File` > `Settings` > `Build, Execution, Deployment` > `Build Tools` > `Maven` > `Runner`.
3. Enable the option **"Delegate IDE build/run actions to Maven"**.
4. Run the application directly using the IntelliJ IDEA run configuration.

The application should now be running and accessible.

## API Endpoints

Here are some of the key endpoints provided by the API:

- `GET /cars`: Retrieve a list of cars.
- `POST /cars`: Add a new car.
- `GET /cars/{id}`: Retrieve a specific car by ID.
- `PUT /cars/{id}`: Update a car by ID.
- `DELETE /cars/{id}`: Delete a car by ID.
- `GET /maintenance/{id}`: Retrieve maintenance record by ID.

For a full list of endpoints, please refer to the OpenAPI documentation.

## OpenAPI Documentation

The OpenAPI documentation for this API can be found in the `docs` folder:

- [OpenAPI definition (PDF)](docs/CarManagementApi%20-%20OpenAPI%20definition.pdf)
- [OpenAPI definition (JSON)](docs/car-management-api-docs.json)

You can use an OpenAPI viewer to visualize and interact with the API documentation.

## Frontend

The frontend for this project is located in the `docs/car-management-frontend` folder. To set up and run the frontend:

1. Navigate to the frontend directory:
    ```bash
    cd docs/car-management-frontend
    ```

2. Install dependencies:
    ```bash
    npm install serve
    ```

3. Run the frontend application:
    ```bash
    npx serve
    ```

4. The frontend should now be running at `http://localhost:3000`.
