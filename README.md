# The Social Network - Backend

This project is the backend server for a simple Social Network system.

The server is built using **Spring Boot** and connects to a **MySQL database**.

## Features

- User registration
- User login
- Search users
- Follow / Unfollow users
- Create text posts
- Feed of posts from followed users
- Like / Unlike posts
- Delete posts
- Update profile image

## Technologies

- Java
- Spring Boot
- JDBC
- MySQL

## How to Run

### 1. Create Database

Create a MySQL database:

social_network

### 2. Configure Database Connection

Update the database credentials in:

DbUtils.java

Example configuration:

username = root  
password = 1234  
schema = social_network

### 3. Run the Server

Run the file:

Main.java

The server will start on:

http://localhost:8989

## Database Tables

The project uses the following tables:

- users
- follows
- posts
- post_likes

## API Examples

Register:

POST /api/auth/register

Login:

POST /api/auth/login

Create Post:

POST /api/posts/create

Follow User:

POST /api/follows/follow

Like Post:

POST /api/posts/like

Delete Post:

DELETE /api/posts/{postId}