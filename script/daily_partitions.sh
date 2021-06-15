#!/bin/bash
sudo supervisorctl stop impression_counter_aero
sudo supervisorctl stop impression_counter_ingestor

EXIT_STATUS=0

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

exit $EXIT_STATUS