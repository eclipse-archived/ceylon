function AppliedValueConstructor$jsint(tipo,$$targs$$,that) {
  if (!$$targs$$.Set$AppliedValueConstructor)$$targs$$.Set$AppliedValueConstructor=$$targs$$.Set$ValueConstructor;
  if (!$$targs$$.Type$AppliedValueConstructor)$$targs$$.Type$AppliedValueConstructor=$$targs$$.Type$ValueConstructor;
  $init$AppliedValueConstructor$jsint();
  if (that===undefined)that=new AppliedValueConstructor$jsint.$$;
  set_type_args(that,$$targs$$);
  ValueConstructor$meta$model({Set$ValueConstructor:$$targs$$.Set$AppliedValueConstructor,
    Type$ValueConstructor:$$targs$$.Type$AppliedValueConstructor},that);
  that.tipo=tipo;
  return that;
}
