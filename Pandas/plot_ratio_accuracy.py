#!/usr/bin/env python
# -*- coding: utf-8 -*-
import pylab
import pandas as pd
from numpy import arange
import Levenshtein as L

resto = pd.read_csv('restaurants.csv')

clusters = pd.merge(resto, resto, on='cluster')
clusters = clusters[clusters.id_x < clusters.id_y]

resto['dummy'] = 0
prod = pd.merge(resto, resto, on='dummy')
# Clean up
del prod['dummy']
del resto['dummy']
prod = prod[prod.id_x < prod.id_y]
# Add ratio column
prod['ratio'] = prod.apply(lambda r: L.ratio(r['name_x'], r['name_y']), axis=1)

def accuracy(max_ratio):
    similar = prod[prod.ratio >= max_ratio]
    correct = float(sum(similar.cluster_x == similar.cluster_y))
    precision = correct / len(similar) if len(similar) > 0 else 0
    recall = correct / len(clusters)
    return (precision, recall)

thresholds = arange(0.1, 1.1, 0.1)
p = []
r = []

for t in thresholds:
    acc = accuracy(t)
    p.append(acc[0])
    r.append(acc[1])

pylab.plot(thresholds, p)
pylab.plot(thresholds, r)
pylab.legend(['precision', 'recall'], loc='upper left')

# pylab.xlabel('recall')
# pylab.ylabel('precision')
# pylab.scatter(r, p)

pylab.title('ratio accuracy')
pylab.show()