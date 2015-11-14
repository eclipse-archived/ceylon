function(d) {
  this.decons_=d;
  if (this.state_!==undefined) {
    throw AssertionError("reference has already been deserialized: " + this.string);
  }
  this.state_=1; //Uninitialized
  return this;
}
