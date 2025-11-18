# Redis Practice Summary(2025.11.18)

Redis를 활용해 **세션 클러스터링**, **비회원 장바구니**, **캐싱**, **리더보드 & Write-Behind 전략**을 구현한 실습 내용을 요약 정리한 문서입니다.

---

## 1. 세션 클러스터링(Session Clustering)

### 🎯 목적
여러 애플리케이션 인스턴스에서 **동일한 로그인 세션을 공유**하도록 구현한다.

### 📌 주요 내용
- Spring Security Form Login 기반
- CSRF 비활성화
- 세션 저장소를 Redis로 변경
- InMemoryUserDetailsManager 사용
- 인스턴스 간 세션 일관성 유지 확인

### 📚 핵심 개념
- 스케일아웃 환경에서 인증 상태 공유  
- Redis를 중앙 세션 저장소로 활용

---

## 2. Redis Hash 기반 비회원 장바구니

### 🎯 목적
로그인 없이 사용할 수 있는 장바구니를 Redis Hash로 구현한다.

### 📌 기능
- 사용자별 장바구니 저장 (`cart:{userId}`)
- Hash field: 상품 ID, value: 수량  
- **TTL 3시간**
- 상품 추가/변경/삭제
- 수량 0 이하 → 자동 제거
- 클라이언트가 상품 ID + 수량만 전달

### 📚 핵심 개념
- Hash는 구조화된 데이터 저장에 적합  
- TTL로 일정 시간 후 자동 만료  
- 멀티 인스턴스 환경에서도 안전

---

## 3. Redis 캐싱 적용(Store CRUD)

### 🎯 목적
Store 엔티티 CRUD에 Redis 캐싱을 적용하여 조회 성능을 개선한다.

### 📌 구현
- `@Cacheable`, `@CachePut`, `@CacheEvict` 활용
- 단건 조회: **Cache-Aside**
- 생성/수정: **Write-Through**
- 전체 조회 캐싱
- 캐시 일관성 유지를 위한 적절한 캐시 삭제/갱신

### 📚 핵심 개념
- Cache-Aside / Write-Through 전략 이해
- Spring Cache Abstraction 활용

---

## 4. 리더보드 & Write-Behind 구현

### 🎯 목적
대량 판매/트래픽 상황을 가정하여 구매 랭킹과 지연 쓰기 방식을 구현한다.

### 📌 기능
- **Sorted Set** 기반 리더보드  
  - 구매량 기준 Top 10 조회
- Write-Behind 방식으로 Redis에 먼저 기록 후 DB로 일정 주기 반영
- Redis 직접 조작(RestTemplate 등)  
  → Annotation 기반 캐싱 사용 불가

### 📚 핵심 개념
- Sorted Set은 점수 기반 정렬에 최적  
- Write-Behind: 고성능 처리 가능하지만 장애 대비 필요

---

## 📘 전체 요약

| 실습 | 활용된 Redis 기능 | 핵심 포인트 |
|------|------------------|-------------|
| 세션 클러스터링 | 세션 저장소 (String, TTL) | 인스턴스 간 로그인 공유 |
| 비회원 장바구니 | Hash + TTL | 로그인 없는 사용자 데이터 관리 |
| Store 캐싱 | Spring Cache + Redis | Cache-Aside / Write-Through |
| 리더보드 & Write-Behind | Sorted Set + 수동 캐싱 | 고성능 랭킹, 지연 쓰기 |
