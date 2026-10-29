-- Existing assignment database: run once before starting the member-enabled app.
-- Existing posts are preserved. Their historical author/time is unknown;
-- use a migration member and the migration time, rather than inventing history.
CREATE TABLE IF NOT EXISTS member (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;
ALTER TABLE post ADD COLUMN member_id BIGINT NULL;
ALTER TABLE post ADD COLUMN created_at DATETIME(6) NULL;
INSERT INTO member (name, created_at)
SELECT '기존 게시글 작성자', CURRENT_TIMESTAMP(6)
WHERE EXISTS (SELECT 1 FROM post WHERE member_id IS NULL);
SET @legacy_member_id = LAST_INSERT_ID();
UPDATE post SET member_id = @legacy_member_id WHERE member_id IS NULL;
UPDATE post SET created_at = CURRENT_TIMESTAMP(6) WHERE created_at IS NULL;
ALTER TABLE post MODIFY member_id BIGINT NOT NULL;
ALTER TABLE post MODIFY created_at DATETIME(6) NOT NULL;
ALTER TABLE post MODIFY content TEXT;
ALTER TABLE post ADD CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES member(id);
