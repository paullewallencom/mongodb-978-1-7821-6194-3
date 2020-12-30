function prepareTestData() {
	db.updAndDelTest.drop()
	for(x = 1; x <= 2 ; x++) {
		for(y = 1 ; y <= 10 ; y++) {
			doc = {'x':x, 'y':y}
			db.updAndDelTest.insert(doc)	
		}
	}
	print('Inserted ' + db.updAndDelTest.count() + ' documents in updAndDelTest')
}