function(f){
  for (var i=this.length-1;i>=0;i--){
    if (f(this[i]))return this[i];
  }
  return null;
}
