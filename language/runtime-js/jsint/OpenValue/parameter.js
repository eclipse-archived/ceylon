if (this.param$$!==undefined)return this.param$$!==null;
if (is$(this.container,{t:'u',l:[{t:FunctionalDeclaration$meta$declaration},{t:ClassWithInitializerDeclaration$meta$declaration}]})) {
  var param;
  for (var iter=this.container.parameterDeclarations.iterator();(param=iter.next())!==finished();) {
    if (param.name.equals(this.name)) {
      this.param$$=param;
      return true;
    }
  }
}
this.param$$=null;
return false;
