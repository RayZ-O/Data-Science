#!/usr/bin/env python3

import urllib.request
from bs4 import BeautifulSoup
import re, io, gzip, os, time

result_path = '/home/rui/Desktop/Skin/'
headers={'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36',
         'Accept':' text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
         'Accept-Encoding': 'gzip'}

base_url = r'http://www.amazon.com'
search_url = r'http://www.amazon.com/s/url=search-alias%3Dbeauty&field-keywords='

def fetch_html(url, retry):
    attempts = 0
    while attempts < retry:
        try:
            req = urllib.request.Request(url, headers=headers)
            response = urllib.request.urlopen(req)
            # accept encoding is gzip, decode to get the actual html
            bi = io.BytesIO(response.read())
            gf = gzip.GzipFile(fileobj=bi, mode='rb')
            return gf.read()

        except Exception as e:
            print(str(e))
            attempts += 1
            time.sleep(1)
    return ""

def fetch_refine_links(soup):
    skin_types = ['Combination', 'Normal', 'Dry', 'Oily']
    refine_lst = soup.findAll('span', {'class' : 'refinementLink'})
    refine_links = {}
    for r in refine_lst:
        if r.string in skin_types:
            # link to the refinement page is in the parent tag
            refine_links[r.string] = r.parent['href']
    return refine_links

def fetch_products(soup):
    # get ancestor tag of each product
    result_lst = soup.findAll(id=re.compile('result_\d+'))
    products = []
    for result in result_lst:
        h2_tag = result.find('h2')
        products.append(h2_tag.contents[0])
    return products

def save_as(filename, products):
    text_file = open(filename , "w")
    for p in products:
        text_file.write(p + '\n')
    text_file.close()

keywords = ['cleanser', 'moisturizers']
for keyword in keywords:
    prod_dir = result_path + keyword + '/'
    if not os.path.exists(prod_dir):
        os.makedirs(prod_dir)
    # fetch start html
    start_page_html = fetch_html(search_url + keyword, 3)
    soup = BeautifulSoup(start_page_html, 'lxml')

    # get links to refinement
    refine_links = fetch_refine_links(soup)

    for skin_type, link in refine_links.items():
        # create a directory for corresponding type
        type_dir = prod_dir + skin_type + '/'
        if not os.path.exists(type_dir):
            os.makedirs(type_dir)

        url = base_url + link
        page_num = 1
        while url != base_url:
            product_page_html = fetch_html(url, 3)
            soup = BeautifulSoup(product_page_html, 'lxml')
            # fetch all products' name in this page and save
            products = fetch_products(soup)
            save_as(type_dir + 'page' + str(page_num), products)
            page_num += 1
            # get link to next page
            next_page = soup.find(id='pagnNextLink') # last page id is pagnNextString
            next_page_link = next_page['href'] if next_page is not None else ''
            url = base_url + next_page_link
