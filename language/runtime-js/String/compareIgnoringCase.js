function(other){
  return this.equalsIgnoringCase(other) ? 
      equal() :
      this.uppercased.compare(other.uppercased);
}
