INSERT INTO member (email, is_activated, name, password, phone, pin_number, role)
VALUES
    ('test1111@gmail.com', 1, '테스트', '$2a$10$yiKBKLZ7qFeLIUu/ByRyheZxk7OiuUBLoDKSU4ompN4HvpKXC8E4m', '01011111111', '$2a$10$d8qQGqzmIuiguX1vYOCnKuwfy6Bip8lJ9HpUYqt9EKqbBrMY3vFX2', 'USER');

-- musical
INSERT INTO musical (id, place, place_detail, ranking, running_time, ticketing_end_date,
                     ticketing_start_date, title, detail_image_url, notice_image_url,
                     place_image_url, poster_image_url)
VALUES (1, '서울', '서울 아트센터', 1, 150, '2024-10-16', '2024-10-01', '킹키부츠',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg1.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg1.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg1.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg1.png'),
       (2, '대구', '대구 문화회관', 2, 130, '2024-10-16', '2024-10-01', '레드북',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg2.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg2.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg2.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg2.png'),
       (3, '서울', '서울 아트센터', 3, 60, '2024-10-17', '2024-10-05', '오페라의 유령',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg3.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg3.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg3.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg3.png'),
       (4, '대구', '대구 문화회관', 4, 130, '2024-10-16', '2024-10-02', '레미제라블',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg4.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg4.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg4.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg4.png'),
       (5, '부산', '부산 드림시어터', 5, 60, '2024-10-17', '2024-10-03', '지킬 앤 하이드',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg5.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg5.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg5.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg5.png'),
       (6, '서울', '서울 국립극장', 12, 130, '2024-10-16', '2024-10-01', '시카고',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg6.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg6.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg6.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg6.png'),
       (7, '인천', '인천 송도아트센터', 11, 150, '2024-10-17', '2024-10-01', '라이온 킹',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg7.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg7.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg7.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg7.png'),
       (8, '광주', '광주 문화예술회관', 10, 200, '2024-10-18', '2024-10-02', '맘마미아!',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg8.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg8.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg8.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg8.png'),
       (9, '수원', '수원SK아트리움', 9, 180, '2024-10-18', '2024-10-06', '위키드',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg9.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg9.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg9.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg9.png'),
       (10, '성남', '성남아트센터', 8, 80, '2024-10-18', '2024-10-07', '마리 앙투아네트',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg10.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg10.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg10.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg10.png'),
       (11, '대전', '대전 예술의전당', 7, 60, '2024-10-17', '2024-10-07', '지젤',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg11.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg11.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg11.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg11.png'),
       (12, '창원', '창원 성산아트홀', 6, 80, '2024-10-17', '2024-10-01', '노트르담 드 파리',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/DetailImg12.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/NoticeImg12.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PlaceImg12.png',
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/musical/PosterImg12.png');

-- schedule
INSERT INTO schedule(id, date, start_time, end_time, musical_id)
VALUES (1, '2024-10-17', '10:00:00', '12:30:00', 1),
       (2, '2024-10-17', '16:00:00', '18:30:00', 1),
       (3, '2024-10-18', '10:00:00', '12:30:00', 1),
       (4, '2024-10-18', '16:00:00', '18:30:00', 1),
       (5, '2024-10-21', '10:00:00', '12:30:00', 1),
       (6, '2024-10-21', '16:00:00', '18:30:00', 1),
       (7, '2024-10-22', '12:00:00', '14:10:00', 2),
       (8, '2024-10-22', '18:00:00', '20:10:00', 2),
       (9, '2024-10-23', '12:00:00', '14:10:00', 2),
       (10, '2024-10-23', '18:00:00', '20:30:00', 2),
       (11, '2024-10-24', '12:00:00', '14:10:00', 2),
       (12, '2024-10-24', '18:00:00', '20:10:00', 2),
       (13, '2024-10-26', '10:00:00', '11:00:00', 3),
       (14, '2024-10-26', '14:00:00', '15:00:00', 3),
       (15, '2024-10-27', '10:00:00', '11:00:00', 3),
       (16, '2024-10-27', '14:00:00', '15:00:00', 3),
       (17, '2024-10-28', '11:00:00', '12:00:00', 3),
       (18, '2024-10-28', '19:00:00', '20:00:00', 3);

INSERT INTO actor(id, name, character_name, schedule_id, image_url)
VALUES (1, '김호영', '찰리', 1,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg1.png'),
       (2, '이석훈', '찰리', 2,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg2.png'),
       (3, '신재범', '찰리', 3,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg3.png'),
       (4, '김호영', '찰리', 4,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg4.png'),
       (5, '이석훈', '찰리', 5,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg5.png'),
       (6, '신재범', '찰리', 6,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg6.png'),
       (7, '서경수', '롤라', 1,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg7.png'),
       (8, '강홍석', '롤라', 2,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg8.png'),
       (9, '최재림', '롤라', 3,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg9.png'),
       (10, '서경수', '롤라', 4,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg10.png'),
       (11, '강홍석', '롤라', 5,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg11.png'),
       (12, '최재림', '롤라', 6,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/actor/ActorImg12.png');

-- section
INSERT INTO section(id, available_seats, grade, price, musical_id, schedule_id)
VALUES (1, 100, 'R', 190000, 1, 1),
       (2, 100, 'S', 160000, 1, 1),
       (3, 100, 'A', 130000, 1, 1),
       (4, 100, 'R', 190000, 1, 2),
       (5, 100, 'S', 160000, 1, 2),
       (6, 100, 'A', 130000, 1, 2),
       (7, 100, 'R', 190000, 1, 3),
       (8, 100, 'S', 160000, 1, 3),
       (9, 100, 'A', 130000, 1, 3),
       (10, 100, 'R', 190000, 1, 4),
       (11, 100, 'S', 160000, 1, 4),
       (12, 100, 'A', 130000, 1, 4),
       (13, 100, 'R', 190000, 1, 5),
       (14, 100, 'S', 160000, 1, 5),
       (15, 100, 'A', 130000, 1, 5),
       (16, 100, 'R', 190000, 1, 6),
       (17, 100, 'S', 160000, 1, 6),
       (18, 100, 'A', 130000, 1, 6);

-- seat
-- sectionId 1에 대한 1~100 좌석 생성
INSERT INTO seat (seat_no, is_available, schedule_id, section_id)
SELECT ROW_NUMBER() OVER () AS seat_no, b'1',
       1,
       1
FROM (SELECT 0 AS n
      UNION ALL
      SELECT 1
      UNION ALL
      SELECT 2
      UNION ALL
      SELECT 3
      UNION ALL
      SELECT 4
      UNION ALL
      SELECT 5
      UNION ALL
      SELECT 6
      UNION ALL
      SELECT 7
      UNION ALL
      SELECT 8
      UNION ALL
      SELECT 9) AS a,
     (SELECT 0 AS n
      UNION ALL
      SELECT 1
      UNION ALL
      SELECT 2
      UNION ALL
      SELECT 3
      UNION ALL
      SELECT 4
      UNION ALL
      SELECT 5
      UNION ALL
      SELECT 6
      UNION ALL
      SELECT 7
      UNION ALL
      SELECT 8
      UNION ALL
      SELECT 9) AS b,
     (SELECT 0 AS n
      UNION ALL
      SELECT 1
      UNION ALL
      SELECT 2
      UNION ALL
      SELECT 3
      UNION ALL
      SELECT 4
      UNION ALL
      SELECT 5
      UNION ALL
      SELECT 6
      UNION ALL
      SELECT 7
      UNION ALL
      SELECT 8
      UNION ALL
      SELECT 9) AS c LIMIT 100;

-- sectionId 2에 대한 101~200 좌석 생성
INSERT INTO seat (seat_no, is_available, schedule_id, section_id)
SELECT ROW_NUMBER() OVER () + 100 AS seat_no, b'1',
       1,
       2
FROM (SELECT 0 AS n
      UNION ALL
      SELECT 1
      UNION ALL
      SELECT 2
      UNION ALL
      SELECT 3
      UNION ALL
      SELECT 4
      UNION ALL
      SELECT 5
      UNION ALL
      SELECT 6
      UNION ALL
      SELECT 7
      UNION ALL
      SELECT 8
      UNION ALL
      SELECT 9) AS a,
     (SELECT 0 AS n
      UNION ALL
      SELECT 1
      UNION ALL
      SELECT 2
      UNION ALL
      SELECT 3
      UNION ALL
      SELECT 4
      UNION ALL
      SELECT 5
      UNION ALL
      SELECT 6
      UNION ALL
      SELECT 7
      UNION ALL
      SELECT 8
      UNION ALL
      SELECT 9) AS b,
     (SELECT 0 AS n
      UNION ALL
      SELECT 1
      UNION ALL
      SELECT 2
      UNION ALL
      SELECT 3
      UNION ALL
      SELECT 4
      UNION ALL
      SELECT 5
      UNION ALL
      SELECT 6
      UNION ALL
      SELECT 7
      UNION ALL
      SELECT 8
      UNION ALL
      SELECT 9) AS c LIMIT 100;

-- sectionId 3에 대한 201~300 좌석 생성
INSERT INTO seat (seat_no, is_available, schedule_id, section_id)
SELECT ROW_NUMBER() OVER () + 200 AS seat_no, b'1',
       1,
       3
FROM (SELECT 0 AS n
      UNION ALL
      SELECT 1
      UNION ALL
      SELECT 2
      UNION ALL
      SELECT 3
      UNION ALL
      SELECT 4
      UNION ALL
      SELECT 5
      UNION ALL
      SELECT 6
      UNION ALL
      SELECT 7
      UNION ALL
      SELECT 8
      UNION ALL
      SELECT 9) AS a,
     (SELECT 0 AS n
      UNION ALL
      SELECT 1
      UNION ALL
      SELECT 2
      UNION ALL
      SELECT 3
      UNION ALL
      SELECT 4
      UNION ALL
      SELECT 5
      UNION ALL
      SELECT 6
      UNION ALL
      SELECT 7
      UNION ALL
      SELECT 8
      UNION ALL
      SELECT 9) AS b,
     (SELECT 0 AS n
      UNION ALL
      SELECT 1
      UNION ALL
      SELECT 2
      UNION ALL
      SELECT 3
      UNION ALL
      SELECT 4
      UNION ALL
      SELECT 5
      UNION ALL
      SELECT 6
      UNION ALL
      SELECT 7
      UNION ALL
      SELECT 8
      UNION ALL
      SELECT 9) AS c LIMIT 100;

-- account
INSERT INTO account (id, balance, bank_logo, bank_name, bank_color, created_at, number, member_id)
VALUES (1, 4500000,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/account/BankLogo1.png',
        'KOOKMIN', '#FFCC00', '2024-10-06 10:00:00', '853001-00-122246', 1),
       (2, 3150000,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/account/BankLogo2.png',
        'SHINHAN', '#003DA5', '2024-10-06 12:00:00', '110-356-074876', 1),
       (3, 4170000,
        'https://240930-kb-deploy-test-s3-bucket.s3.ap-northeast-2.amazonaws.com/account/BankLogo3.png',
        'HANA', '#009490', '2024-10-06 14:00:00', '789-4561234-10112', 1);

-- transaction_record
-- 첫 번째 계좌(국민은행)
INSERT INTO transaction_record (amount, created_at, current_balance, transaction_type, vendor,
                                account_id)
VALUES (500000, '2024-10-01 09:00:00', 500000, 'DEPOSIT', '급여', 1),
       (100000, '2024-10-01 10:00:00', 400000, 'WITHDRAWAL', '올리브영', 1),
       (600000, '2024-10-02 11:00:00', 1000000, 'DEPOSIT', '환불', 1),
       (200000, '2024-10-03 12:00:00', 800000, 'WITHDRAWAL', '스타벅스', 1),
       (700000, '2024-10-03 13:00:00', 1500000, 'DEPOSIT', '계좌이체', 1),
       (300000, '2024-10-04 14:00:00', 1200000, 'WITHDRAWAL', 'GS25', 1),
       (1000000, '2024-10-05 15:00:00', 2200000, 'DEPOSIT', '보너스', 1),
       (200000, '2024-10-06 16:00:00', 2000000, 'WITHDRAWAL', '다이소', 1),
       (3000000, '2024-10-06 17:00:00', 5000000, 'DEPOSIT', '투자수익', 1),
       (500000, '2024-10-06 18:00:00', 4500000, 'WITHDRAWAL', '버거킹', 1);

-- 두 번째 계좌(신한은행)
INSERT INTO transaction_record (amount, created_at, current_balance, transaction_type, vendor,
                                account_id)
VALUES (300000, '2024-10-01 09:00:00', 300000, 'DEPOSIT', '알바급여', 2),
       (50000, '2024-10-01 10:00:00', 250000, 'WITHDRAWAL', '스타벅스', 2),
       (450000, '2024-10-02 11:00:00', 700000, 'DEPOSIT', '물품환불', 2),
       (80000, '2024-10-03 12:00:00', 620000, 'WITHDRAWAL', '배달의민족', 2),
       (350000, '2024-10-03 13:00:00', 970000, 'DEPOSIT', '이체입금', 2),
       (120000, '2024-10-04 14:00:00', 850000, 'WITHDRAWAL', '교보문고', 2),
       (700000, '2024-10-05 15:00:00', 1550000, 'DEPOSIT', '추가급여', 2),
       (150000, '2024-10-06 16:00:00', 1400000, 'WITHDRAWAL', '다이소', 2),
       (2000000, '2024-10-06 17:00:00', 3400000, 'DEPOSIT', '투자이익', 2),
       (250000, '2024-10-06 18:00:00', 3150000, 'WITHDRAWAL', '버거킹', 2);

-- 세 번째 계좌(하나은행)
INSERT INTO transaction_record (amount, created_at, current_balance, transaction_type, vendor,
                                account_id)
VALUES (400000, '2024-10-01 09:00:00', 400000, 'DEPOSIT', '용돈', 3),
       (70000, '2024-10-01 10:00:00', 330000, 'WITHDRAWAL', '커피빈', 3),
       (800000, '2024-10-02 11:00:00', 1130000, 'DEPOSIT', '물품환불', 3),
       (150000, '2024-10-03 12:00:00', 980000, 'WITHDRAWAL', '맘스터치', 3),
       (500000, '2024-10-03 13:00:00', 1480000, 'DEPOSIT', '이체입금', 3),
       (180000, '2024-10-04 14:00:00', 1300000, 'WITHDRAWAL', 'CU', 3),
       (900000, '2024-10-05 15:00:00', 2200000, 'DEPOSIT', '보너스', 3),
       (130000, '2024-10-06 16:00:00', 2070000, 'WITHDRAWAL', '롯데리아', 3),
       (2500000, '2024-10-06 17:00:00', 4570000, 'DEPOSIT', '투자수익', 3),
       (400000, '2024-10-06 18:00:00', 4170000, 'WITHDRAWAL', '미니스톱', 3);