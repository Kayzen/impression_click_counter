#!/bin/bash
day=$(date +"%Y-%m-%d" -d"7 day ago")
echo "Drop $day old Aerospike-Imp partition"
mysql --defaults-file=/home/platform/.fcapdb2_ic -f impression_counter < /home/platform/impression_click_counter/schema/partitions/daily_drop.sql
if [ $? -ne 0 ];
then
    echo "Partition creation failed on Impression Counter"
    exit 1
fi
