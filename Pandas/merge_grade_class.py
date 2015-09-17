#!/usr/bin/env python
# -*- coding: utf-8 -*-
import pandas as pd

# Use merge to join Grades with the Classes table, and find out what class Alice got an A in.

Students = pd.DataFrame({'student_id': [1, 2], 'name': ['Alice', 'Bob']})
Grades = pd.DataFrame({'student_id': [1, 1, 2, 2], 'class_id': [1, 2, 1, 3], 'grade': ['A', 'C', 'B', 'B']})
Classes = pd.DataFrame({'class_id': [1, 2, 3], 'title': ['Math', 'English', 'Spanish']})

Classgrade = pd.merge(Grades, Classes, on='class_id')
alice_id = Students[Students['name'] == 'Alice'].at[0, 'student_id']
course = Classgrade[(Classgrade['student_id'] == alice_id) & (Classgrade['grade'] == 'A')]['title']
print course

# 0    Math
# Name: title, dtype: object

