function(a){
  if (this.shared && this.at$) {
    return this.at$.memberGet(a);
  }
  throw StorageException$meta$model("Attribute " + this.name + " is neither captured nor shared so it has no physical storage allocated and cannot be read by the metamodel");
}
