function findServiceProviders(serv,$targs,buscados) {
  var r=[];
  var key=serv.string+"/"+qname$($targs.Service$findServiceProviders);
  if (this.$servimpl$ && this.$servimpl$[key]) {
    r.push.apply(r,this.$servimpl$[key]);
  } else {
    var typelit=typeLiteral$meta({Type$typeLiteral:$targs.Service$findServiceProviders});
    var pkg;for (var pkgIter=this.members.iterator();(pkg=pkgIter.next())!==finished();) {
      var tipo;for (var titer=pkg.members({Kind$members:{t:ClassDeclaration$meta$declaration}}).iterator();(tipo=titer.next())!==finished();) {
        var sat;for (var siter=tipo.satisfiedTypes.iterator();(sat=siter.next())!==finished();) {
          if (sat.declaration.string.equals(typelit.declaration.string)) {
            var ann=tipo.annotations({Annotation$annotations:{t:ServiceAnnotation}});
            if (ann.size==1 && ann.first.contract.equals(typelit.declaration)) {
              var impl=tipo.instantiate()
              if (!this.$servimpl$)this.$servimpl$={};
              if (this.$servimpl$[key]) {
                this.$servimpl$[key].push(impl);
              } else {
                this.$servimpl$[key]=[impl];
              }
              r.push(impl);
            }
          }
        }
      }
    }
  }
  //Deps lookup
  if (buscados)buscados.push(this.string);else buscados=[this.string];
  var dep;for (var depsIter=this.dependencies.iterator();(dep=depsIter.next())!==finished();) {
    var mod=modules$meta().find(dep.name,dep.version);
    if (mod && buscados.indexOf(mod.string)<0) {
      var mr=mod.findServiceProviders(serv,$targs,buscados);
      if (mr.size) {
        for (var i=0;i<mr.size;i++)r.push(mr.$_get(i));
      }
    }
  }
  return r.length?$arr$sa$(r,$targs.Service$findServiceProviders,1):empty();
}
