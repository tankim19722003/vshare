# VShare - Group Voice Messaging Platform

VShare is a backend service for a voice messaging application that allows users to register, log in, and share voice recordings in private groups. Users can create groups, add members, and post voice messages for everyone in the group to hear.

The project is built with Java & Spring Boot and deployed on AWS EC2 with an automated CI/CD pipeline using GitHub Actions.

**Deployment URL:** [http://3.26.3.86:8080](http://3.26.3.86:8080)

## ✨ Key Features

- **User Authentication:**
  - Register a new account.
  - Log in with an account and password to receive a JWT token.
  - Log out.
- **Voice Message Management:**
  - Create and upload a new voice message (audio file).
  - Delete a voice message.
- **Group Management:**
  - Create a new group.
  - Add members to an existing group.
- **Cloud Storage:** Audio files are stored securely and efficiently on AWS S3.

## 🚀 Tech Stack

- **Backend:** Java 21, Spring Boot 4.0.2
- **Security:** Spring Security, JSON Web Tokens (JWT)
- **Database:** Spring Data JPA, Hibernate
- **File Storage:** AWS S3
- **Build Tool:** Apache Maven
- **Triển khai (Deployment):**
  - **Cloud:** AWS EC2
  - **CI/CD:** GitHub Actions

## ⚙️ Local Setup and Running

### Prerequisites

- JDK 21
- Maven 3.x
- A database instance (MySQL)
- An AWS account with an S3 bucket and access credentials (Access Key & Secret Key)

### Setup Steps

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd vshare
    ```

2.  **Configure the environment:**
    Create an `application-dev.properties` file in the `src/main/resources` directory to configure the local environment.

    ```properties
    # ===============================================
    # =      DATABASE CONFIGURATION                 =
    # ===============================================
    spring.datasource.url=jdbc:mysql://localhost:3306/vshare_db?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh
    spring.datasource.username=<your-db-user>
    spring.datasource.password=<your-db-password>
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    
    # ===============================================
    # =      JPA & HIBERNATE CONFIGURATION          =
    # ===============================================
    spring.jpa.hibernate.ddl-auto=update # Use 'update' for dev, 'validate' or 'none' for prod
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
    
    # ===============================================
    # =      JWT CONFIGURATION                      =
    # ===============================================
    secret.key=<your-very-long-and-secure-jwt-secret-key>
    token.expiration=86400000 # 24 hours in milliseconds
    
    # ===============================================
    # =      AWS S3 CONFIGURATION                   =
    # ===============================================
    AWS_S3_BUCKET_NAME=<your-s3-bucket-name>
    AWS_S3_REGION=<your-aws-region> # e.g., ap-southeast-2
    
    # ===============================================
    # =      FILE UPLOAD CONFIGURATION              =
    # ===============================================
    spring.servlet.multipart.max-file-size=10MB
    spring.servlet.multipart.max-request-size=10MB
    ```

3.  **Run the application:**
    ```bash
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
    ```
    The application will be running at `http://localhost:8080`.

## ☁️ Deployment

This project is configured for automatic deployment to AWS EC2 whenever a new commit is pushed to the `main` branch.

### CI/CD Pipeline

The pipeline is defined in the `.github/workflows/deploy.yml` file and includes the following steps:
1.  **Checkout Code:** Checks out the source code from the repository.
2.  **Set up JDK:** Installs Java 21.
3.  **Build with Maven:** Builds the project into a `.jar` file.
4.  **Copy JAR to EC2:** Copies the built JAR file to the EC2 server.
5.  **Run App on EC2:** Stops the old process (if it exists) and runs the new JAR file with the necessary environment variables.

### GitHub Secrets Configuration

For the CI/CD pipeline to work, you need to configure the following secrets in the **Settings > Secrets and variables > Actions** section of your GitHub repository:

- `EC2_HOST`: The public IP address of the EC2 instance.
- `EC2_USER`: The username for SSH access to the EC2 instance (e.g., `ubuntu`, `ec2-user`).
- `EC2_SSH_KEY`: The private key for SSH access to the EC2 instance.
- `DB_HOST`: The database host.
- `DB_NAME`: The database name.
- `DB_USER`: The database user.
- `DB_PASSWORD`: The database password.
- `AWS_ACCESS_KEY_ID`: AWS Access Key ID.
- `AWS_SECRET_ACCESS_KEY`: AWS Secret Access Key.
- `AWS_S3_BUCKET_NAME`: The name of the S3 bucket.
- `AWS_S3_REGION`: The region of the S3 bucket (e.g., `ap-southeast-1`).

## 📋 API Endpoints

Below are some of the main application endpoints.

**Authentication**
- `POST /users/register`: Register a new user.
- `POST /users/login`: Log in and receive a JWT.

**Voice Post**
- `POST /voice-post`: Create a new voice message (requires authentication).
- `GET /voice-post?page=...&size=...`: Create a new voice message (requires authentication).


**Group**
- `POST /groups`: Create a new group (requires authentication).
- `POST /groups/{groupId}/members`: Add a member to a group (requires authentication).

---

```