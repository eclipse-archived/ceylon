function ArraySequence(/* {Element+} */items, $$targs$$) {
  var value = [];
  var iter=items.iterator();
  var item;while((item=iter.next())!==getFinished())value.push(item);
  value.seq$ = true;
  var t=$$targs$$.Element$Iterable;
  if (t===undefined)t=$$targs$$.Element$ArraySequence;
  if (t===undefined)t=$$targs$$.Element$Array;
  if (t===undefined)t=$$targs$$.Element$Sequence;
  if (t===undefined)t=$$targs$$.Element$Sequential;
  if (t===undefined)t=$$targs$$.Element$List;
  if (t===undefined)throw new Error("Invalid type arguments for ArraySequence: "+/*require('util').inspect(*/$$targs$$);
  Sequence({Element$Sequence:t},value);
  value.seq$ = true;
  value.$$targs$$.Element$ArraySequence=t;
  value.$$targs$$.Element$Array=t;
  return value;
}
ArraySequence.$crtmm$=function(){return{mod:$CCMM$,d:['$','ArraySequence'],$ps:[{$nm:'elements',$t:{t:Iterable,a:{Element$Iterable:'Element$ArraySequence',Absent$Iterable:{t:Nothing}}}}],$tp:{Element$ArraySequence:{'var':'out'}},satisfies:[{t:Sequence,a:{Element$Sequence:'Element$ArraySequence'}}]};};
initTypeProto(ArraySequence, 'ceylon.language::ArraySequence', $init$Basic(), $init$Sequence());
ex$.ArraySequence=ArraySequence;
