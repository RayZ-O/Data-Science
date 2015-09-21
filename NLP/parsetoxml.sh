#!/usr/bin/env bash
#
# Runs the English PCFG parser on one or more files, printing xml tree

if [ ! $# -ge 1 ]; then
  echo Usage: `basename $0` 'file(s)'
  echo
  exit
fi

scriptdir="${BASH_SOURCE[0]}"
scriptdir=`readlink "${scriptdir}"`
scriptdir=`dirname "${scriptdir}"`

java -mx750m -cp "$scriptdir/*:" edu.stanford.nlp.parser.lexparser.LexicalizedParser \
 -outputFormat "xmlTree" -outputFormatOptions "xml" edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz $*
