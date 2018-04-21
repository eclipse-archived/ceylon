function AppliedValueConstructor$jsint(tipo,$a$,that) {
  if (!$a$.Type$AppliedValueConstructor)$a$.Type$AppliedValueConstructor=$a$.Type$ValueConstructor;
  $i$AppliedValueConstructor$jsint();
  if (that===undefined)that=new AppliedValueConstructor$jsint.$$;
  set_type_args(that,$a$);
  ValueConstructor$meta$model({Type$ValueConstructor:$a$.Type$AppliedValueConstructor},that);
  that.tipo=tipo;
  return that;
}
