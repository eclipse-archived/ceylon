function(other) {
    return this==other ? 
        equal() : 
        (this<other ? smaller():larger());   
}
