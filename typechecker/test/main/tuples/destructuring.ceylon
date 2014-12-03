void destructuring([Integer,Integer, String] tuple) {
    value [i,j,str] = tuple;
    Integer ii = i;
    Integer jj = j;
    String s = str;
    value [Integer i_,value j_,@error Float str_] = tuple;
    @error value [x,y] = tuple;
    value [a,b,c,@error d,@error e] = tuple;
    @error value [z] = "";
}