function triggerOperations() {
	db = db.getSisterDB('test')
	var collection = db.oplogTriggerTest
	collection.drop()
	//Insert documents into collection
	for(i = 0 ; i < 6 ; i++) {
		collection.insert({'i' : i})
	}
	//Update One
	collection.update({'i' : 1}, {$set: {'value' : 'One'}})
	
	//Delete One
	collection.remove({i:2}, true)
	//Add more
	for(i = 6 ; i < 10 ; i++) {
		collection.insert({'i' : i})
	}
	//delete all
	collection.remove()
}
