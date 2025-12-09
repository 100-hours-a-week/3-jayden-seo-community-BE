# Daily Share
> Daily Share 프로젝트의 백엔드 리포지토리입니다.
## **📌** 프로젝트 개요

### 서비스 소개

여러 사용자가 자신의 일상을 게시글과 댓글로 공유하며 서로 소통할 수 있는 커뮤니티 서비스입니다.

### 주요 기능

- 회원가입, 로그인 (JWT)
- 게시글, 댓글 
- 게시글 좋아요
- 게시글 무한 스크롤 조회
- 게시글 통계
- 게시글, 회원 이미지 업로드

---

## **🛠** 기술 스택

- **Language**: Java 21
- **Framework**: Spring Boot
- **Database**: MySQL
- **ORM**: JPA / Hibernate
- **Security**: BCrypt, JWT
- **Infra / DevOps**
    - Docker, Docker Compose
    - AWS EC2, VPC, S3, CloudFront
    - Lambda, API Gateway
    - RDS, ECR
    - GitHub Actions
- **Test**: JUnit 5

## 🧱 서버 아키텍쳐

<img width="600" height="650" alt="캡처" src="https://github.com/user-attachments/assets/282de188-057c-4974-8246-5e80e22ce9f0" />


## 🗂️ 패키지 구조

```bash
community/
└── backend/
    ├── .dockerignore
    ├── .gitattributes
    ├── .gitignore
    ├── Dockerfile
    ├── README.md
    ├── build.gradle
    ├── gradlew
    ├── gradlew.bat
    ├── settings.gradle
    ├── gradle/
    │   └── wrapper/
    │       ├── gradle-wrapper.jar
    │       └── gradle-wrapper.properties
    └── src/
        ├── main/
        │   ├── java/com/jayden/community/
        │   │   ├── config/
        │   │   ├── controller/
        │   │   ├── dto/
        │   │   ├── entity/
        │   │   ├── exceptions/
        │   │   ├── repository/
        │   │   ├── service/
        │   │   ├── jwt/
        │   │   └── util/
        │   └── resources/
        │       ├── templates/
				│       │    ├── privacy.html
				│       │    └── terms.html      
        │       └── application.properties
        └── test/
            └── java/com/jayden/community/

```

## **🚀 실행 방법**

```bash
cd community
./gradlew clean build
java -jar build/libs/community-0.0.1-SNAPSHOT.jar
```
