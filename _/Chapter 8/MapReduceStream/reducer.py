#!/usr/bin/env python

import sys
from pymongo_hadoop import BSONReducer
def reducer(key, documents):
	print >> sys.stderr, 'Invoked reducer for key "', key, '"'
	count = 0
	for doc in documents:
		count += 1
	return {'_id' : key, 'count' : count}


BSONReducer(reducer)

