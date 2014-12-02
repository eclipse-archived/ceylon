function AppliedConstructor$jsint(tipo,$$targs$$,that,myTargs) {
  if ($$targs$$.Type$AppliedConstructor===undefined)$$targs$$.Type$AppliedConstructor=$$targs$$.Type$Constructor;
  if ($$targs$$.Arguments$AppliedConstructor===undefined)$$targs$$.Arguments$AppliedConstructor=$$targs$$.Type$Constructor;
  $init$AppliedConstructor$jsint();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    that=new AppliedConstructor$jsint.$$;
  }
  Constructor$meta$model({Type$Constructor:$$targs$$.Type$AppliedConstructor,Arguments$Constructor:$$targs$$.Arguments$AppliedConstructor},that);
  set_type_args(that,$$targs$$,AppliedConstructor$jsint);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
