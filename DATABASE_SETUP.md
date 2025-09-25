# Database Setup Guide

## Environment Variables

Create a `.env` file in your project root with the following variables:

```env
# Database Configuration
DB_USERNAME=root
DB_PASSWORD=rootpassword

# Mail Configuration (Optional - configure when needed)
MAIL_USERNAME=
MAIL_PASSWORD=

# Keycloak Configuration (Optional - configure when needed)
KEYCLOAK_URL=http://localhost:8081/auth
KEYCLOAK_REALM=festivo
KEYCLOAK_CLIENT_ID=festivo-backend
KEYCLOAK_CLIENT_SECRET=

# Firebase Configuration (Optional - configure when needed)
FIREBASE_PROJECT_ID=
FIREBASE_PRIVATE_KEY=
FIREBASE_CLIENT_EMAIL=

# PayHere Configuration (Optional - configure when needed)
PAYHERE_MERCHANT_ID=
PAYHERE_MERCHANT_SECRET=
PAYHERE_BASE_URL=https://sandbox.payhere.lk
```

## Database Setup Options

### Option 1: Docker Compose (Recommended)

1. Install Docker Desktop
2. Run the following command in your project root:
   ```bash
   docker-compose up -d
   ```
3. This will start MySQL and phpMyAdmin
4. Access phpMyAdmin at: http://localhost:8083
   - Username: root
   - Password: rootpassword

### Option 2: Local MySQL Installation

1. Download and install MySQL Server 8.0+
2. Set root password during installation
3. Create database:
   ```sql
   CREATE DATABASE festivo_db;
   ```
4. Update `.env` file with your MySQL credentials

## Connection Details

- **Database URL**: jdbc:mysql://localhost:3306/festivo_db
- **Username**: root (or your custom username)
- **Password**: rootpassword (or your custom password)
- **Driver**: com.mysql.cj.jdbc.Driver

## Testing Connection

1. Start your Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
2. Check the console logs for successful database connection
3. Verify tables are created automatically by Hibernate

## Troubleshooting

- Ensure MySQL server is running on port 3306
- Check firewall settings if connection fails
- Verify credentials in `.env` file
- Check MySQL server logs for any errors

