#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pylab
from sklearn.datasets import fetch_mldata
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import confusion_matrix

# logistic regression

DATA_PATH = '~/data'
mnist = fetch_mldata('MNIST original', data_home=DATA_PATH)
# Analyze logistic regression model on the data set based on the training and test samples below
train = mnist.data[:60000]
train_labels = mnist.target[:60000]

test = mnist.data[60000:]
test_labels = mnist.target[60000:]

# For traning
train_sample = train[::100]
train_sample_labels = train_labels[::100]
# for testing
test_sample = test[::10]
test_sample_labels = test_labels[::10]
# use train_sample to train the model and test_sample to test the accuracy and get confusion matrix
model = LogisticRegression().fit(train_sample, train_sample_labels)
# measure the model's classification accuracy on test_sample
model.score(test_sample, test_sample_labels)
# 0.82199999999999995
# Compute and plot the confusion matrix for test_sample
preds = model.predict(test_sample)
cm = confusion_matrix(test_sample_labels, preds)
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
