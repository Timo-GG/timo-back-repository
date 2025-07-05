CREATE TABLE univ_verification
(
    email       VARCHAR(255) PRIMARY KEY,
    univ_name   VARCHAR(255) NOT NULL,
    code        VARCHAR(255) NOT NULL,
    is_verified BOOLEAN      NOT NULL DEFAULT FALSE,
    expires_at  DATETIME(6)  NOT NULL
);