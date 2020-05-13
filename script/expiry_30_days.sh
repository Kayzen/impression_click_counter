#!/bin/bash

EXIT_STATUS=0

mysql --defaults-file=/home/platform/.fcapdb2_ic -f impression_counter < /home/platform/impression_click_counter/schema/expiry_30_days/expiry_30_days.sql
if [ $? -ne 0 ];
then
    echo "Partition creation failed on Impression Counter"
    EXIT_STATUS=1
fi

exit $EXIT_STATUS