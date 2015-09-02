function AppliedValueConstructor$jsint(tipo,$$targs$$,that) {
  $init$AppliedValueConstructor$jsint();
  if (that===undefined)that=new AppliedValueConstructor$jsint.$$;
  set_type_args(that,$$targs$$);
  ValueConstructor$meta$model({Set$ValueConstructor:$$targs$$.Set$AppliedValueConstructor,
    Type$ValueConstructor:$$targs$$.Type$AppliedValueConstructor},that);
  that.tipo=tipo;
  return that;
}
