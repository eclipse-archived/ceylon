function(o) {
  if (is$(o,{t:Byte})) {
    return o.val$===this.val$;
  }
  return false;
}
