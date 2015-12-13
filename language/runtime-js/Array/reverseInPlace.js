function() {
  var len = this.arr$.length;
  for (i=0; i<Math.floor(len/2); i++) {
      var swap = this.arr$[i];
      this.arr$[i]=this.arr$[len-i-1];
      this.arr$[len-i-1]=swap;
  }
}
