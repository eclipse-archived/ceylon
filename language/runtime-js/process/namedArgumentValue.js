function(name) {
  if (this.namedArgs===undefined)this.$_arguments;
  return this.namedArgs[name]||null;
}
