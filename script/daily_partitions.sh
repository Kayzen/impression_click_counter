#!/bin/bash
echo `date +"%Y-%m-%d" -d"15 day ago"` > /home/platform/impression-counter_today_minus_15.txt
diff /home/platform/jenkin_last_run.txt /home/platform/impression-counter_today_minus_15.txt
if [ $? -ne 0 ];
then
    echo "Partition creation wont run for Impression-Counter today"
    exit 0
else
    echo "Daily partitions on Impression-Counter will be run today"
fi

sudo supervisorctl stop impression_counter_aero
sudo supervisorctl stop impression_counter_ingestor

EXIT_STATUS=0

now=$(date)
echo "Starting Impression-Counter partition creation :$now"
mysql --defaults-file=/home/platform/.fcapdb2_ic -f impression_counter < /home/platform/impression_click_counter/schema/partitions/daily_partitions.sql
if [ $? -ne 0 ];
then
    echo "Partition creation failed on Impression Counter"
    EXIT_STATUS=1
fi

partition_name=day`date +%Y%m%d`
mysql --defaults-file=/home/platform/.fcapdb2_ic impression_counter -e "SELECT TABLE_NAME FROM information_schema.partitions WHERE TABLE_SCHEMA='impression_counter' and PARTITION_NAME='$partition_name' order by TABLE_NAME;" > /tmp/new_ic_paritions_$partition_name.txt
diff /home/platform/impression_click_counter/expected_new_daily_partitions.txt /tmp/new_ic_paritions_$partition_name.txt
if [ $? -ne 0 ];
then
    echo "Partition creation failed on Impression Counter"
    EXIT_STATUS=1
else
	echo "Successfully created daily partitions on Impression Counter"
fi

sudo supervisorctl start impression_counter_aero
sudo supervisorctl start impression_counter_ingestor

echo `date +"%Y-%m-%d"` > /home/platform/jenkin_last_run.txt
exit $EXIT_STATUS
