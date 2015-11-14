function(sub, repl) {
  var index = this.indexOf(sub);
  if (index >= 0) {
    return this.substring(0, index) + repl + 
           this.substring(index + sub.length);
  }
  return this;
}
