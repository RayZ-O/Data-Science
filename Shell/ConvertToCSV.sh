#!/bin/bash
# convert the log file into the CSV format 
# log file format:
# 0 - - [30/Apr/1998:22:00:02 +0000] "GET /images/home_intro.anim.gif HTTP/1.0" 200 60349
# 1 - - [30/Apr/1998:22:00:03 +0000] "GET /images/home_bg_stars.gif HTTP/1.0" 200 2557
# 1 - - [30/Apr/1998:22:00:03 +0000] "GET /images/home_fr_phrase.gif HTTP/1.0" 200 2843

# sed, cut & awk
head wc_day6_1.log | sed 's/\[//g; s/ +0000\]//g; s/"//g; s/Apr/04/g; s/\([0-9]\{2\}\)\/\(.*\)\/\([0-9]\{4\}\):/\3-\2-\1 /g' \
| cut -d ' ' -f 1,4,5,6,7,9,10 | awk '{print $1","$2","$3","$5","$6","$7","$4}'

# sed only
head wc_day6_1.log | sed 's/\[//g; s/ +0000\]//g; s/"//g; s/Apr/04/g; s/\([0-9]\{2\}\)\/\(.*\)\/\([0-9]\{4\}\):/\3-\2-\1 /g' \
| sed 's/\([^ ]*\) - - \([^ ]* [^ ]*\) \([^ ]*\) \([^ ]*\) \([^ ]*\) \([^ ]* [^ ]*\)$/\1 \2 \4 \6 \3/g; s/ /,/g'
