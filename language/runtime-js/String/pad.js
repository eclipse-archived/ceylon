function(size, character) {
  var total = size - this.size;
  if (total<=0) return this;
  var right = Math.ceil(total / 2);
  var left = total - right;
  var c = (character || ' ').string;
  return Array(left+1).join(c) + this + 
         Array(right+1).join(c);
}
