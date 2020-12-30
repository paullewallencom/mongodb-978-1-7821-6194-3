names = [
	{name:'James Smith', login:'jsmith', age:30},
	{name:'Robert Johnson', login:'rjohns', age:22},
	{name:'Michael Williams', login:'mwilli', age:32},
	{name:'David Jones', login:'djones', age:24},
	{name:'Richard Brown', login:'rbrown', age:30},
	{name:'Paul Davis', login:'pdavis', age:38},	
	{name:'Charles Miller', login:'cmille',age:29},
	{name:'Paul Wilson', login:'pwilso', age:25},
	{name:'George Moore', login:'gmoore', age:21},
	{name:'Mark Taylor', login:'mtaylo', age:25},
	{name:'Brian Anderson', login:'bander', age:31},
	{name:'Timothy Thomas', login:'tthoma', age:27},
	{name:'Kevin Jackson', login:'kjacks', age:32},
	{name:'Frank White', login:'fwhite', age:23},
	{name:'Joshua Harris', login:'jharri', age:29},
	{name:'Dennis Martin', login:'dmarti', age:33},	
	{name:'Carl Thompson', login:'cthomp', age:25},
	{name:'Jack Garcia', login:'jgarci', age:24},
	{name:'Roy Martinez', login:'rmarti', age:22},
	{name:'Jeremy Robinson', login:'jrobin', age:30}
]
function loadUserDetailsData() {
	db.userDetails.drop()
	numOfRecords = names.length
	for(i = 0 ; i < 100 ; i++) {
		db.userDetails.insert(names[i % numOfRecords])
	}
}