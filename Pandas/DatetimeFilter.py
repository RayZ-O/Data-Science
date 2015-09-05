#!/usr/bin/env python
# -*- coding: utf-8 -*-
from pylab import *
import pandas as pd

# Print how many requests were made on May 1st before and after noon (12:00:00)

# file format example: 
# 1044,30/Apr/1998,22:46:12,/images/11104.gif,200,508
# log_df = pd.read_csv('/home/datascience/Desktop/small.csv',
log_df = pd.read_csv('/home/datascience/Desktop/wc_day6_1_sample.csv',
names=['ClientID','Date','Time','URL','ResponseCode','Size'],
na_values=['-'])

may1_df = log_df[log_df['Date'] == '01/May/1998']

print 'Before noon:', may1_df[may1_df['Time'] < '12:00:00'].shape[0]
print 'After noon:', may1_df[may1_df['Time'] >= '12:00:00'].shape[0]

# Another solution, print the same result but much slower
# may1_df['DateTime'] = pd.to_datetime(may1_df.apply(lambda row: row['Date'] + ' ' + row['Time'], axis=1))

# print 'Before noon:', may1_df[may1_df.apply(lambda row: row['DateTime'].hour < 12, axis=1)].shape[0]
# print 'After noon:', may1_df[may1_df.apply(lambda row: row['DateTime'].hour >= 12, axis=1)].shape[0]