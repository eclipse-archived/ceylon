value good = "hello";
@error String bad;
function goodFunction() { return good; }
@error String badFunction();

@error shared value sharedBad = "hello";
@error shared function sharedBadFunction() { return sharedBad; }