#!/usr/bin/env python
# -*- coding: utf-8 -*-
import pylab
import pandas as pd
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
# Add distance column
prod['distance'] = prod.apply(lambda r: L.distance(r['name_x'], r['name_y']), axis=1)

def accuracy(max_distance):
    similar = prod[prod.distance < max_distance]
    correct = float(sum(similar.cluster_x == similar.cluster_y))
    precision = correct / len(similar)
    recall = correct / len(clusters)
    return (precision, recall)

thresholds = range(1, 11)
p = []
r = []

for t in thresholds:
    acc = accuracy(t)
    p.append(acc[0])
    r.append(acc[1])

pylab.xlabel('recall')
pylab.ylabel('precision')
pylab.title('distance accuracy')
pylab.scatter(r, p)

pylab.show()