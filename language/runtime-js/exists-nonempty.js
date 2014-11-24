//nonempty
function ne$(value){
  return value!==null&&value!==undefined&&is$(value,{t:Sequence});
}

//exists
function nn$(e) {
  return e!==null&&e!==undefined&&e!==getNull();
}
ex$.ne$=ne$;
ex$.nn$=nn$;
