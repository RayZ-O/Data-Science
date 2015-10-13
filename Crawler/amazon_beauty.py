#!/usr/bin/env python3

import urllib.request
from bs4 import BeautifulSoup
import re

url = r'http://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=cleanser'

response = urllib.request.urlopen(url)
html = response.read()
soup = BeautifulSoup(html, 'lxml')

result_lst = soup.findAll(id=re.compile('result_\d+'))
refine_lst = soup.findAll('span', {'class' : 'refinementLink'})

products = []
refine_links = []
for result in result_lst:
    h2_tag = result.find('h2')
    products.append(h2_tag.contents[0])






