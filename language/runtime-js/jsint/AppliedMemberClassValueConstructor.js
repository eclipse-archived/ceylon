function AppliedMemberClassValueConstructor$jsint(tipo,$$targs$$,that) {
  if (!$$targs$$.Set$AppliedMemberClassValueConstructor)$$targs$$.Set$AppliedValueConstructor=$$targs$$.Set$MemberClassValueConstructor;
  if (!$$targs$$.Type$AppliedMemberClassValueConstructor)$$targs$$.Type$AppliedValueConstructor=$$targs$$.Type$MemberClassValueConstructor;
  if (!$$targs$$.Container$AppliedMemberClassValueConstructor)$$targs$$.Container$AppliedValueConstructor=$$targs$$.Container$MemberClassValueConstructor;
  $init$AppliedMemberClassValueConstructor$jsint();
  if (that===undefined)that=new AppliedMemberClassValueConstructor$jsint.$$;
  set_type_args(that,$$targs$$);
  MemberClassValueConstructor$meta$model({Set$MemberClassValueConstructor:$$targs$$.Set$AppliedMemberClassValueConstructor,
    Type$MemberClassValueConstructor:$$targs$$.Type$AppliedMemberClassValueConstructor,
    Container$MemberClassValueConstructor:$$targs$$.Container$AppliedMemberClassValueConstructor},that);
  that.tipo=tipo;
  return that;
}
