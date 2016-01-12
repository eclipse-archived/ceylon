if (this.$defcons$===undefined) {
  var mm=getrtmm$$(this.tipo);
  var fn=mm.d[mm.d.length-1]+'_$c$';
  var cn=this.tipo[fn];
  var fc=false;
  if (!cn) {
    var m2=get_model(mm);
    if (m2 && m2.$cn===undefined) {
      cn=this.tipo;
      fc=true;
    } else if (m2 && m2.$cn) {
      //The class has constructors, but it's a local class or something
      for (var mem in this.tipo) {
        if (mem.endsWith("_$c$")) {
          cn=this.tipo[mem];
          break;
        }
      }
    }
  }
  if (cn) {
    mm=getrtmm$$(cn).ps;
    var args=tupleize$params(getrtmm$$(cn).ps,this.$$targs$$.Target$Type.a);
    var r=AppliedCallableConstructor$jsint(cn,{Type$AppliedCallableConstructor:this.$$targs$$.Type$AppliedClass,
          Arguments$AppliedCallableConstructor:args},undefined,this.$targs);
    r.cont$=this;
    this.$defcons$=r;
    if(fc)r.fakeConstr$=true;
    return r;
  }
  this.$defcons$=null;
}
return this.$defcons$;
