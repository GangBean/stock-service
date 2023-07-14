# 주식 서비스

## 프로젝트 설정
1. spring boot 2.7.13
2. MySql
3. H2 - TEST 환경

## 시스템 아키텍처
- api server 
- batch server
- database server
- NginX
- CI/CD

## API 목록
1. 회원가입
2. 로그인
3. 토큰 재발급
4. 계좌등록
5. 계좌삭제
6. 계좌목록조회
7. 계좌상세조회
8. 송금 
9. 결제 
10. 주식목록조회
11. 주식상세조회
12. 주식구매
13. 주식판매
14. 예약결제
15. 계좌삭제

## ERD

## DB Schema

## 고민사항
1. Exception handling
2. Entity 연관관계 주인 설정
3. 테스트 코드 작성과 예외 테스트
    - 테스트 도구 설정 기준: JUnit5 vs Spock
    - 테스트 isolation
    - 놓치는 예외에 대한 정량적 판단 방법이나 기준이 있는지
4. 테스트 위한 시스템 공통 사용 데이터 처리: BatchExecutionTime
5. API 서버와 배치서버의 분리 - 멀티모듈 프로젝트
6. 목록조회 페이징 처리
7. @Transactional 의 적용 범위: Controller vs Service
8. 목록조회 API 자료구조: Set vs List
9. @Embeddable 을 통한 일급컬렉션 엔티티 구성
10. git branch 전략: github flow
11. reservationExecute batch 오류
    - Reservation &rarr; Account &rarr; Trades 를 참조하고 있는 관계에서,
    - 첫번째는 ManyToOne / LAZY / cascade ALL
    - 두번째는 OneToMany / LAZY / cascade ALL
    - 위와 같이 설정했는데 배치 실행시 한번의 배치 수행시 같은 Account 에 대한 예약이 여러건이면, Account 와 Trades 의 연관관계 테이블에 마지막 반영분만 insert 된다.
    - 전체 대상 select &rarr; account select &rarr; trades select &rarr; item processor 건별처리 &rarr; [reservation select 한건 &rarr; bank select 한건 &rarr; member select 한건 &rarr; trade insert 한건 &rarr; trades insert 한건 &rarr;] 반복 &rarr; account update &rarr; reservation update all &rarr; account_trades insert 1건
    - saveAll을 해서 그런가? 건별로 save를 해볼까?