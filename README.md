# mechugi.io server

`mechugi.io`의 Spring Boot API 서버 스타터 프로젝트입니다.

## 기술 스택

- Java 21
- Spring Boot 3.5.16
- Gradle 8.14.3 (Gradle Wrapper 포함)
- Spring Web, Spring Data JPA, Validation, Lombok
- H2 인메모리 데이터베이스

## 실행

Java 21이 필요합니다.

```bash
./gradlew bootRun
```

서버가 시작되면 다음 주소를 사용할 수 있습니다.

- Health API: `GET http://localhost:8080/api/health`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:mechugi`
  - User Name: `sa`
  - Password: 비워 둠

Health API 응답 예시:

```json
{
  "status": "UP",
  "service": "mechugi.io",
  "timestamp": "2026-09-07T00:00:00Z"
}
```

## 검증

```bash
./gradlew clean test
./gradlew build
```

## 패키지 구조

```text
io.mechugi
├── Application.java
└── health
    └── api
        ├── HealthController.java
        └── HealthResponse.java
```

기능이 늘어나면 `io.mechugi.<domain>` 아래에 `api`, `application`, `domain`,
`infrastructure` 패키지를 두는 도메인 중심 구조로 확장하는 것을 권장합니다.

## 로컬 데이터베이스

현재 H2는 인메모리 모드이며 서버를 종료하면 데이터가 사라집니다.
`spring.jpa.hibernate.ddl-auto=create-drop`은 초기 개발용 설정입니다. 운영 DB를
도입할 때는 Flyway 같은 마이그레이션 도구와 운영 프로필 설정으로 교체해야 합니다.
