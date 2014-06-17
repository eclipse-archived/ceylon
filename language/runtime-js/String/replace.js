function(sub, repl) {
  var result = this;
  var index = -1;
  while ((index = this.indexOf(sub)) >= 0) {
    result = result.substring(0, index) + repl + 
             result.substring(index + sub.length);
  }
  return result;
}
