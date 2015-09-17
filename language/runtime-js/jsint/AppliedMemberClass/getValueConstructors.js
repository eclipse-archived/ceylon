function(anntypes){
  return this.getDeclaredValueConstructors(anntypes)
    .select(function(e){return (getrtmm$$(e.tipo).pa&1)>0;});
}
