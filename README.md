# Social Media Application

## Overview 

This is a social media application built using **Spring Boot** for the backend and **Vue.js** (with Quasar Framework) for the frontend. The application allows users to create posts, comment on posts, reply to comments, and manage user interactions.

---

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [License](#license)

## **Introduction**

The **Social Media Application** is a full-stack web application designed to provide users with a platform to share posts, engage in discussions through comments and replies, and interact with other users. The backend is built using **Spring Boot**, providing a robust and scalable REST API, while the frontend is developed with **Vue.js** and the **Quasar Framework**, offering a modern and responsive user interface.

This project demonstrates the integration of a secure authentication system, efficient database management, and a dynamic frontend to create a seamless user experience. It is ideal for learning or building a foundation for a larger social media platform.

## **Features**

- **User Authentication**: Secure login and token-based authentication using JWT.
- **Post Management**: Create, edit, delete, and view posts.
- **Comment System**: Add, edit, delete, and reply to comments.
- **Replies**: Nested replies for comments.
- **Pagination**: Efficient loading of posts and comments with pagination.
- **Role Management**: Admin and user roles for managing content.
- **Database Integration**: MySQL database for storing user, post, and comment data.

---

## **Technologies Used**

### **Backend**

- **Spring Boot**: Backend framework for RESTful APIs.
- **MySQL**: Relational database for storing application data.
- **Hibernate**: ORM for database interaction.
- **JWT**: Token-based authentication.
- **Swagger**: API documentation.

### **Frontend**

- **Vue.js**: Frontend framework for building the user interface.
- **Quasar Framework**: UI framework for Vue.js.
- **Axios**: HTTP client for API communication.

---

## **Project Structure**

### **Backend**

Located in the `src/main/java/personal/social` directory:
- `controllers/`: REST API controllers.
- `services/`: Business logic and service layer.
- `repository/`: Data access layer using Spring Data JPA.
- `model/`: Entity classes for database tables.
- `dto/`: Data Transfer Objects for API communication.
- `config/`: Configuration files (e.g., JWT, database).

### **Frontend**

Located in the `socialUi` directory:
- `src/components/`: Vue components (e.g., `CommentSection.vue`, `EditPostDialog.vue`).
- `src/pages/`: Vue pages (e.g., `IndexPage.vue`).
- `src/services/`: API service layer using Axios.
- `src/assets/`: Static assets (e.g., images, styles).

---

## **Setup Instructions**

### **Backend Setup**
1. Install **Java 17** and **Maven**.

2. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/social-media-app.git
   cd social/src

3. Download and Install MySQL for Windows

   Go to: [Download MySQL](https://dev.mysql.com/downloads/installer/)  
   - Select version: Choose version  
   - Select Operating System: Your system  
   - Configure: [MySQL Installation Guide](https://www.geeksforgeeks.org/how-to-install-mysql-in-windows/)

4. Download and install Intellij

      https://www.jetbrains.com/idea/download/?section=windows
   
5. Open project with intellij
   
6. Run this file: `\src\main\java\personal\social\SocialApplication.java`

### **Frontend Setup**
1. Install Node.js and npm.

2. Navigate to the socialUi directory: 

       cd social/socialUi
      
4. Install dependencies:

        pip install
   
5. Start the development server: 

        quasar dev

6. Open the application in your browser at `http://localhost:9000`.

---

## **License**

This project is licensed under the MIT License. See the LICENSE file for details.

***Contact***

For any questions or issues, please contact: `hoquocviet.na.dio@gmail.com`
