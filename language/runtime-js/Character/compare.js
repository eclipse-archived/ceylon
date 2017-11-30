function(other) {
    return this.value===other.value ? 
        equal() : 
        (this.value<other.value ? smaller():larger());
}
