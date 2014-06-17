function(size, character) {
  var c=(character||' ').string;
  return Array(size-this.length+1).join(c)+this;
}
