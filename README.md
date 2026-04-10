# Fast Finnish Vocab

A full-stack learning app with focus on backend where users practice Finnish vocabulary by level (A1–C2), with secure authentication, email verification, and admin tooling. Built with Spring Boot 4, Thymeleaf, Spring Security, Spring Data JPA, and PostgreSQL.

## Overview

This project is a Spring Boot web application that lets learners pick a proficiency level and study random words (Finnish, English, and an example sentence). Administrators manage the shared word list. It implements features such as registration, login, email verification, password reset, role-based access (USER/ADMIN), a learn/practice flow, and CRUD for words grouped by level.

The codebase demonstrates development skills:

- **Backend:** Spring Boot, Spring MVC, Spring Data JPA, Spring Security 6  
- **Frontend:** Thymeleaf with Bootstrap styling  
- **Infrastructure:** PostgreSQL, JavaMail (SMTP)  
- **Quality control:** Repository tests, environment-based config  

## Tech Stack

- **Language:** Java 17  
- **Frameworks:** Spring Boot 4, Spring MVC, Spring Security 6, Spring Data JPA  
- **Templating:** Thymeleaf + Bootstrap  
- **Database:** PostgreSQL  
- **Build:** Maven  
- **Mail:** JavaMail (SMTP)  

## Features

- Sign up with email and password  
- Email verification before first login  
- Login / logout with Spring Security 6  
- Forgot password and password reset via emailed token  
- Choose and change Finnish level (A1–C2); learn view shows a random word for that level  
- Create, read, update, delete words (admin); words belong to a level  
- Role-based access: regular users vs admin  
- Password hashing (BCrypt)  
- PostgreSQL for persistence  
- Optional JSON seeding when the app is started with the `seed` program argument  

## Running the App

1. Configure the database by updating `src/main/resources/application.properties` with your PostgreSQL URL, username, and password.  
2. Configure email if you want to test verification and reset emails (or use options like logging links in dev, if configured).  
3. Run the app with `mvn spring-boot:run`.  
