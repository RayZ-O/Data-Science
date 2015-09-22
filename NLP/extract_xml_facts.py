#!/usr/bin/env python
# -*- coding: utf-8 -*-
from lxml import etree

def printnode(node):
    for i in node.findall(".//leaf"):
        print(' ' + i.attrib['value']),
    print('')

def testnode(node, agent, action, pos):
    aa = node.findall("./node[@value='NP']//node[@value='"+pos+"']//leaf[@value='"+agent+"']")
    bb = node.findall("./node[@value='VP']//leaf[@value='"+action+"']")
    if (len(aa) > 0 and len(bb) > 0):
        printnode(node)  

def testnode_nn(node, agent, action):
    testnode(node, agent, action, 'NN')

def testnode_nns(node, agent, action):
    testnode(node, agent, action, 'NNS')

def agentact(node, agent, action, testnode):
    testnode(node, agent, action)
    snodes = node.findall(".//node[@value='S']")
    for snode in snodes:
        testnode(snode, agent, action)

def extract(agent, action, testnode, root):
	for title in agent:
		for act in action:			
			map(lambda (nn): agentact(nn[0][0][0], title, act, testnode), root)

parser = etree.XMLParser(recover=True)
tree = etree.parse('/home/datascience/Desktop/lab2/cat.xml',parser)
root=tree.getroot()

extract(['Cat', 'cat'], ['is', 'was'], testnode_nn, root)
extract(['Cats', 'cats'], ['are', 'were'], testnode_nns, root)

