#!/usr/bin/env python
# -*- coding: utf-8 -*-
import re
import requests
import json
import mwparserfromhell as mwph
import sys

title='Barack Obama'
# title='Jim Parsons'
# title='Xi Jinping'
response = requests.get('http://en.wikipedia.org/w/api.php?format=json&action=query&titles='+str(title)+'&prop=revisions&rvprop=content')
jsondata = response.json()
content = jsondata['query']['pages'].values()[0]['revisions'][0]['*']

wikicode = mwph.parse(content)
infobox = ''
for t in wikicode.filter_templates():
	if re.search(re.compile('infobox', re.IGNORECASE), t.encode('utf-8')):
	 	infobox = t.encode('utf-8')
print infobox
if not infobox:
	print "Can't find infobox."
	sys.exit(1)

print title + ':\n'
spouse = re.search(r'spouse.*\[\[([^\]]*)\]\]', infobox)
if spouse:
	print 'Spouse:', spouse.group(1), '\n' 
else:
	print 'Spouse not found\n'

raw_birth_place = re.search(r'birth_place[ ]*=[ ]*(.*)', infobox)
if raw_birth_place:
	print 'Place of birth:', re.sub('\[|\]', '', raw_birth_place.group(1)), '\n'
else:
	print 'Place of birth not found\n'

education = re.search(r'education.*\[\[([^\]]*)\]\]', infobox)
if education:
	print 'Education:', education.group(1), '\n'

# schools maybe {{plain list|\n*[[school A]]\n*[[school B]]\n*[[school B]]\n}} or single element [[school A]]
raw_alma_mater = re.search(re.compile(r'alma_mater[ ]*=[ ]*({{plain list([^}]*)|\[\[([^\]]*\]\]))', re.DOTALL), infobox)
if raw_alma_mater:
	print 'Schools:'
	for sram in raw_alma_mater.group(1).split('\n'):
		school = re.search(r'\[\[([^\]]*)\]\]', sram)
		if school:
			print school.group(1)

if not education and not raw_alma_mater:
	print 'Schools not found'