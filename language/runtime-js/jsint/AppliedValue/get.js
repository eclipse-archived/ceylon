function(){
  if (this.obj) {
    var mm=this.tipo.$crtmm$;
    return (mm&&mm.d&&this.obj[mm.d[mm.d.length-1]])||this.tipo.get.call(this.obj);
  }
  //this.tipo() is for anonymous classes that are treated as values
  return this.tipo.get?this.tipo.get():this.tipo();
}
