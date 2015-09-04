#!/bin/bash
# Get the total number of appearance of different protocols in descending order.
# log file format:
# 0 - - [30/Apr/1998:22:00:02 +0000] "GET /images/home_intro.anim.gif HTTP/1.0" 200 60349
# 1 - - [30/Apr/1998:22:00:03 +0000] "GET /images/home_bg_stars.gif HTTP/1.0" 200 2557
# 1 - - [30/Apr/1998:22:00:03 +0000] "GET /images/home_fr_phrase.gif HTTP/1.0" 200 2843

cut -d ' ' -f 8  wc_day6_1.log | sort | uniq -c | sed 's/"//g'

