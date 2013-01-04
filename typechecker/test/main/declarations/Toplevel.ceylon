String good = "hello";
@error String bad;
String goodFunction() { return good; }
@error String badFunction();

@error value sharedBad = "hello";
@error function sharedBadFunction() { return sharedBad; }

variable Integer count=0;
Float amount = 0.0;