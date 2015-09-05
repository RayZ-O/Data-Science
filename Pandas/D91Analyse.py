#!/usr/bin/env python
# -*- coding: utf-8 -*-
from pylab import *
import pandas as pd
import re

# Day 91
# Print the average file size and the standard deviation for images 
# (.gif, .jpg and .jpeg files), which had response code 200

# file format example: 
# 1044 30/Apr/1998 22:46:12 /images/11104.gif 200 508
# Seperate by space because several URIs contain comma

log_df = pd.read_csv('/home/datascience/Desktop/wc_day91_1.csv',
sep=' ',
names=['ClientID','Date','Time','URL','ResponseCode','Size'],
na_values=['-'])

r200_img_df = log_df[ (log_df['ResponseCode'] == 200) &
             (log_df['URL'].str.contains('.gif$|.jpg$|.jpeg$', 
             flags=re.IGNORECASE, regex=True, na=False))]

print 'Average:', r200_img_df['Size'].mean(axis=1)
print 'Standard deviation:', r200_img_df['Size'].std(axis=1)

# output: 
# Average: 3211.20639682
# Standard deviation: 6290.60162517

hour_grouped = log_df.groupby(lambda row: int(str(log_df['Time'][row])[:2]))
pl = hour_grouped['ClientID'].nunique().plot()
pl.set_xlabel("Hour")
pl.set_ylabel("Number of users")
show()

