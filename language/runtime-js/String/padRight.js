function(size, character) {
  var c=(character||' ').string;
  return this+Array(size-this.length+1).join(c);
}
