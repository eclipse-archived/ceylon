if (this.declcn$===undefined)this.constructorDeclarations();
if (this.defcn$===undefined) {
  for (var i=0;i<this.declcn$.size;i++) {
    if (this.declcn$.$_get(i).name==='') {
      this.defcn$=this.declcn$.$_get(i);
      return this.defcn$;
    }
  }
  this.defcn$=null;
}
return this.defcn$;
