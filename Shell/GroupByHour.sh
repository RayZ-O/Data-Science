#!/bin/bash
# Count the number of protocol "HTTP/1.0" used in each hour of the day
# log file format:
# 0 - - [30/Apr/1998:22:00:02 +0000] "GET /images/home_intro.anim.gif HTTP/1.0" 200 60349
# 1 - - [30/Apr/1998:22:00:03 +0000] "GET /images/home_bg_stars.gif HTTP/1.0" 200 2557
# 1 - - [30/Apr/1998:22:00:03 +0000] "GET /images/home_fr_phrase.gif HTTP/1.0" 200 2843

grep 'HTTP/1.0' wc_day6_1.log | cut -d ' ' -f 4 | cut -d ':' -f 2 | sort -n | uniq -c