function(t){
  for (var i=0; i<this.caseTypes.size; i++) {
    if (this.caseTypes.$_get(i).supertypeOf(t))return true;
  }
  return false;
}
