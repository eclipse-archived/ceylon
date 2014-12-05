function(a){
  a=convert$params(getrtmm$$(this.tipo),a,this.$targs);
  if (this.$targs) {
    a.push(this.$targs);
  }
  return this.tipo.apply(this.$bound,a);
}
