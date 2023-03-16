
var squirrelUtil = {};

squirrelUtil.isEmpty = function(string){
	return typeof string === 'undefined' || string == null || string === '';   
}

squirrelUtil.isNotEmpty = function(string){
	return !squirrelUtil.isEmpty(string);   
}