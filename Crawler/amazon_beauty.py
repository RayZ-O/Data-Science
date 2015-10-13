#!/usr/bin/env python3

import urllib.request
from bs4 import BeautifulSoup
import re, io, gzip

headers={'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) \
                        Chrome/45.0.2454.101 Safari/537.36',
        'Accept':' text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
        'Accept-Encoding': 'gzip'}

base_url = r'http://www.amazon.com'
search_url = r'http://www.amazon.com/s/url=search-alias%3Dbeauty&field-keywords='

def fetch_html(url, retry):
    while attempts < retry:
        try:
            req = urllib.request.Request(url, headers)
            response = urllib.request.urlopen(req)
            bi = io.BytesIO(response.read())
            gf = gzip.GzipFile(fileobj=bi, mode='rb')
            return gf.read()
        except e:
            attempts += 1
            print("Fetch html error")
    return ""

def fetch_refine_links(soup):
    skin_types = ['Combination', 'Normal', 'Dry', 'Oily']
    refine_lst = soup.findAll('span', {'class' : 'refinementLink'})
    return [ r.parent['href'] for r in refine_lst if r.string in skin_types ]

def fetch_products(soup):
    result_lst = soup.findAll(id=re.compile('result_\d+'))
    products = []
    for result in result_lst:
        h2_tag = result.find('h2')
        products.append(h2_tag.contents[0])
    return products

keywords = ['cleanser', 'moisturizers']
for keyword in keywords:
    # fetch start html
    html = fetch_html(search_url + keyword, 3)
    soup = BeautifulSoup(html, 'lxml')

    # get links to refinement
    refine_links = fetch_refine_links(soup)

    for link in refine_links:

        url = base_url + link

        while url != base_url:
            html = fetch_html(url, 3)
            soup = BeautifulSoup(html, 'lxml')

            # fetch all products' name in this page
            products = fetch_products(soup)
            # TODO save products to file

            # get link to next page
            next_page_link = soup.find(id='pagnNextLink')['href']
            url = base_url + next_page_link





