
# Online Travel System (OTS)

## 📖 Project Overview

The **Online Travel System** is a robust, full-stack web application designed to manage various aspects of the tourism industry. It facilitates interactions between users, travel suppliers, and administrators. The system covers a wide range of functionalities including attraction browsing, ticket booking, hotel reservations, travel strategy sharing, and supplier management.

This project adopts a **Front-end and Back-end Separation** architecture, ensuring scalability and maintainability. It is suitable for learning enterprise-level development patterns involving complex database designs and business logic.

-----

## 🛠 Technology Stack

### Backend

  * **Framework:** Spring Boot
  * **ORM:** MyBatis
  * **MVC Framework:** Spring MVC
  * **Language:** Java (JDK 1.8+)

### Frontend

  * **Framework:** Vue.js
  * **Routing:** Vue Router
  * **HTTP Client:** Axios
  * **UI/Languages:** HTML, CSS, JavaScript

### Database

  * **Database:** MySQL (Version 5.7 or 8.0)
  * **Tools:** Navicat (Recommended for management)

-----

## ✨ Core Features

The system is built around the keyword "**Travel**" and includes approximately 15 database tables handling the following modules:

### 1\. User Module

  * User Registration & Login.
  * Personal Center & Information Management.
  * Comments and Reviews system.

### 2\. Attraction & Ticket Module

  * **Attraction Management:** Name, category, cover image, location, status.
  * **Ticket Management:** Ticket details, pricing, discounts, inventory numbers.
  * **Booking System:** Ticket purchasing and order generation (`scenic_ticket_order`).
  * **Ratings & Reviews:** User ratings for attractions.

### 3\. Hotel Module

  * **Hotel Info:** Name, address, cover image, contact info.
  * **Room Management:** Room types, pricing, discounts, descriptions.
  * **Bed Management:** Specific bed status tracking within rooms.
  * **Reservations:** Hotel booking orders and payment status tracking.

### 4\. Travel Strategy (Community)

  * **Travel Guides:** Users can publish travel strategies/guides including titles, content, and cover images.
  * **Review Process:** Admin audit system for published strategies.

### 5\. Supplier/Provider Module

  * **Supplier Management:** Manage service providers with contact details, verification status, and product types.

### 6\. System Management

  * **Announcements:** System-wide notifications.
  * **Route Management:** Defined travel routes and hierarchies.
  * **Attraction Categories:** Management of attraction types.

-----

## 💾 Database Design

The project utilizes a relational database named **`online-travel`**. It involves complex relationships including Foreign Keys, Joins (Left/Right/Inner), and Sub-queries.

**Key Entities Include:**

  * `user` (Users)
  * `scenic` (Attractions)
  * `scenic_ticket` (Tickets)
  * `scenic_ticket_order` (Ticket Orders)
  * `hotel` (Hotels)
  * `hotel_room` (Rooms)
  * `hotel_order` (Hotel Orders)
  * `strategy` (Travel Guides)
  * `supplier` (Service Providers)

-----

## ⚙️ Environment Setup & Prerequisites

Before running the project, ensure your environment meets the following requirements:

### Development Tools

  * **Backend IDE:** IntelliJ IDEA (Ultimate Edition recommended).
  * **Frontend IDE:** Visual Studio Code.
  * **Database Tool:** Navicat or similar MySQL client.

### Environment Dependencies

1.  **Java:** JDK 1.8 or higher.
2.  **Maven:** Version 3.6 or higher.
3.  **Node.js:** Required for the Vue frontend.
4.  **MySQL:** Version 5.7 or 8.0.

-----

## 🚀 Installation & Execution

### 1\. Database Setup

1.  Open your MySQL client (e.g., Navicat).
2.  Create a new database named **`online-travel`** with `utf-8` encoding.
3.  Import the SQL script provided in the project `sql` folder to initialize tables and data (e.g., Users, Comments, etc.).

### 2\. Backend Setup

1.  Open the backend project in **IntelliJ IDEA**.
2.  Wait for Maven to download all dependencies.
3.  Locate the configuration file (usually `application.yml` or `application.properties`).
4.  Update the **MySQL connection settings** (URL, username, and password) to match your local environment.
5.  Run the main application class to start the Spring Boot server.

### 3\. Frontend Setup

1.  Open the frontend project folder in **Visual Studio Code**.
2.  Open the terminal and install dependencies:
    ```bash
    npm install
    ```
3.  Start the development server:
    ```bash
    npm run serve
    ```
4.  Access the application via the browser (usually at `http://localhost:8080` or the port specified in the terminal).

-----

## 📝 Notes

  * If using MySQL 8.0, ensure the authentication plugin is compatible or update your root password configuration if connection issues arise.
  * This project is designed as a comprehensive tutorial system involving substantial business logic and UI design.
