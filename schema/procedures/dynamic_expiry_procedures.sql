DROP PROCEDURE IF EXISTS `expire_impression_counter_events`;
DELIMITER $$
CREATE PROCEDURE expire_impression_counter_events (_interval TINYINT,
									 _chunk_size INT)
BEGIN
	SET @EXECUTION_DAY_QUERY=CONCAT('SELECT DATE_FORMAT(CURRENT_DATE - INTERVAL ', _interval, ' DAY, \'%Y%m%d\') INTO @EXECUTION_DAY');
	CALL execute_query(@EXECUTION_DAY_QUERY);
	SET @EVENT_NAME=CONCAT('expire_impression_click_day_minus_', _interval);

	SET @today = (select DATE_FORMAT(NOW(),'%Y%m%d'));
	IF(@EXECUTION_DAY < @today) THEN
		SET @GET_LOCK =(select COALESCE(GET_LOCK(@EVENT_NAME, 600), 0));
		IF(@GET_LOCK = 1) THEN
	        SET @rowcount = 0;
	        SET @rowcount_temp = 0;
			    SET @start_id = 0;
	        SET @chunk_size = _chunk_size;
	        SET @end_id = @chunk_size;
			    SET @min_id = 0;
			    SET @max_id = 0;
			    SET @MIN_MAX_ID_QUERY = CONCAT('SELECT IF(min(id) IS NULL,-1,min(id)), IF(max(id) IS NULL,-1,max(id)) INTO @min_id, @max_id FROM device_impressions_daily WHERE day_int=',to_days(@EXECUTION_DAY));
	        SET @UPDATE_STATEMENT = CONCAT('UPDATE device_impressions_daily SET status =\'expired\' WHERE day_int=',to_days(@EXECUTION_DAY));
			    SET @UPDATE_STATEMENT = CONCAT(@UPDATE_STATEMENT, ' AND id>=REPLACE_START_ID AND id<=REPLACE_END_ID AND status=\'active\'');
          CALL execute_query(@MIN_MAX_ID_QUERY);

			
			  IF(@min_id != -1 AND @max_id != -1) THEN
				    SET @start_id = 0;
		        SET @end_id = IF(@max_id <= @chunk_size, @max_id+1, @chunk_size);
				-- Loop over chunks for update
				WHILE @start_id < @max_id+1 DO
					-- set query for this thread_id, start_id & end_id
					SET @DYNAMIC_QUERY=(SELECT REPLACE(@UPDATE_STATEMENT,'REPLACE_START_ID',@start_id));
					SET @DYNAMIC_QUERY=(SELECT REPLACE(@DYNAMIC_QUERY,'REPLACE_END_ID',@end_id));
					CALL execute_query_with_rowcount(@DYNAMIC_QUERY);
					SET @rowcount = @rowcount + @rowcount_temp;
					SET @start_id = @end_id;
					SET @end_id = @end_id + @chunk_size;
					SET @end_id = IF(@end_id >= @max_id, @max_id+1, @end_id);
				END WHILE;

				SELECT RELEASE_LOCK(@EVENT_NAME);
			END IF;
		END IF;
	END IF;
END $$
DELIMITER ;