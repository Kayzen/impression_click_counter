#!/bin/bash

EXIT_STATUS=0

expiry_date=`date -d "30 days ago" +%Y%m%d`
mysql --defaults-file=/home/platform/.fcapdb2_ic -f impression_counter < /home/platform/impression_click_counter/schema/expiry_30_days/expiry_30_days.sql
mysql --defaults-file=/home/platform/.fcapdb2_ic -f impression_counter -e "SELECT count(*) from device_impressions_daily where day_int= to_days(DATE_FORMAT(CURRENT_DATE - INTERVAL 30 DAY, '%Y%m%d')) and status='active';" > /tmp/new_ic_expiry_$expiry_date.txt
diff /home/platform/impression_click_counter/new_ic_expiry.txt /tmp/new_ic_expiry_$expiry_date.txt
if [ $? -ne 0 ];
then
    echo "Expiry of 30 day old Impression and Click Failed"
    EXIT_STATUS=1
else
	echo "Successfully expired 30 day old Impression and Clicks"
fi

exit $EXIT_STATUS