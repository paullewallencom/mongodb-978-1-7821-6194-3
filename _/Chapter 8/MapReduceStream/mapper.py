#!/usr/bin/env python

import sys
from pymongo_hadoop import BSONMapper
def mapper(documents):
	print >> sys.stderr, 'Starting mapper'
	for doc in documents:
		yield {'_id' : doc['state'], 'count' : 1}
	print >> sys.stderr, 'Mapper completed'

BSONMapper(mapper)

