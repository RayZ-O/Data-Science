#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pylab
from sklearn.datasets import fetch_mldata
from sklearn.neighbors import NearestNeighbors

# Use the nearest neighbors model to inspect results for images in the test set.

# Display several images in a row
def show(imgs, n=1):
    fig = pylab.figure()
    for i in xrange(0, n):
        fig.add_subplot(1, n, i, xticklabels=[], yticklabels=[])
        if n == 1:
            img = imgs
        else:
            img = imgs[i]
        pylab.imshow(img.reshape(28, 28), cmap="Greys")

DATA_PATH = '~/data'
mnist = fetch_mldata('MNIST original', data_home=DATA_PATH)

train = mnist.data[:60000]
test = mnist.data[60000:]

model = NearestNeighbors(algorithm='brute').fit(train)
for i in xrange(0, 10000, 1000):
    query_img = test[i]
    _, result = model.kneighbors(query_img, n_neighbors=4)
    show(train[result[0],:], len(result[0]))