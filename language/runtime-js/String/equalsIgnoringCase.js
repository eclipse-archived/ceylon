function(other){
  return is$(other,{t:$_String}) 
      && other.uppercased.equals(this.uppercased)
      && other.lowercased.equals(this.lowercased);
}
