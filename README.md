# Fast Finnish Vocab

Fast Finnish Vocab is a web application for practicing Finnish vocabulary. Learners work through words matched to a proficiency level (A1 through C2). Administrators maintain the word list in the same app.

- Web UI built with Spring MVC and Thymeleaf (Bootstrap).
- Users can register, verify email, sign in, reset password, choose or change their level, and open a learn view that shows a random word for that level (Finnish, English, example).
- Admins can list, add, edit, and delete words and assign them to levels.
- Data is stored in PostgreSQL via Spring Data JPA.
- Access is protected with Spring Security (roles for regular users and admins).
- Optional database seeding from JSON when the app is started with the `seed` argument.
- Build with Maven; runnable as a Spring Boot JAR.
  