#!/usr/bin/env python
# -*- coding: utf-8 -*-
import requests
import json
import mwparserfromhell as mwph

def pretty(jdata):
    str = json.dumps(jdata, sort_keys=True, indent=4).decode('string_escape');
    return str

def saveas(sdata, fname):
    f = open(fname,'w');
    f.write(sdata);
    f.close();

title='parsing'
response = requests.get("http://en.wikipedia.org/w/api.php?format=json&action=query&titles="+str(title)+"&prop=revisions&rvprop=content")
jsondata = response.json()
content = jsondata['query']['pages'].values()[0]['revisions'][0]['*']

wikicode = mwph.parse(content)
text = wikicode.strip_code()
saveas(pretty(text), '/home/datascience/Desktop/lab2/'+title+'.txt')