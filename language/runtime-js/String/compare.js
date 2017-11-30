function(other) {
    if (!is$(other,{t:$_String}))throw new TypeError("String expected");
    return this==other ? 
        equal() : 
        (this<other ? smaller():larger());   
}
