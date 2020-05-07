DROP PROCEDURE IF EXISTS `manage_partition`;
DELIMITER $$
CREATE PROCEDURE `manage_partition` (_table_name VARCHAR(256),
								     _operator CHAR(1),
								     _interval TINYINT,
								     _action ENUM('create', 'drop'),
								     _debug ENUM('true','false'))
BEGIN
	CALL log_debug(CONCAT('manage_partition:_table_name=',_table_name,',_operator=\'',_operator,'\',_interval=',_interval,',_action=',_action,',_debug=',_debug), _debug);

	SET @PARTITION_DATE_QUERY=CONCAT('SELECT DATE_FORMAT(CURRENT_DATE ', _operator, ' INTERVAL ', _interval, ' DAY, \'%Y%m%d\') INTO @PARTITION_DATE');
	CALL log_debug(@PARTITION_DATE_QUERY, _debug);
	CALL execute_query(@PARTITION_DATE_QUERY);

	SET @PARTITION_NAME=CONCAT('day', @PARTITION_DATE);

	SET @CHECK_PARTITION ='SELECT EXISTS(SELECT 1 FROM information_schema.partitions WHERE TABLE_SCHEMA = \'fpadb\' AND TABLE_NAME = \'';
	SET @CHECK_PARTITION = CONCAT(@CHECK_PARTITION, _table_name, '\' AND PARTITION_NAME = \'');
	SET @CHECK_PARTITION = CONCAT(@CHECK_PARTITION, @PARTITION_NAME, '\') INTO @PARTITION_EXISTS');

	CALL log_debug(@CHECK_PARTITION, _debug);
	CALL execute_query(@CHECK_PARTITION);

	SET @EVENT_NAME=CONCAT(_action, 'partition_',_table_name, '_', _interval);
	CALL log_debug(@EVENT_NAME, _debug);
	SET @INSERT_TIME=(select DATE_FORMAT(NOW(),'%Y%m%d%H%i'));

	IF(@PARTITION_EXISTS = 1) THEN
		IF(_action = 'drop') THEN
			CALL log_debug(CONCAT(@PARTITION_NAME, ' exists. Will attempt to drop partition'), _debug);
			SET @DROP_PARTITION = CONCAT('ALTER TABLE ', _table_name, ' DROP PARTITION ', @PARTITION_NAME);
			CALL log_debug(@DROP_PARTITION, _debug);
			CALL execute_query(@DROP_PARTITION);
			SET @INSERT_TIME=(select DATE_FORMAT(NOW(),'%Y%m%d%H%i'));
			CALL log_debug(CONCAT(@PARTITION_NAME, ' partition droped'), _debug);
		ELSE
			CALL log_info(CONCAT(@PARTITION_NAME, ' exists. Nothing to do for create.'));
		END IF;
	ELSEIF(@PARTITION_EXISTS = 0) THEN
		IF(_action = 'create') THEN
			CALL log_debug(CONCAT(@PARTITION_NAME, ' does not exists. Will attempt to create partition'), _debug);
			SET @CREATE_PARTITION = CONCAT('ALTER TABLE ', _table_name, ' REORGANIZE PARTITION future INTO (PARTITION ');
			SET @CREATE_PARTITION = CONCAT(@CREATE_PARTITION, @PARTITION_NAME, ' VALUES LESS THAN (TO_DAYS(\'');
			SET @CREATE_PARTITION = CONCAT(@CREATE_PARTITION, @PARTITION_DATE, 	'\')), PARTITION future VALUES LESS THAN MAXVALUE)');
			CALL log_debug(@CREATE_PARTITION, _debug);
			CALL execute_query(@CREATE_PARTITION);
			SET @INSERT_TIME=(select DATE_FORMAT(NOW(),'%Y%m%d%H%i'));
			CALL log_debug(CONCAT(@PARTITION_NAME, ' partition created'), _debug);
		ELSE
			CALL log_info(CONCAT(@PARTITION_NAME, ' does not exists. Nothing to do for drop.'));
		END IF;
	END IF;
END $$
DELIMITER ;