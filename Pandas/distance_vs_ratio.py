#!/usr/bin/env python
# -*- coding: utf-8 -*-
import pylab
import pandas as pd
from numpy import arange
import Levenshtein as L

def dis_accuracy(max_distance):
	similar = prod[prod.distance < max_distance]
	correct = float(sum(similar.cluster_x == similar.cluster_y))
	precision = correct / len(similar)
	recall = correct / len(clusters)
	return (precision, recall)


def ratio_accuracy(max_ratio):
    similar = prod[prod.ratio >= max_ratio]
    correct = float(sum(similar.cluster_x == similar.cluster_y))
    precision = correct / len(similar) if len(similar) > 0 else 0
    recall = correct / len(clusters)
    return (precision, recall)

def fill(thresholds, p_lst, r_lst, accuracy):
	for t in thresholds:
	    acc = accuracy(t)
	    p_lst.append(acc[0])
	    r_lst.append(acc[1])

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
prod['distance'] = prod.apply(lambda recall: L.distance(recall['name_x'], recall['name_y']), axis=1)
prod['ratio'] = prod.apply(lambda ratio_r: L.ratio(ratio_r['name_x'], ratio_r['name_y']), axis=1)

thresholds = range(1, 11)
precision = []
recall = []
fill(thresholds, precision, recall, dis_accuracy)
pylab.plot(recall, precision)

thresholds = arange(0.0, 1.1, 0.1)
precision = []
recall = []
fill(thresholds, precision, recall, ratio_accuracy)
pylab.plot(recall, precision)

pylab.legend(['distance', 'ratio'], loc='upper right')
pylab.title('ratio vs. distance')
pylab.xlabel('recall')
pylab.ylabel('precision')

pylab.show()