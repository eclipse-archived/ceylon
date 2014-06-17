function(sub, repl) {
  var result = this;
  var index = -1;
  var from = 0;
  while ((index = result.indexOf(sub,from)) >= 0) {
    result = result.substring(0, index) + repl + 
             result.substring(index + sub.length);
    from = index + repl.length;
  }
  return result;
}
