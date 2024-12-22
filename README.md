# Car Management System API

This repository contains the Car Management System API, which provides endpoints for managing cars, maintenance, and other related operations. 

## Features

- Manage cars and related information.
- Handle maintenance records.

## Getting Started

### Prerequisites

- [Node.js](https://nodejs.org/)

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/ErayAli6/car-management-system-api.git
    cd car-management-system-api
    ```

2. Install dependencies:
    ```bash
    npm install
    ```

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
    npm install
    ```

3. Run the frontend application:
    ```bash
    npm start
    ```

4. The frontend should now be running at `http://localhost:3000`.
