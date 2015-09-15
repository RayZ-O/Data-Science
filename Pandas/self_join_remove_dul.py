#!/usr/bin/env python
# -*- coding: utf-8 -*-
from pylab import *
import pandas as pd

# If records A and B match each other, then self-join will get both (A, B) and (B, A) in the 
# output. Filter clusters so that only keep one instance of each matching pair
resto = pd.read_csv('restaurants.csv')

clusters = pd.merge(resto, resto, on='cluster')
clusters = clusters[clusters.id_x < clusters.id_y]
print clusters[:10]