CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    nick_name VARCHAR(50) UNIQUE NOT NULL,
    account VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE INDEX idx_user_account ON users(account);


CREATE TABLE voice_posts(
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    title TEXT NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_like INT DEFAULT 0,
    user_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_user_voice_post FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id)
);

CREATE TABLE comments(
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_user_comment FOREIGN KEY (user_id) REFERENCES users(id),
    voice_post_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_voice_post_comment FOREIGN KEY (voice_post_id) REFERENCES voice_posts(id)

);

CREATE TABLE post_likes(
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_user_voice_post_like FOREIGN KEY (user_id) REFERENCES users(id),
    voice_post_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_voice_post_like FOREIGN KEY (voice_post_id) REFERENCES voice_posts(id)
);