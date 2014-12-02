function(a){
  var mm=getrtmm$$(this.tipo);
  var mdl=get_model(mm);
  a=convert$params(mm,a);
  if (this.$targs)a.push(this.$targs);
  return this.tipo.apply(undefined,a);
}
