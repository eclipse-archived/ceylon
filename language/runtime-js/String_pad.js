function(size, character) {
  var padlen = len - str.length;
  var right = Math.ceil(padlen / 2);
  var left = padlen - right;
  return Array(left+1).join(character||" ") + this + 
         Array(right+1).join(character||" ");
}
