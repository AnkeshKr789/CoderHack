# 🏆 CoderHack Leaderboard REST API

A Spring Boot-based RESTful API to manage a **Leaderboard** for a coding platform contest. It supports user registration, score updates, badge awards, and leaderboard retrieval. MongoDB is used to persist the data.

---

## 📚 Features

* Register users for a contest
* Update user scores
* Automatically assign badges based on scores
* Retrieve all users sorted by score (descending)
* View specific user details
* Delete a user
* Includes basic validation and error handling
* JUnit test coverage for main functionalities

---

## 📑 User Model

```json
{
  "userId": "string",
  "username": "string",
  "score": 0-100,
  "badges": ["Code Ninja", "Code Champ", "Code Master"]
}
```

### 🌟 Badge Criteria

| Score Range        | Badge       |
| ------------------ | ----------- |
| 1 <= score < 30    | Code Ninja  |
| 30 <= score < 60   | Code Champ  |
| 60 <= score <= 100 | Code Master |

> A user can only have **unique** badges (max 3).

---

## 🛠️ API Endpoints

### POST `/coderhack/api/v1/users`

Register a new user

```json
Request Body:
{
  "userId": "123",
  "username": "john_doe"
}
```

### GET `/coderhack/api/v1/users`

Retrieve all users sorted by score

### GET `/coderhack/api/v1/users/{userId}`

Retrieve a specific user's details

### PUT `/coderhack/api/v1/users/{userId}`

Update the user's score (badge is automatically awarded)

```json
Request Body:
{
  "score": 45
}
```

### DELETE `/coderhack/api/v1/users/{userId}`

Deregister a user from the contest

### GET `/coderhack/api/v1/users/leaderboard`

Get the leaderboard sorted by score

---

## 🔒 Validation & Error Handling

* Score must be in the range 0 - 100
* Duplicate user ID registration returns `400 Bad Request`
* User not found returns `404 Not Found`
* Invalid data returns appropriate HTTP status codes

---

## 🌐 Tech Stack

* Java 21
* Spring Boot 3+
* MongoDB
* Spring Data MongoDB
* JUnit 5
* Gradle

---

## ✅ Running the Project

1. Clone the repository:

```bash
git clone https://github.com/your-username/coderhack-leaderboard.git
cd coderhack-leaderboard
```

2. Configure your MongoDB URI in `application.properties`
3. Build and run:

```bash
./gradlew bootRun
```

4. Access API at `http://localhost:8080/coderhack/api/v1/users`

---

## 👤 Author

**Ankesh Kumar**
[GitHub](https://github.com/AnkeshKr789) | [LinkedIn](https://www.linkedin.com/in/ankeshkumar90/)
