#!/usr/bin/env python
# -*- coding: utf-8 -*-
from pylab import *
import pandas as pd

# Use merge to join Grades with the Classes table, and find out what class Alice got an A in.

Students = pd.DataFrame({'student_id': [1, 2], 'name': ['Alice', 'Bob']})
Grades = pd.DataFrame({'student_id': [1, 1, 2, 2], 'class_id': [1, 2, 1, 3], 'grade': ['A', 'C', 'B', 'B']})
Classes = pd.DataFrame({'class_id': [1, 2, 3], 'title': ['Math', 'English', 'Spanish']})

print pd.merge(pd.merge(Students, Grades, on='student_id'), Classes, on='class_id')
#     name  student_id  class_id grade    title
# 0  Alice           1         1     A     Math
# 1    Bob           2         1     B     Math
# 2  Alice           1         2     C  English
# 3    Bob           2         3     B  Spanish
