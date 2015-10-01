function AppliedValueConstructor$jsint(tipo,$$targs$$,that) {
  if (!$$targs$$.Type$AppliedValueConstructor)$$targs$$.Type$AppliedValueConstructor=$$targs$$.Type$ValueConstructor;
  $init$AppliedValueConstructor$jsint();
  if (that===undefined)that=new AppliedValueConstructor$jsint.$$;
  set_type_args(that,$$targs$$);
  ValueConstructor$meta$model({Type$ValueConstructor:$$targs$$.Type$AppliedValueConstructor},that);
  that.tipo=tipo;
  return that;
}
