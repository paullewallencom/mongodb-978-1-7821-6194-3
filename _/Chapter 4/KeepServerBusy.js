db.monitoringTest.drop()
while(true) {
	for(i = 0; i < 1000 ; i++) {
		db.monitoringTest.insert({'i':i})
	}
	for(i = 0; i < 1000 ; i++) {
		db.monitoringTest.update({'i':i}, {$set:{'text':'text'}})
	}	
	cursor = db.monitoringTest.find()
	while(cursor.hasNext()) {
		c = cursor.next()
	}
	for(i = 0; i < 1000 ; i++) {
		db.monitoringTest.remove({'i':i})	
	}	
}