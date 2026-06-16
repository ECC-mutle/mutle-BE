# 🍾 MUTLE Backend (MUSIC + BOTTLE)

<div align="center">
  <img src="https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=Java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.1-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-15+-4169E1?style=for-the-badge&logo=PostgreSQL&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-🐳-2496ED?style=for-the-badge&logo=Docker&logoColor=white"/>
</div>

<br>

> **"기록과 음악으로 연결을 만들다"**
> 바다 위 무인도, 유리병에 담긴 음악과 당신의 이야기.  
> ECC 25-2 겨울 프로젝트 2팀 **MUTLE**의 백엔드 시스템 애플리케이션 저장소입니다.

---

## 🗓️ 개발 기간
- **총 개발 기간**: 2025년 12월 ~ 2026년 2월

---

## 👥 팀원 소개 (ECC 25-2 겨울 프로젝트 2팀)

<div align="center">

| 이름 | 역할 | GitHub |
| :---: | :---: | :---: |
| **이예안** | Front-End (팀장) | [@dldPdks5](https://github.com/dldPdks5) |
| **김서현** | Back-End | [@seohyeonS2](https://github.com/seohyeonS2) |
| **정재은** | Back-End | [@Jaeeun71](https://github.com/Jaeeun71) |
| **정하윤** | Front-End / UIUX | [@hayun524](https://github.com/hayun524) |

</div>

---

## 🏝️ 서비스 소개

**MUTLE**은 사용자가 자신만의 '섬'을 가꾸고, 그날의 감정과 기억을 음악에 담아 '유리병'에 띄워 보내는 **음악 기반 소셜 네트워킹 서비스(SNS)**입니다. 

- **유리병 낚시 (랜덤 매칭)**: 바다를 표류하는 다른 사람의 유리병을 무작위로 주워 음악과 숨겨진 이야기를 확인하고 반응을 남깁니다.
- **30일 음악 챌린지 (Today's Quest)**: 매일 주어지는 질문에 맞춰 나만의 음악과 감정을 기록하며 깊이 있는 아카이빙을 형성합니다.
- **섬-친구 시스템**: 음악을 매개로 취향이 맞는 유저들과 소통하며 따뜻한 연결을 확장해 나갑니다.

---

## 🛠️ 기술 스택 (Tech Stacks)

### 💻 Core Framework & Language
- **Language**: Java 17
- **Framework**: Spring Boot 3.4.1
- **Build Tool**: Gradle (Groovy DSL)

### 🔐 Security & Authentication
- **Security**: Spring Security
- **Authentication**: JWT (Json Web Token) 기반 자체 인증 및 Filter 인프라 구축
- **Token Management**: `TokenBlacklist` 컴포넌트를 설계하여 안전한 로그아웃 및 만료 토큰 원천 차단

### 🗄️ Database & Storage
- **Main Database**: PostgreSQL (Object-Relational DBMS)
- **ORM / Data**: Spring Data JPA (Hibernate)
- **Cloud Storage**: Supabase Storage 멀티파트 연동을 통한 프로필 및 콘텐츠 미디어 파일 관리

### ⚙️ DevOps & Infrastructure
- **Containerization**: Docker (Dockerfile 멀티 스테이지 빌드 최적화를 통해 이미지 경량화)
- **API Documentation**: Springdoc OpenAPI v2 (Swagger UI)

---

## 📁 디렉토리 구조 (Directory Structure)

본 프로젝트는 도메인 주도 및 역할 기반 분리를 준수하여, 확장성과 유지보수성을 극대화한 레이어드 아키텍처(Layered Architecture) 구조로 설계되었습니다.

```text
src/main/java/com/mutle/mutle/
├── config/                 # Security, CORS, Swagger 등 글로벌 인프라 설정
│   ├── AppConfig.java
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   └── WebConfig.java
├── controller/             # HTTP 요청 매핑 및 클라이언트 인터페이스 (Presentation Layer)
│   ├── AuthController.java
│   ├── BottleApiController.java
│   ├── FriendshipApiController.java
│   ├── ImageController.java
│   ├── IslandController.java
│   ├── MenuController.java
│   └── MusicController.java
├── dto/                    # 계층 간 데이터 교환을 위한 순수 데이터 객체 (Data Transfer Object)
├── entity/                 # 데이터베이스 테이블과 매핑되는 비즈니스 핵심 도메인 모델 (Domain Entity)
├── exception/              # 비즈니스 예외 처리 및 일관된 에러 응답 정의 (Global Exception Handlers)
│   ├── CustomException.java
│   ├── ErrorCode.java
│   └── GlobalExceptionHandler.java
├── jwt/                    # JWT 토큰 관리, 인증 필터 및 블랙리스트 컴포넌트
│   ├── JwtAuthenticationFilter.java
│   ├── JwtUtil.java
│   └── TokenBlacklist.java
├── repository/             # 데이터 영속성 제어를 위한 Spring Data JPA 인터페이스 (Data Access Layer)
└── service/                # 핵심 트랜잭션 및 비즈니스 로직 캡슐화 (Business Logic Layer)
```
---

## 🗺️ 시스템 아키텍처 및 도메인 모델 (ERD)

### 📌 주요 엔티티 모델 (Domain Entities)

- **`User`** : 회원 고유 식별 정보, 패스워드, 소셜 계정 연동 및 Island 프로필 정보(Bio, 한 줄 소개) 총괄
- **`Bottle`** : 유저가 가꾼 섬에서 작성된 음악 기반 감정 기록 글, 해시태그, 표류 시작 시간 관리
- **`Music`** : iTunes API 연동을 통해 실시간 검색 및 동적 저장되는 고유 음원 메타데이터 (곡명, 아티스트, 앨범 커버 이미지)
- **`RepMusic`** : 유저가 본인의 공간(섬)을 표현하기 위해 커스텀 설정한 대표곡 매핑 관계 테이블
- **`Platform`** : Apple Music, Spotify, YouTube Music 등 유저별 외부 스트리밍 서비스 랜딩 링크 연동
- **`FriendShip`** : 유저 간 관계 생명주기 관리 및 상태 값(`PENDING`, `ACCEPTED`)에 따른 친구 흐름 제어
- **`Reaction`** : 타인의 유리병에 피드백을 남길 수 있는 감정 기반 공감 상호작용 모델
- **`TodayQuest`** : 일별 다채로운 질문이 주어지는 30일 음악 챌린지 퀘스트 및 답변 매핑 아카이브
- **`Bookmark`** : 계속해서 보관하고 싶은 타인의 감정 유리병을 저장하는 스크랩북 데이터

### 📊 ERD (데이터베이스 관계도)
```
==========================================================================================
                                   [ MUTLE SYSTEM ERD ]
==========================================================================================

  [1. 사용자 및 친구 공간]
  - User         : 회원 고유 식별 정보, 패스워드, 소셜 로그인(Kakao, Google) 연동 인프라
  - FriendShip   : 유저 간 친구 신청, 수락/거절 생명주기 제어 및 상태 관리 (PENDING, ACCEPTED)

  [2. 섬 커스텀 프로필]
  - Platform     : 외부 스트리밍 연동 (Spotify, Apple Music, YouTube Music 등)
  - RepMusic     : 사용자가 본인의 섬(공간)을 표현하기 위해 설정한 대표곡 매핑 데이터

  [3. 유리병 소통 및 상호작용]
  - Bottle       : 유저가 작성한 음악 기반 감정 기록 글, 해시태그, 바다 표류 시작 시간
  - Reaction     : 타인의 유리병에 피드백을 남기는 감정 기반 공감 상호작용 모델
  - Bookmark     : 보관하고 싶은 다른 유저의 감정 유리병 스크랩북 데이터

  [4. 음악 및 챌린지 퀘스트]
  - Music        : iTunes API 연동을 통해 실시간 검색 및 시스템에 동적 저장되는 고유 음원 메타
  - TodayQuest   : 일별 다채로운 질문이 주어지는 30일 음악 챌린지 퀘스트 및 답변 아카이브

------------------------------------------------------------------------------------------

                           [ 데이터베이스 테이블 연관 관계도 ]

      [Platform] (N) ───┐
                        │
      [RepMusic] (N) ───┼─── (1) [ User ] 1 ──────── 1 [ FriendShip ] (상태 제어)
                        │         │
      [Bookmark] (N) ───┘         │ (1)
                                  │
                                  └─── N [ Bottle ] 1 ─── 1 [ Music ]
                                           │
                                           └─── N [ Reaction ]

==========================================================================================
```
> [!NOTE]
> 아래 배지나 링크를 클릭하시면 ERDCloud 공식 사이트에서 정규화된 테이블 구조 및 실시간 컬럼 명세를 상세히 확인하실 수 있습니다.

<a href="https://www.erdcloud.com/d/qpuDCWpzuEu2NLBSz" target="_blank">
  <img src="https://img.shields.io/badge/ERDCloud-실시간%20ERD%20확인하기-0078D4?style=for-the-badge&logo=databricks&logoColor=white"/>
</a>

---
## ⚙️ 시작 가이드 (How to Run)
### 1. Prerequisites (사전 요구사항)
프로젝트를 로컬 환경에서 구동하기 위해 아래 환경이 필요합니다.
- Java 17 JDK
- PostgreSQL 15 이상 데이터베이스 인스턴스


### 2. 환경 변수 설정 (application.yaml)
`src/main/resources/application.yaml` 경로의 설정 파일에 로컬 PostgreSQL 접속 정보 및 JWT Secret Key 설정을 완료해 주세요.

```YAML
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mutle
    username: YOUR_POSTGRES_USERNAME
    password: YOUR_POSTGRES_PASSWORD
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: YOUR_JWT_SECRET_KEY_HERE
```

### 3. 빌드 및 실행 (Local 환경)
터미널을 열고 프로젝트 루트 디렉토리에서 아래 명령어를 순서대로 실행합니다.

```Bash
# 저장소 복사 (Clone Repository)
$git clone [https://github.com/ecc-mutle/mutle-be.git$](https://github.com/ecc-mutle/mutle-be.git$) cd mutle-be

# 빌드 및 실행 (Gradle Build & Execution)
$./gradlew clean build -x test$ java -jar build/libs/mutle-0.0.1-SNAPSHOT.jar
```
서버가 정상적으로 실행되면 http://localhost:8080 포트를 통해 API 서버와 통신할 수 있습니다.

### 4. Docker를 이용한 컨테이너 가동
프로젝트에 포함된 멀티 스테이지 Dockerfile을 활용하여 격리된 환경에서 즉시 실행이 가능합니다.

```Bash
# Docker 이미지 빌드
$ docker build -t mutle-backend-app .

# 컨테이너 포트 바인딩 및 실행
$ docker run -p 8080:8080 --name mutle-server mutle-backend-app
```
