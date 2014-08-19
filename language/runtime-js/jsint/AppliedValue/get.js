function(){
  if (this.obj) {
    var mm=this.tipo.$crtmm$;
    return (mm&&mm.d&&this.obj[mm.d[mm.d.length-1]])||this.tipo.get.call(this.obj);
  }
  return this.tipo.get();
}
