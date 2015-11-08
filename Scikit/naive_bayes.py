#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pylab
from sklearn.datasets import fetch_mldata
from sklearn.naive_bayes import GaussianNB
from sklearn.metrics import confusion_matrix

# Naive Bayes

DATA_PATH = '~/data'
mnist = fetch_mldata('MNIST original', data_home=DATA_PATH)
# Analyze Naive Bayes Model on the data set based on the training and test samples
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
model = GaussianNB().fit(train_sample, train_sample_labels)
# measure the model's classification accuracy on test_sample
model.score(test_sample, test_sample_labels)
# 0.60199999999999998

# visualize cases where the model makes erroneous predictions
preds = model.predict(test_sample)
errors = [i for i in xrange(0, len(test_sample)) if preds[i] != test_sample_labels[i]]
fig = pylab.figure()
for i, e in enumerate(errors[0:3]):
    fig.add_subplot(1, 3, i, title="misclassified as:" + str(int(preds[e])))
    img = test_sample[e]
    pylab.imshow(img.reshape(28, 28), cmap="Greys")

# Compute and plot the confusion matrix for test_sample
cm = confusion_matrix(test_sample_labels, preds)
print cm
# [[ 82   0   2   4   0   2   1   0   5   2]
#  [  0 105   0   1   0   0   3   0   5   0]
#  [  7   1  50  17   2   2   3   1  17   3]
#  [ 12   0   7  55   5   2   7   2   2   9]
#  [  4   0   5   1  55   1   4   5   1  22]
#  [  9   0   2  14  12  29   3   0  15   6]
#  [  2   0  10   1   4   0  76   0   2   0]
#  [  0   0   0   1   8   3   0  25   3  63]
#  [  2   1   1   4  18   6   3   1  41  21]
 # [  0   1   1   0   3   2   0   7   2  84]]
