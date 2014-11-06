function(dc) {
  if (typeof(this.instance().ser$$) === 'function') {
    this.instance().ser$$(dc);
  } else {
    throw AssertionError("object is not an instance of a serializable class");
  }
}
