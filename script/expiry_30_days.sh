#!/bin/bash

EXIT_STATUS=0

mysql --defaults-file=/home/platform/.fcapdb2_ic -f impression_counter < /home/platform/impression_click_counter/schema/expiry_30_days/expiry_30_days.sql
if [ $? -ne 0 ];
then
    echo "Expiry of 30 day old Impression and Click Failed"
    EXIT_STATUS=1
fi

exit $EXIT_STATUS