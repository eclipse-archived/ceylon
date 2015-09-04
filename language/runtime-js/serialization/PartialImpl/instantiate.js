function instantiate(){
  var classModel=this.clazz;
  if (!classModel) {
    throw DeserializationException("no class specified for instance with id " + this.id);
  }
  var outer,outerClass;
  if (is$(classModel,{t:AppliedClass$jsint})) {
    outer=null;
    outerClass=null;
  } else if (is$(classModel,{t:AppliedMemberClass$jsint})) {
    outer=this.container;
    if (is$(outer,{t:Partial$serialization}))outer=outer.instance_;
    outerClass=type$meta(outer,{Type$type:classModel.$$targs$$.Container$AppliedMemberClass});
  } else {
    throw AssertionError("unexpected class model " + (classModel&&classModel.string||"NULL"));
  }
  this.instance_=classModel.tipo.inst$$(classModel);
  if (outer) {
    this.instance_.outer$=outer;
  }
}
