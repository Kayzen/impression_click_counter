DROP PROCEDURE IF EXISTS execute_query;
DELIMITER $$
CREATE PROCEDURE execute_query(queryText TEXT)
BEGIN
	SET @query=queryText;
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;
-- 
DROP PROCEDURE IF EXISTS execute_query_with_rowcount;
DELIMITER $$
CREATE PROCEDURE execute_query_with_rowcount(queryText TEXT)
BEGIN
	SET @query=queryText;
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	select ROW_COUNT() into @rowcount_temp;
	DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;
--
--
DROP PROCEDURE IF EXISTS log_debug;
DELIMITER $$
CREATE PROCEDURE log_debug(queryText TEXT, _debug ENUM('true','false'))
BEGIN
	IF(_debug = 'true') THEN
		select CAST(queryText AS CHAR (250)) AS `query`;
	END IF;
END $$
DELIMITER ;
--
DROP PROCEDURE IF EXISTS log_error;
DELIMITER $$
CREATE PROCEDURE log_error(queryText TEXT)
BEGIN
	select CAST(queryText AS CHAR (250)) AS `ERROR`;
END $$
DELIMITER ;
--
DROP PROCEDURE IF EXISTS log_info;
DELIMITER $$
CREATE PROCEDURE log_info(queryText TEXT)
BEGIN
	select CAST(queryText AS CHAR (250)) AS `INFO`;
END $$
DELIMITER ;
--
