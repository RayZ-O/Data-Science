#!/usr/bin/env python
# -*- coding: utf-8 -*-
from pylab import *
import pandas as pd

# Generate a plot of the number of users of the site every hour.

# file format example: 
# 1044,30/Apr/1998,22:46:12,/images/11104.gif,200,508

log_df = pd.read_csv('/home/datascience/Desktop/wc_day6_1_sample.csv',
names=['ClientID','Date','Time','URL','ResponseCode','Size'],
na_values=['-'])

hour_grouped = log_df.groupby(lambda row: int(str(log_df['Time'][row])[:2]))
pl = hour_grouped['ClientID'].nunique().plot()
pl.set_xlabel("Hour")
pl.set_ylabel("Number of users")
show()

# Alternative solution if complex datetime operation is needed
# log_df['DateTime'] = pd.to_datetime(log_df.apply(lambda row: row['Date'] + ' ' + row['Time'], axis=1)) 
# hour_grouped = log_df.groupby(lambda row: log_df['DateTime'][row].hour)
# pl = hour_grouped['ClientID'].nunique().plot()
# pl.set_xlabel("Hour")
# pl.set_ylabel("Number of users")
# show()