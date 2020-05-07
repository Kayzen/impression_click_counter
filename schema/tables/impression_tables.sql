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
 PARTITION start       VALUES LESS THAN (0),
 PARTITION day20200507 VALUES LESS THAN (737917),
  PARTITION day20200508 VALUES LESS THAN (737918),
  PARTITION day20200509 VALUES LESS THAN (737919),
  PARTITION day20200510 VALUES LESS THAN (737920),
 PARTITION future      VALUES LESS THAN MAXVALUE
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
 PARTITION start       VALUES LESS THAN (0),
 PARTITION thread_p000 VALUES LESS THAN (1) ,
  PARTITION thread_p001 VALUES LESS THAN (2) ,
  PARTITION thread_p002 VALUES LESS THAN (3) ,
  PARTITION thread_p003 VALUES LESS THAN (4) ,
  PARTITION thread_p004 VALUES LESS THAN (5) ,
  PARTITION thread_p005 VALUES LESS THAN (6) ,
  PARTITION thread_p006 VALUES LESS THAN (7) ,
  PARTITION thread_p007 VALUES LESS THAN (8) ,
  PARTITION thread_p008 VALUES LESS THAN (9) ,
  PARTITION thread_p009 VALUES LESS THAN (10),
  PARTITION thread_p010 VALUES LESS THAN (11),
  PARTITION thread_p011 VALUES LESS THAN (12),
  PARTITION thread_p012 VALUES LESS THAN (13),
  PARTITION thread_p013 VALUES LESS THAN (14),
  PARTITION thread_p014 VALUES LESS THAN (15),
  PARTITION thread_p015 VALUES LESS THAN (16),
  PARTITION thread_p016 VALUES LESS THAN (17),
  PARTITION thread_p017 VALUES LESS THAN (18),
  PARTITION thread_p018 VALUES LESS THAN (19),
  PARTITION thread_p019 VALUES LESS THAN (20),
  PARTITION thread_p020 VALUES LESS THAN (21),
  PARTITION thread_p021 VALUES LESS THAN (22),
  PARTITION thread_p022 VALUES LESS THAN (23),
  PARTITION thread_p023 VALUES LESS THAN (24),
  PARTITION thread_p024 VALUES LESS THAN (25),
  PARTITION thread_p025 VALUES LESS THAN (26),
  PARTITION thread_p026 VALUES LESS THAN (27),
  PARTITION thread_p027 VALUES LESS THAN (28),
  PARTITION thread_p028 VALUES LESS THAN (29),
  PARTITION thread_p029 VALUES LESS THAN (30),
  PARTITION thread_p030 VALUES LESS THAN (31),
  PARTITION thread_p031 VALUES LESS THAN (32),
  PARTITION thread_p032 VALUES LESS THAN (33),
  PARTITION thread_p033 VALUES LESS THAN (34),
  PARTITION thread_p034 VALUES LESS THAN (35),
  PARTITION thread_p035 VALUES LESS THAN (36),
  PARTITION thread_p036 VALUES LESS THAN (37),
  PARTITION thread_p037 VALUES LESS THAN (38),
  PARTITION thread_p038 VALUES LESS THAN (39),
  PARTITION thread_p039 VALUES LESS THAN (40),
  PARTITION thread_p040 VALUES LESS THAN (41),
  PARTITION thread_p041 VALUES LESS THAN (42),
  PARTITION thread_p042 VALUES LESS THAN (43),
  PARTITION thread_p043 VALUES LESS THAN (44),
  PARTITION thread_p044 VALUES LESS THAN (45),
  PARTITION thread_p045 VALUES LESS THAN (46),
  PARTITION thread_p046 VALUES LESS THAN (47),
  PARTITION thread_p047 VALUES LESS THAN (48),
 PARTITION future      VALUES LESS THAN MAXVALUE
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
 PARTITION start       VALUES LESS THAN (0),
 PARTITION thread_p000 VALUES LESS THAN (1) ,
  PARTITION thread_p001 VALUES LESS THAN (2) ,
  PARTITION thread_p002 VALUES LESS THAN (3) ,
  PARTITION thread_p003 VALUES LESS THAN (4) ,
  PARTITION thread_p004 VALUES LESS THAN (5) ,
  PARTITION thread_p005 VALUES LESS THAN (6) ,
  PARTITION thread_p006 VALUES LESS THAN (7) ,
  PARTITION thread_p007 VALUES LESS THAN (8) ,
  PARTITION thread_p008 VALUES LESS THAN (9) ,
  PARTITION thread_p009 VALUES LESS THAN (10),
  PARTITION thread_p010 VALUES LESS THAN (11),
  PARTITION thread_p011 VALUES LESS THAN (12),
  PARTITION thread_p012 VALUES LESS THAN (13),
  PARTITION thread_p013 VALUES LESS THAN (14),
  PARTITION thread_p014 VALUES LESS THAN (15),
  PARTITION thread_p015 VALUES LESS THAN (16),
  PARTITION thread_p016 VALUES LESS THAN (17),
  PARTITION thread_p017 VALUES LESS THAN (18),
  PARTITION thread_p018 VALUES LESS THAN (19),
  PARTITION thread_p019 VALUES LESS THAN (20),
  PARTITION thread_p020 VALUES LESS THAN (21),
  PARTITION thread_p021 VALUES LESS THAN (22),
  PARTITION thread_p022 VALUES LESS THAN (23),
  PARTITION thread_p023 VALUES LESS THAN (24),
  PARTITION thread_p024 VALUES LESS THAN (25),
  PARTITION thread_p025 VALUES LESS THAN (26),
  PARTITION thread_p026 VALUES LESS THAN (27),
  PARTITION thread_p027 VALUES LESS THAN (28),
  PARTITION thread_p028 VALUES LESS THAN (29),
  PARTITION thread_p029 VALUES LESS THAN (30),
  PARTITION thread_p030 VALUES LESS THAN (31),
  PARTITION thread_p031 VALUES LESS THAN (32),
  PARTITION thread_p032 VALUES LESS THAN (33),
  PARTITION thread_p033 VALUES LESS THAN (34),
  PARTITION thread_p034 VALUES LESS THAN (35),
  PARTITION thread_p035 VALUES LESS THAN (36),
  PARTITION thread_p036 VALUES LESS THAN (37),
  PARTITION thread_p037 VALUES LESS THAN (38),
  PARTITION thread_p038 VALUES LESS THAN (39),
  PARTITION thread_p039 VALUES LESS THAN (40),
  PARTITION thread_p040 VALUES LESS THAN (41),
  PARTITION thread_p041 VALUES LESS THAN (42),
  PARTITION thread_p042 VALUES LESS THAN (43),
  PARTITION thread_p043 VALUES LESS THAN (44),
  PARTITION thread_p044 VALUES LESS THAN (45),
  PARTITION thread_p045 VALUES LESS THAN (46),
  PARTITION thread_p046 VALUES LESS THAN (47),
  PARTITION thread_p047 VALUES LESS THAN (48),
 PARTITION future      VALUES LESS THAN MAXVALUE
);
