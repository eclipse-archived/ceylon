function $_bind(cont){
  if (this.declaration.$_static) {
    return this(null);
  }
  var ot=cont.getT$name ? cont.getT$all()[cont.getT$name()]:err$(IncompatibleTypeException$meta$model("Container does not appear to be a Ceylon object"));
  if (!ot)throw IncompatibleTypeException$meta$model("Incompatible Container (has no metamodel information)");
  var omm=getrtmm$$(ot);
  var mm=getrtmm$$(this.tipo);
  if (!extendsType({t:ot},{t:mm.$cont}))throw IncompatibleTypeException$meta$model("Incompatible container type");
  return this(cont);
}
