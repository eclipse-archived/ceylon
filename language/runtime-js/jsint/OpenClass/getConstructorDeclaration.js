function(nom) {
  if (this.declcn$===undefined)this.constructorDeclarations();
  for (var i=0; i < this.declcn$.size; i++) {
    if (this.declcn$.$_get(i).name===nom)return this.declcn$.$_get(i);
  }
  return null;
}
