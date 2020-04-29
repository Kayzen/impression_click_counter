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
