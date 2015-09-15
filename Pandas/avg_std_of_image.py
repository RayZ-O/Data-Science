#!/usr/bin/env python
# -*- coding: utf-8 -*-
from pylab import *
import pandas as pd
import re

# Day 6
# Print the average file size and the standard deviation for images 
# (.gif, .jpg and .jpeg files), which had response code 200

# file format example: 
# 1044,30/Apr/1998,22:46:12,/images/11104.gif,200,508

log_df = pd.read_csv('/home/datascience/Desktop/wc_day6_1_sample.csv',
names=['ClientID','Date','Time','URL','ResponseCode','Size'],
na_values=['-'])

r200_img_df = log_df[ (log_df['ResponseCode'] == 200) &
             (log_df['URL'].str.contains('\.gif$|\.jpg$|\.jpeg$', 
             flags=re.IGNORECASE, regex=True, na=False))]

print 'Average:', r200_img_df['Size'].mean(axis=1)
print 'Standard deviation:', r200_img_df['Size'].std(axis=1)

# output:
# Average: 3571.63762615
# Standard deviation: 8977.28117815