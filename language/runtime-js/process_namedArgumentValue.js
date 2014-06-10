function(name) {
  if (this.namedArgs===undefined)this.arguments;
  return this.namedArgs[name]||null;
}
