function createSparseIndexData() {	
	db.sparseTest.drop()
	for(x = 1, y = 1 ; x < 101; x++) {		
		doc = {'x': x}
		if(x % 3 == 0) {
			doc.y = y++	
		}
		db.sparseTest.insert(doc)
	}
}