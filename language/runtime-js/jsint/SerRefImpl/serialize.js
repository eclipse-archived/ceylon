function(dc) {
  if (typeof(this.instance().ser$$) === 'function') {
    this.instance().ser$$(dc);
  } else {
    throw AssertionError("not an instance of a serializable class: " + this.instance().string);
  }
}
