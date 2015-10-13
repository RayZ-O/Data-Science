#!/usr/bin/env python3

import urllib.request
from bs4 import BeautifulSoup
import re

def is_result(tag):
    if tag.name != 'li':
        return False
    if tag.has_attr('id'):
        matcher = re.match(r'result_[0-9]*', tag['id'])
        return matcher is not None

def is_refine(tag):
    if tag.name != 'span':
        return False
    if tag.has_attr('class'):
        return tag['class'] == 'refinementLink'

url = r'http://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=cleanser'
req = urllib.request.Request(
    url,
    data=None,
    headers={
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
        'Accept-Encoding': 'gzip, deflate, sdch',
        'Accept-Language': 'en-US,en;q=0.8',
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36'
    }
'http://www.amazon.com/s/ref=sr_nr_p_n_feature_browse-b_1?fst=as%3Aoff&rh=n%3A3760911%2Ck%3Acleanser%2Cp_n_feature_browse-bin%3A370239011&keywords=cleanser'
'http://www.amazon.com/s/ref=sr_nr_p_n_feature_browse-b_3?fst=as%3Aoff&rh=n%3A3760911%2Ck%3Asun+cream%2Cp_n_feature_browse-bin%3A370239011&keywords=sun+cream'
response = urllib.request.urlopen(req)
html = response.read()
soup = BeautifulSoup(html, 'lxml')


result_lst = soup.find_all(is_result)
refine_lst = soup.find_all(is_refine)

products = []
refine_links = []
for result in result_lst:
    h2_tag = result.find('h2')
    products.append(h2_tag.contents[0])





