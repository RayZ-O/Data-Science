#!/usr/bin/env python
# -*- coding: utf-8 -*-
from lxml import etree

def printnode(node):
    for i in node.findall(".//leaf"):
        print(' ' + i.attrib['value']),
    print('')

def testnode(pos):    
    def test_node(node, agent, action):
        aa = node.findall("./node[@value='NP']//node[@value='"+pos+"']//leaf[@value='"+agent+"']")
        bb = node.findall("./node[@value='VP']//leaf[@value='"+action+"']")
        if (len(aa) > 0 and len(bb) > 0):
            printnode(node)  
    return test_node

def agentact(node, agent, action, testnode):
    testnode(node, agent, action)
    snodes = node.findall(".//node[@value='S']")
    for snode in snodes:
        testnode(snode, agent, action)

def extract(agent, action, testnode, root):
	for title in agent:
		for act in action:			
			map(lambda (nn): agentact(nn[0][0][0], title, act, testnode), root)

def extract_cat(parser):
    tree = etree.parse('/home/datascience/Desktop/lab2/cat.xml',parser)
    extract(['Cat', 'cat'], ['is', 'was'], testnode("NN"), tree.getroot())
    extract(['Cats', 'cats'], ['are', 'were'], testnode("NNS"), tree.getroot())

def extract_jim(parser):
    tree = etree.parse('/home/datascience/Desktop/lab2/Jim_Parsons.xml',parser)
    extract(['Jim', 'Parsons'], ['is', 'was'], testnode("NNP"), tree.getroot())

def extract_obama(parser):
    tree = etree.parse('/home/datascience/Desktop/lab2/Barack_Obama.xml',parser)
    extract(['Obama'], ['is', 'was'], testnode("NNP"), tree.getroot())

parser = etree.XMLParser(recover=True)
extract_jim(parser)




