String good = "hello";
@error String bad;
String goodFunction() { return good; }
@error String badFunction();

@error value sharedBad = "hello";
@error function sharedBadFunction() { return sharedBad; }