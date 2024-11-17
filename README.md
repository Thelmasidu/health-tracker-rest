### Health Tracker REST API
A RESTful API service for tracking health-related data including health histories, medication logs, and sleep records.
## Overview
Health Tracker REST is a backend service built with Kotlin that provides endpoints for managing various health-related data points. The service allows users to track their health history, medication intake, and sleep patterns through a comprehensive API interface.
## Features
- Health history tracking
- Medication logging system
- Sleep record management
- RESTful API endpoints
- Integration testing support
## Tech Stack
- Kotlin
- Spring Boot
- Maven
- Unirest (for integration testing)
## Project Structure
```
health-tracker-rest/
├── src/                    # Source files
├── .idea/                  # IDE configuration
├── .gitignore             # Git ignore rules
├── pom.xml                # Maven dependencies
└── health-tracker-rest.yaml # API configuration
```
## Prerequisites
- JDK 11 or higher
- Maven 3.6+
- Your preferred IDE (IntelliJ IDEA recommended)
## Getting Started
1. Clone the repository:
```bash
git clone https://github.com/Thelmasidu/health-tracker-rest.git
cd health-tracker-rest
```
2. Install dependencies:
```bash
mvn clean install
```
3. Run the application:
```bash
mvn spring-boot:run
```
## API Endpoints
The API provides endpoints for:
# API Endpoints

## User Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| POST | `/api/users` | Add a new user |
| GET | `/api/users/{user-id}` | Get user by ID |
| PATCH | `/api/users/{user-id}` | Update user |
| DELETE | `/api/users/{user-id}` | Delete user |
| GET | `/api/users/email/{user-email}` | Get user by email |

## Activity Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/activities` | Get all activities |
| POST | `/api/activities` | Add new activity |
| GET | `/api/activities/{activity-id}` | Get activity by ID |
| PATCH | `/api/activities/{activity-id}` | Update activity |
| DELETE | `/api/activities/{activity-id}` | Delete specific activity |
| GET | `/api/users/{user-id}/activities` | Get activities by user |
| DELETE | `/api/users/{user-id}/activities` | Delete activities by user |

## Health History Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/histories` | Get all health histories |
| POST | `/api/histories` | Add new health history |
| GET | `/api/histories/{history-id}` | Get health history by ID |
| PATCH | `/api/histories/{history-id}` | Update health history |
| DELETE | `/api/histories/{history-id}` | Delete specific health history |
| GET | `/api/users/{user-id}/histories` | Get health histories by user |
| DELETE | `/api/users/{user-id}/histories` | Delete health histories by user |

## Medication Log Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/medication` | Get all medication logs |
| POST | `/api/medication` | Add new medication log |
| GET | `/api/medication/{medication-id}` | Get medication log by ID |
| PATCH | `/api/medication/{medication-id}` | Update medication log |
| DELETE | `/api/medication/{medication-id}` | Delete specific medication log |
| GET | `/api/users/{user-id}/medication` | Get medication logs by user |
| DELETE | `/api/users/{user-id}/medication` | Delete medication logs by user |

## Sleep Record Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/sleep-records` | Get all sleep records |
| GET | `/api/users/{user-id}/sleep-records` | Get sleep records by user |
| POST | `/api/users/{user-id}/sleep-records` | Add sleep record for user |
| DELETE | `/api/users/{user-id}/sleep-records/{record-id}` | Delete specific sleep record 
Detailed API documentation is available in the `health-tracker-rest.yaml` file.
## Testing
The project includes comprehensive test coverage using:
- Unit tests for DAO layers
- Integration tests using Unirest
- Duplicated code inspection for Kotlin

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
## Latest Release
Current Version: v3.0

## Contact
Project Link: https://github.com/Thelmasidu/health-tracker-rest

