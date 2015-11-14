function(a){
  a=convert$params(getrtmm$$(this.tipo),a,this.$targs);
  if (this.$targs) {
    a.push(this.$targs);
  }
  var thefun=this.tipo;
  if (thefun.$fml===1) {
    //formal member, get the concrete impl from the bound object
    var mm=getrtmm$$(thefun);
    thefun=this.$bound[mm.d[mm.d.length-1]];
  }
  return thefun.apply(this.$bound,a);
}
