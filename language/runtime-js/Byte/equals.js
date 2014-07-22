function(o) {
  if (is$(o,{t:Byte})) {
    return o.integer===this.integer;
  }
  return false;
}
