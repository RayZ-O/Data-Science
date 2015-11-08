#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pylab
from sklearn.datasets import fetch_mldata

# Use imshow to visualize a number of images from sample. 

DATA_PATH = '~/data'
mnist = fetch_mldata('MNIST original', data_home=DATA_PATH)

fig = pylab.figure()
sample = [mnist.data[i] for i in xrange(0, 60000, 5000)]
for i, img in enumerate(sample):
    fig.add_subplot(1, len(sample), i, xticklabels=[], yticklabels=[])
    pylab.imshow(img.reshape(28, 28), cmap="Greys")