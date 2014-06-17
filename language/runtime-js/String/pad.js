function(size, character) {
  var total = size - this.length;
  var right = Math.ceil(total / 2);
  var left = total - right;
  return Array(left+1).join(character||" ") + this + 
         Array(right+1).join(character||" ");
}
