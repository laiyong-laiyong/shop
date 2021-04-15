ALTER TABLE consume_fail_msg ADD COLUMN handler VARCHAR(255) COMMENT '处理人';
ALTER TABLE consume_fail_msg ADD COLUMN handleDate datetime COMMENT '处理时间';
ALTER TABLE publish_fail_msg ADD COLUMN handler VARCHAR(255) COMMENT '处理人';
ALTER TABLE publish_fail_msg ADD COLUMN handleDate datetime COMMENT '处理时间';