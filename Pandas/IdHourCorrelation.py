#!/usr/bin/env python
# -*- coding: utf-8 -*-
from pylab import *
import pandas as pd

#  Get 100 client ids from the dataset and generate a scatter plot
# that shows the hours of the day these clients sent requests

# file format example: 
# 1044,30/Apr/1998,22:46:12,/images/11104.gif,200,508

log_df = pd.read_csv('/home/datascience/Desktop/wc_day6_1_sample.csv',
names=['ClientID','Date','Time','URL','ResponseCode','Size'],
na_values=['-'])

selected_id = log_df['ClientID'].unique()[0:100]
uc_df = log_df[log_df['ClientID'].apply(lambda id: id in selected_id)]
uc_df['Hour'] = uc_df['Time'].apply(lambda time: int(str(time)[:2])) 
uc_df.plot(kind='scatter', x='Hour', y='ClientID')
show()