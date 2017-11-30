
String bug257Name = "Gavin";

@test
shared void bug257(){
    value na = `bug257Name`;
    value temp = na.get();
}