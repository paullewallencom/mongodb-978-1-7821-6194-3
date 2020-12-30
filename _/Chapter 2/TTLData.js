function addTTLTestData() {
	print("Create three records in database each with a create time that is 1 minute apart")
	current = new Date()
	//Drop the collection if it exists
	db.ttlTest.drop()
	//Document that was created 4 mins back
	doc = {}
	doc._id = 1
	doc.createDate = new Date(current - 4 * 60 * 1000)
	db.ttlTest.insert(doc)
	//Document that was created 3 mins back
	doc = {}
	doc._id = 2
	doc.createDate = new Date(current - 3 * 60 * 1000)
	db.ttlTest.insert(doc)
	//Document that was created 2 mins back
	doc = {}
	doc._id = 3
	doc.createDate = new Date(current - 2 * 60 * 1000)
	db.ttlTest.insert(doc)
	print("Created three test documents, oldest being 4 mins old")
	print("Now create a TTL index with expiry of 5 mins on the createDate field as follows")
	print("db.ttlTest.ensureIndex({createDate:1}, {expireAfterSeconds:300})")
}


function addTTLTestData2() {
	print("Create three records in database each with a different expiry")
	current = new Date()
	//Drop the collection if it exists
	db.ttlTest2.drop()
	//Document with expiry date, 5 mins after current time
	doc = {}
	doc._id = 1
	doc.expiryDate = new Date(
			year = 1900 + current.getYear(),
			month = current.getMonth(),
			date = current.getDate(),
			hours = current.getHours(),
			minutes = current.getMinutes() + 5,
			seconds = current.getSeconds()
		)
	db.ttlTest2.insert(doc)
	//Document with expiry date, 4 mins after current time
	doc = {}
	doc._id = 2
	doc.expiryDate = new Date(
			year = 1900 + current.getYear(),
			month = current.getMonth(),
			date = current.getDate(),
			hours = current.getHours(),
			minutes = current.getMinutes() + 4,
			seconds = current.getSeconds()
		)
	db.ttlTest2.insert(doc)
	//Document with expiry date, 7 mins after current time
	doc = {}
	doc._id = 3
	doc.expiryDate = new Date(
			year = 1900 + current.getYear(),
			month = current.getMonth(),
			date = current.getDate(),
			hours = current.getHours(),
			minutes = current.getMinutes() + 7,
			seconds = current.getSeconds()
		)
	db.ttlTest2.insert(doc)
	print("Created three test documents")
	print("Now create a TTL index with expiry of 5 mins on the createDate field as follows")
	print("db.ttlTest2.ensureIndex({expiryDate:1}, {expireAfterSeconds:0})")
}
