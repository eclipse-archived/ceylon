function findServiceProviders(serv,$targs,buscados) {
  var r=[];
  var key=serv.string+"/"+qname$($targs.Service$findServiceProviders);
  if (this.$servimpl$ && this.$servimpl$[key]) {
    //Found impls in module-level service cache
    r.push.apply(r,this.$servimpl$[key]);
  } else {
    //Get the declaration for the service provider
    var typelit=typeLiteral$meta({Type$typeLiteral:$targs.Service$findServiceProviders}).declaration;
    //Iterate over this module's packages
    var pkg;for (var pkgIter=this.members.iterator();(pkg=pkgIter.next())!==finished();) {
      //Iterate over each package's classes
      var tipo;for (var titer=pkg.members({Kind$members:{t:ClassDeclaration$meta$declaration}}).iterator();(tipo=titer.next())!==finished();) {
        //Check each class' satisfied types, looking for the service provider
        var sat;for (var siter=tipo.satisfiedTypes.iterator();(sat=siter.next())!==finished();) {
          //If declarations match, compare OpenType vs Type (to easily match type arguments)
          if (sat.declaration.equals(typelit) && sat.string.equals(serv.string)) {
            //Service provider satisfied, check its service annotation
            var ann=tipo.annotations({Annotation$annotations:{t:ServiceAnnotation}});
            if (ann.size==1 && ann.first.contract.equals(typelit)) {
              //Instantiate the service and store it in module cache
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
  //Search dependencies
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
