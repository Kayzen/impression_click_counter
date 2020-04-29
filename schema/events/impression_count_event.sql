DROP EVENT IF EXISTS expire_impressions_day_minus_15;
DELIMITER $$
CREATE EVENT `expire_impressions_day_minus_15`
ON SCHEDULE EVERY 2 MINUTE STARTS current_timestamp() DO
BEGIN
	CALL expire_fcap_events('impression', 15, 50000);
END$$
DELIMITER ;