DROP TRIGGER IF EXISTS device_impressions_daily_ai;
DELIMITER $$
CREATE TRIGGER  device_impressions_daily_ai
AFTER INSERT ON device_impressions_daily
FOR EACH ROW
BEGIN
	CALL handle_impression_count(NEW.app_object_id,  NEW.logType, NEW.thread_id, NEW.device_id,NEW.status, 'insert_trigger');
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS device_impressions_daily_au;
DELIMITER $$
CREATE TRIGGER  device_impressions_daily_au
AFTER UPDATE ON device_impressions_daily
FOR EACH ROW
BEGIN
	IF(OLD.status != NEW.status) THEN
	    CALL handle_impression_count(NEW.app_object_id, NEW.logType, NEW.thread_id, NEW.device_id, NEW.status, 'update_trigger');
	END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS device_impression_counts_ai;
DELIMITER $$
CREATE TRIGGER  device_impression_counts_ai
AFTER INSERT ON device_impression_counts
FOR EACH ROW
BEGIN
		IF(NEW.impression_count > 0) THEN
		  CALL add_to_aerospike_impression_count_data(NEW.app_object_id, NEW.thread_id, NEW.device_id,NEW.impression_count,'impression', 'add');
		ELSEIF (NEW.click_count > 0) THEN
		  CALL add_to_aerospike_impression_count_data(NEW.app_object_id, NEW.thread_id, NEW.device_id,NEW.click_count,'click', 'add');
		END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS device_impression_counts_au;
DELIMITER $$
CREATE TRIGGER  device_impression_counts_au
AFTER UPDATE ON device_impression_counts
FOR EACH ROW
	BEGIN
	  IF (NEW.impression_count != OLD.impression_count) THEN
	    IF(NEW.impression_count > 0) THEN
    		CALL add_to_aerospike_impression_count_data(NEW.app_object_id, NEW.thread_id, NEW.device_id,NEW.impression_count,'impression','add');
      ELSEIF (NEW.impression_count = 0) THEN
    		CALL add_to_aerospike_impression_count_data(NEW.app_object_id, NEW.thread_id, NEW.device_id,0,'impression','remove');
    	END IF;
    END IF;

    IF (NEW.click_count != OLD.click_count) THEN
    	IF(NEW.click_count > 0) THEN
        CALL add_to_aerospike_impression_count_data(NEW.app_object_id, NEW.thread_id, NEW.device_id,NEW.click_count,'click','add');
      ELSEIF (NEW.click_count = 0) THEN
        CALL add_to_aerospike_impression_count_data(NEW.app_object_id, NEW.thread_id, NEW.device_id,0,'click','remove');
      END IF;
    END IF;
END $$
DELIMITER ;