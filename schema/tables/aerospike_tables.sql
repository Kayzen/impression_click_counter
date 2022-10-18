DROP TABLE IF EXISTS `aerospike_impression_count_data_0`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_0` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_1`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_1` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_2`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_2` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_3`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_3` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_4`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_4` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_5`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_5` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_6`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_6` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_7`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_7` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_8`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_8` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_9`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_9` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_10`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_10` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_11`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_11` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_12`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_12` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_13`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_13` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_14`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_14` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_15`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_15` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_16`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_16` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_17`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_17` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_18`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_18` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_19`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_19` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_20`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_20` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_21`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_21` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_22`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_22` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_23`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_23` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_24`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_24` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_25`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_25` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_26`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_26` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_27`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_27` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_28`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_28` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_29`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_29` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_30`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_30` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_31`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_31` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_32`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_32` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_33`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_33` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_34`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_34` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_35`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_35` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_36`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_36` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_37`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_37` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_38`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_38` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_39`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_39` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_40`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_40` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_41`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_41` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_42`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_42` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_43`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_43` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_44`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_44` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_45`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_45` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_46`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_46` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

DROP TABLE IF EXISTS `aerospike_impression_count_data_47`;
CREATE TABLE IF NOT EXISTS `aerospike_impression_count_data_47` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `day_int` INT(11) NOT NULL,
    `app_object_id` INT(11) NOT NULL,
    `thread_id` TINYINT(3) UNSIGNED NOT NULL,
    `device_id` INT(10) UNSIGNED NOT NULL,
    `imp_click_count` INT NOT NULL DEFAULT 0,
    `logType` ENUM('impression','click') NOT NULL,
    `action` ENUM('add', 'remove') NOT NULL,
    `dca_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `hkg_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
        `ams_status` ENUM('pending', 'processed') NOT NULL DEFAULT 'pending',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` , `day_int`),
    KEY `dca_status_idx` (`dca_status`),KEY `hkg_status_idx` (`hkg_status`),KEY `ams_status_idx` (`ams_status`),
    KEY `entity_id_idx` (`app_object_id`)
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

