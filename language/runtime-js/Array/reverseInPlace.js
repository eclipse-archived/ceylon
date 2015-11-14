function() {
  var len = this.length;
  for (i=0; i<Math.floor(len/2); i++) {
      var swap = this[i];
      this[i]=this[len-i-1];
      this[len-i-1]=swap;
  }
}
