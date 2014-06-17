function(size, character) {
  var total = size - this.length;
  if (total<=0) return this;
  var c = (character || ' ').string;
  return Array(total+1).join(c) + this;
}
