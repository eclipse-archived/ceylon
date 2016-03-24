function(name) {
  if (this.namedArgs===undefined)this.$_arguments;
  return (name in this.namedArgs);
}
