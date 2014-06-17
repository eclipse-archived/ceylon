function(size, character) {
  return Array(size-this.length+1).join(character||" ")+this;
}
