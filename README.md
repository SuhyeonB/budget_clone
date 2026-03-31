# budget_clone

**개인 학습용 가계부 프로젝트**  
Spring Boot + Redis + JWT 기반의 학습용 프로젝트입니다.  

---

## 📌 프로젝트 개요

- **목표**: Spring Boot, JWT 인증, Redis, JPA를 활용한 가계부 서비스 학습  
- **주요 기능**:
  - 사용자 회원가입/로그인/JWT 인증
  - 계좌(Account) 등록/조회/관리
  - 거래(Transaction) 등록/기록/조회/삭제
  - 인증/인가 처리 (Access Token / Refresh Token)
  - 월별 통계
  - 카테고리

---

## 🏗️ 도메인 구조

| 도메인 | 설명 |
|--------|------|
| auth | 로그인, 회원가입, JWT 토큰 발급 및 재발급 |
| user | 사용자 정보 관리 |
| account | 계좌 관리 (등록, 수정, 삭제, 조회) |
| transaction | 거래 내역 기록 및 조회 |

---

## ⚙️ 기술 스택

- **Backend**: Java 17, Spring Boot 3.x
- **DB**: MySQL / Redis (Refresh Token 저장용)
- **Security**: Spring Security, JWT
- **Build**: Gradle
- **테스트**: JUnit

---

## 🛠️ 환경 설정

1. `.env` 파일
2. docker-compose.yml을 활용해 Redis 실행
3. 애플리케이션 실행
```bash
./gradlew bootRun
```

---

## 🔑 인증 흐름
회원가입/로그인 시 Access Token + Refresh Token 발급
API 요청 시 Access Token 사용
Access Token 만료 시 Refresh Token으로 새 Access Token 재발급

## 📂 디렉토리 구조 (간략)

```
src/
 ├─ main/java/com/example/budget_clone/
 │    ├─ domain/auth/
 │    ├─ domain/user/
 │    ├─ domain/account/
 │    ├─ domain/transaction/
 │    └─ global/ (jwt, config, exception, dto 등)
 └─ main/resources/
      └─ application.yml.example
```
