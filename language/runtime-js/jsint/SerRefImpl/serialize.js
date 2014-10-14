function(dc) {
  if (typeof(this.instance().ser$$) === 'function') {
    this.instance().ser$$(dc);
  }
  throw AssertionError("object is not an instance of a serializable class");
}
