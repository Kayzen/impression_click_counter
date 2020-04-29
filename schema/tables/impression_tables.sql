DROP TABLE IF EXISTS device_impressions_daily;
CREATE TABLE IF NOT EXISTS `device_impressions_daily` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
     `campaign_id`  INT(11) NOT NULL,
     `logType` ENUM('impression','click') NOT NULL,
    `status` ENUM('active', 'expired') NOT NULL DEFAULT 'active',
      PRIMARY KEY (`id` , `day_int`),
    KEY `status_idx` (`status`)
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1
PARTITION BY RANGE (day_int)
(
 PARTITION start       VALUES LESS THAN (0)
, PARTITION future      VALUES LESS THAN MAXVALUE
);

DROP TABLE IF EXISTS device_impression_counts;
CREATE TABLE IF NOT EXISTS `device_impression_counts` (
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `impression_count` INT NOT NULL DEFAULT 0,
    `click_count` INT NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`app_object_id` , `thread_id` , `device_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8
PARTITION BY RANGE (thread_id)
(
 PARTITION start       VALUES LESS THAN (0)
, PARTITION future      VALUES LESS THAN MAXVALUE
);

DROP TABLE IF EXISTS devices;
CREATE TABLE IF NOT EXISTS `devices` (
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `device_id_sha1` CHAR(40) NOT NULL,
    PRIMARY KEY (`thread_id` , `device_id`)
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1
PARTITION BY RANGE (thread_id)
(
 PARTITION start       VALUES LESS THAN (0)
, PARTITION future      VALUES LESS THAN MAXVALUE
);
