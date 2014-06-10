function(name) {
  if (this.namedArgs===undefined)this.arguments;
  return (name in this.namedArgs);
}
