DROP PROCEDURE IF EXISTS `handle_impression_count`;
DELIMITER $$
CREATE PROCEDURE `handle_impression_count` (_app_object_id INT,
                                 _logType ENUM('impression','click'),
															   _thread_id TINYINT,
															   _device_id INT,
															   _status ENUM('active','expired'),
															   _called_by ENUM('insert_trigger','update_trigger'))
BEGIN
	
	IF(_called_by = 'insert_trigger') THEN
	  IF(_logType = 'impression') THEN
		  SET @counter = IF(_status = 'active', 1, 0);
		  INSERT INTO device_impression_counts ( app_object_id,  thread_id,  device_id,impression_count)
    											    VALUES (_app_object_id, _thread_id, _device_id,@counter)
    		    ON DUPLICATE KEY UPDATE impression_count=impression_count+@counter;
		END IF;

		IF(_logType = 'click') THEN
    		  SET @counter = IF(_status = 'active', 1, 0);
    		  INSERT INTO device_impression_counts ( app_object_id,  thread_id,  device_id,click_count)
        											    VALUES (_app_object_id, _thread_id, _device_id,@counter)
        		    ON DUPLICATE KEY UPDATE click_count=click_count+@counter;
    END IF;

	END IF;

	IF(_called_by = 'update_trigger') THEN
	  IF(_logType = 'impression') THEN
      SET @counter = IF(_status = 'expired', -1, 0);

      UPDATE device_impression_counts
      SET    impression_count=impression_count+@counter
      WHERE  app_object_id=_app_object_id
      AND    thread_id = _thread_id
      AND    device_id=_device_id;
		END IF;

		IF(_logType = 'click') THEN
          SET @counter = IF(_status = 'expired', -1, 0);

          UPDATE device_impression_counts
          SET    click_count=click_count+@counter
          WHERE  app_object_id=_app_object_id
          AND    thread_id = _thread_id
          AND    device_id=_device_id;
    END IF;

	END IF;
END $$
DELIMITER ;
