function(other) {
    return this==other ? 
        equal() : 
        (this<other ? smaller():larger());   
         
  /*var len1 = this.length;
    var len2 = other.length;
    var lim = Math.min(len1, len2);

    var k = 0;
    while (k < lim) {
        var c1 = this.charCodeAt(k);
        var c2 = other.charCodeAt(k);
        if (c1 != c2) {
            return c1 - c2;
        }
        k++;
    }
    return len1 - len2;*/
}
