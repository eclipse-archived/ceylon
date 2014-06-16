function(size, character) {
  return this+Array(size-this.length+1).join(character||" ");
}
