# Discord Bot

Discord 봇 프로젝트입니다.

## 요구사항

- Java 21 이상
- Gradle

## 빌드 방법

### ShadowJAR 빌드

```bash
./gradlew shadowJar
```

빌드된 JAR 파일은 `build/libs/app-1.0-SNAPSHOT.jar`에 생성됩니다.

## 실행 방법

### 1. ShadowJAR로 실행

```bash
java -jar build/libs/app-1.0-SNAPSHOT.jar

nohup java -jar build/libs/app-1.0-SNAPSHOT.jar &
```

### 2. Gradle로 실행 (개발용)

```bash
./gradlew run
```

## 주의사항

- 실행하기 전에 `.env` 파일에 Discord 봇 토큰이 설정되어 있어야 합니다.
- ShadowJAR로 실행하는 것이 배포 환경에 적합합니다.
- 개발 중에는 Gradle로 실행하는 것이 더 편리합니다.
