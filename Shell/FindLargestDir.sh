#!/bin/bash
#Find the 3th largest directory in the home directory /home/datascience

find /home/datascience/ -mindepth 1 -maxdepth 1 -type d | xargs du -hs | sort -rh | sed -n '3p'