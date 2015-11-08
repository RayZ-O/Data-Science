#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pylab
from sklearn.datasets import fetch_mldata
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import confusion_matrix

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

# KNN Classification

DATA_PATH = '~/data'
mnist = fetch_mldata('MNIST original', data_home=DATA_PATH)

train = mnist.data[:60000]
test = mnist.data[60000:]
train_labels = mnist.target[:60000]
test_labels = mnist.target[60000:]
test_labels_sample = test_labels[::100]

model = KNeighborsClassifier(n_neighbors=4, algorithm='brute').fit(train, train_labels)
# measure the model's classification accuracy on test_sample
model.score(test_sample, test_labels_sample)
# 0.96999999999999997
# visualize the nearest neighbors of cases where the model makes erroneous predictions
preds = model.predict(test_sample)
errors = [i for i in xrange(0, len(test_sample)) if preds[i] != test_labels_sample[i]]

for i in errors:
    query_img = test_sample[i]
    _, result = model.kneighbors(query_img, n_neighbors=4)
    show(query_img)
    show(train[result[0],:], len(result[0]))

test_sample = test[::10]
test_labels_sample = test_labels[::10]
preds = model.predict(test_sample)
# Compute and plot the confusion matrix for test_sample
cm = confusion_matrix(test_labels_sample, preds)
print cm
# [[ 98   0   0   0   0   0   0   0   0   0]
#  [  0 114   0   0   0   0   0   0   0   0]
#  [  1   4  93   0   0   0   0   5   0   0]
#  [  0   0   0  96   0   0   0   4   1   0]
#  [  0   2   0   0  92   0   3   0   0   1]
#  [  0   0   0   3   0  86   1   0   0   0]
#  [  0   0   0   0   1   0  94   0   0   0]
#  [  0   2   0   0   0   0   0 100   0   1]
#  [  1   2   0   2   2   3   0   1  87   0]
#  [  0   1   0   2   0   0   1   0   0  96]]
