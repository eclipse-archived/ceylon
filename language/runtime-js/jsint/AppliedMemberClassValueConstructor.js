function AppliedMemberClassValueConstructor$jsint(tipo,$a$,that) {
  if (!$a$.Type$AppliedMemberClassValueConstructor)$a$.Type$AppliedValueConstructor=$a$.Type$MemberClassValueConstructor;
  if (!$a$.Container$AppliedMemberClassValueConstructor)$a$.Container$AppliedValueConstructor=$a$.Container$MemberClassValueConstructor;
  $i$AppliedMemberClassValueConstructor$jsint();
  if (that===undefined)that=new AppliedMemberClassValueConstructor$jsint.$$;
  set_type_args(that,$a$);
  MemberClassValueConstructor$meta$model(
    {Type$MemberClassValueConstructor:$a$.Type$AppliedMemberClassValueConstructor,
    Container$MemberClassValueConstructor:$a$.Container$AppliedMemberClassValueConstructor},that);
  that.tipo=tipo;
  return that;
}
