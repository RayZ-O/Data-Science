#!/bin/bash
# Find the 3 largest files in directory /usr/bin that have file names starting with "grub"

find /usr/bin/ -type f -name 'grub*'| xargs du -h | sort -rh | head -n 3