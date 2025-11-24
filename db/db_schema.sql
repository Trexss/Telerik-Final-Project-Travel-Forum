CREATE TABLE tags
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE users
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(32) NOT NULL UNIQUE,
    first_name    VARCHAR(32) NOT NULL,
    last_name     VARCHAR(32) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(32),
    profile_photo VARCHAR(1024),
    is_admin      TINYINT(1) DEFAULT 0 NOT NULL,
    is_blocked    TINYINT(1) DEFAULT 0 NOT NULL,
    CONSTRAINT check_size_first_name CHECK (CHAR_LENGTH(first_name) BETWEEN 4 AND 32),
    CONSTRAINT check_size_last_name CHECK (CHAR_LENGTH(last_name) BETWEEN 4 AND 32),
    CONSTRAINT check_size_username CHECK (CHAR_LENGTH(username) BETWEEN 4 AND 32)
);

CREATE TABLE posts
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT NOT NULL,
    title      VARCHAR(255) NOT NULL,
    content    TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP(),
    CONSTRAINT posts_users_id_fk FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT check_content_size CHECK (CHAR_LENGTH(content) BETWEEN 32 AND 8192),
    CONSTRAINT check_title_size CHECK (CHAR_LENGTH(title) BETWEEN 16 AND 64)
);

CREATE INDEX index_created_at ON posts (created_at);
CREATE INDEX index_user_id ON posts (user_id);

CREATE TABLE comments
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    post_id    INT NOT NULL,
    user_id    INT,
    content    TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT comments_fk_post_id FOREIGN KEY (post_id) REFERENCES posts(id),
    CONSTRAINT comments_fk_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT check_comment_content_size CHECK (CHAR_LENGTH(content) BETWEEN 1 AND 2048)
);

CREATE INDEX index_post_id ON comments (post_id);
CREATE INDEX index_comment_user_id ON comments (user_id);

CREATE TABLE post_likes
(
    user_id INT NOT NULL,
    post_id INT NOT NULL,
    PRIMARY KEY (user_id, post_id),
    CONSTRAINT post_likes_fk_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT post_likes_fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX index_postLikes_post_id ON post_likes (post_id);

CREATE TABLE post_tags
(
    post_id INT NOT NULL,
    tag_id  INT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT post_tags_fk_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT post_tags_fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);