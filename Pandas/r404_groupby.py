#!/usr/bin/env python
# -*- coding: utf-8 -*-
import pandas as pd

# Print the number of requests per client that had HTTP return code 404

# file format example: 
# 1044,30/Apr/1998,22:46:12,/images/11104.gif,200,508
log_df = pd.read_csv('/home/datascience/Desktop/wc_day6_1_sample.csv',
names=['ClientID','Date','Time','URL','ResponseCode','Size'],
na_values=['-'])

print log_df[log_df['ResponseCode'] == 404].groupby('ClientID').size()
