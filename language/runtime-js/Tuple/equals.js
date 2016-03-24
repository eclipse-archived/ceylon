function(o) {
  if (is$(o,{t:Tuple})) {
    if (this.size===o.size) {
      for (var i=0;i<this.size;i++) {
        if (!this.$_get(i).equals(o.$_get(i)))return false;
      }
      return true;
    }
  } else if (is$(o,{t:Sequence})) {
    return o.equals(this);
  }
  return false;
}
