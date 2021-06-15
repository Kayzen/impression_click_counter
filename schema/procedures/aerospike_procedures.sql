DROP PROCEDURE IF EXISTS `add_to_aerospike_impression_count_data`;

DELIMITER $$
CREATE PROCEDURE `add_to_aerospike_impression_count_data` (_app_object_id   INT,
                                              _thread_id   TINYINT,
                                              _device_id   VARCHAR(512),
                                              _imp_click_count INT,
                                              _logType ENUM('impression','click'),
                                              _action      ENUM('add', 'remove'))
BEGIN
  CASE _thread_id MOD 48
    WHEN  0 THEN
      INSERT INTO aerospike_impression_count_data_0  (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN  1 THEN
      INSERT INTO aerospike_impression_count_data_1  (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN  2 THEN
      INSERT INTO aerospike_impression_count_data_2  (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN  3 THEN
      INSERT INTO aerospike_impression_count_data_3  (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN  4 THEN
      INSERT INTO aerospike_impression_count_data_4  (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN  5 THEN
      INSERT INTO aerospike_impression_count_data_5  (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN  6 THEN
      INSERT INTO aerospike_impression_count_data_6  (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN  7 THEN
      INSERT INTO aerospike_impression_count_data_7  (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN  8 THEN
      INSERT INTO aerospike_impression_count_data_8  (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN  9 THEN
      INSERT INTO aerospike_impression_count_data_9  (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 10 THEN
      INSERT INTO aerospike_impression_count_data_10 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 11 THEN
      INSERT INTO aerospike_impression_count_data_11 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 12 THEN
      INSERT INTO aerospike_impression_count_data_12 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 13 THEN
      INSERT INTO aerospike_impression_count_data_13 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 14 THEN
      INSERT INTO aerospike_impression_count_data_14 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 15 THEN
      INSERT INTO aerospike_impression_count_data_15 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 16 THEN
      INSERT INTO aerospike_impression_count_data_16 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 17 THEN
      INSERT INTO aerospike_impression_count_data_17 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 18 THEN
      INSERT INTO aerospike_impression_count_data_18 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 19 THEN
      INSERT INTO aerospike_impression_count_data_19 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 20 THEN
      INSERT INTO aerospike_impression_count_data_20 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 21 THEN
      INSERT INTO aerospike_impression_count_data_21 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 22 THEN
      INSERT INTO aerospike_impression_count_data_22 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 23 THEN
      INSERT INTO aerospike_impression_count_data_23 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 24 THEN
      INSERT INTO aerospike_impression_count_data_24 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 25 THEN
      INSERT INTO aerospike_impression_count_data_25 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 26 THEN
      INSERT INTO aerospike_impression_count_data_26 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 27 THEN
      INSERT INTO aerospike_impression_count_data_27 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 28 THEN
      INSERT INTO aerospike_impression_count_data_28 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 29 THEN
      INSERT INTO aerospike_impression_count_data_29 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 30 THEN
      INSERT INTO aerospike_impression_count_data_30 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 31 THEN
      INSERT INTO aerospike_impression_count_data_31 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 32 THEN
      INSERT INTO aerospike_impression_count_data_32 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 33 THEN
      INSERT INTO aerospike_impression_count_data_33 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 34 THEN
      INSERT INTO aerospike_impression_count_data_34 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 35 THEN
      INSERT INTO aerospike_impression_count_data_35 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 36 THEN
      INSERT INTO aerospike_impression_count_data_36 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 37 THEN
      INSERT INTO aerospike_impression_count_data_37 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 38 THEN
      INSERT INTO aerospike_impression_count_data_38 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 39 THEN
      INSERT INTO aerospike_impression_count_data_39 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 40 THEN
      INSERT INTO aerospike_impression_count_data_40 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 41 THEN
      INSERT INTO aerospike_impression_count_data_41 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 42 THEN
      INSERT INTO aerospike_impression_count_data_42 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 43 THEN
      INSERT INTO aerospike_impression_count_data_43 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 44 THEN
      INSERT INTO aerospike_impression_count_data_44 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 45 THEN
      INSERT INTO aerospike_impression_count_data_45 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 46 THEN
      INSERT INTO aerospike_impression_count_data_46 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    WHEN 47 THEN
      INSERT INTO aerospike_impression_count_data_47 (day_int,  app_object_id, thread_id, device_id, action, imp_click_count, logType) VALUES (to_days(curdate()), _app_object_id, _thread_id, _device_id, _action, _imp_click_count, _logType);
    ELSE BEGIN END;
  END CASE;
END $$
DELIMITER ;
