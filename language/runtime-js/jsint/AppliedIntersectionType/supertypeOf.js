function supertypeOf(t){
  for (var i=0; i<this.satisfiedTypes.size; i++) {
    if (!this.satisfiedTypes.$_get(i).supertypeOf(t))return false;
  }
  return true;
}
