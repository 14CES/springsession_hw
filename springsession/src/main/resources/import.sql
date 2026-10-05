USE likelion_blog;

CREATE TABLE IF NOT EXISTS posts (

                                     id BIGINT AUTO_INCREMENT,
                                     title VARCHAR(255),
    content TEXT,
    created_at DATETIME,
    PRIMARY KEY (id)
    );
INSERT INTO posts (title, content, created_at) VALUES ('JPA 시작하기', 'Java 객체와 MySQL 테이블을 연결해봅니다.', NOW());
INSERT INTO posts (title, content, created_at) VALUES ('DTO가 필요한 이유', '목적에 필요한 데이터만 전달해봅니다.', NOW());