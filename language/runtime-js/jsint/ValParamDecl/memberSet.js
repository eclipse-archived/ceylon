function(a,b){
  if (this.shared && this.variable && this.at$) {
    return this.at$.memberSet(a,b);
  }
  throw StorageException$meta$model("Attribute " + this.name + " is neither captured nor shared so it has no physical storage allocated and cannot be read by the metamodel");
}
