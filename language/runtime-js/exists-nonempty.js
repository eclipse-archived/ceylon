//nonempty
function ne$(value){
  return value!==null&&value!==undefined&&(is$(value,{t:Sequence})||(is$(value,{t:List})&&value.size>0));
}

//exists
function nn$(e) {
  return e!==null&&e!==undefined&&e!==$_null();
}
x$.ne$=ne$;
x$.nn$=nn$;
