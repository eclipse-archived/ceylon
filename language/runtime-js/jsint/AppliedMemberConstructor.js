function AppliedMemberConstructor$jsint(tipo,$$targs$$,that,myTargs) {
  if ($$targs$$.Type$AppliedMemberConstructor===undefined)$$targs$$.Type$AppliedMemberConstructor=$$targs$$.Type$Method;
  if ($$targs$$.Container$AppliedMemberConstructor===undefined)$$targs$$.Container$AppliedMemberConstructor=$$targs$$.Type$Method;
  if ($$targs$$.Arguments$AppliedMemberConstructor===undefined)$$targs$$.Arguments$AppliedMemberConstructor=$$targs$$.Type$Method;
  $init$AppliedMemberConstructor$jsint();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    that=new AppliedMemberConstructor$jsint.$$;
  }
  MemberClassConstructor$meta$model({Type$MemberClassConstructor:$$targs$$.Type$AppliedMemberConstructor,
    Arguments$MemberClassConstructor:$$targs$$.Arguments$AppliedMemberConstructor,
    Container$MemberClassConstructor:$$targs$$.Container$AppliedMemberConstructor},that);
  set_type_args(that,$$targs$$,AppliedMemberConstructor$jsint);
  that.$targs=myTargs;
  that.tipo=tipo;
  return that;
}
