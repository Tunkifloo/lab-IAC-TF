# MTD API

## Installation

1. Clone the repository:
```
git clone https://github.com/your-username/mtd-api.git
```
2. Navigate to the project directory:
```
cd mtd-api
```
3. Build the project:
```
./gradlew build
```
4. Run the application:
```
./gradlew bootRun
```

## Usage

The MTD API provides the following endpoints:

### Login
- **Endpoint**: `POST /api/v1/users/login`
- **Request Body**:
```json
{
  "username": "your-username",
  "password": "your-password"
}
```
- **Response**:
```json
{
  "token": "your-jwt-token",
  "firstLogin": true
}
```

### Register User
- **Endpoint**: `POST /api/v1/users/register`
- **Request Body**:
```json
{
  "username": "new-user",
  "password": "new-password",
  "role": "ADMIN",
  "name": "John",
  "surname": "Doe",
  "dni": "12345678",
  "email": "john.doe@example.com",
  "age": 30,
  "phoneNumber": "123456789",
  "country": "USA",
  "region": "California",
  "motivation": "I want to contribute to the project."
}
```
- **Response**:
```json
{
  "token": "your-jwt-token"
}
```

## API

The MTD API provides the following entities:

- `Admin`
- `Coordinator`
- `Council`
- `Maker`
- `Role`
- `User`

The `User` entity is the base class for all user types, and it implements the `UserDetails` interface from Spring Security.

## Contributing

To contribute to the MTD API, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Implement your changes.
4. Test your changes.
5. Submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
