//nonempty
function ne$(s){
  return s!==null&&s!==undefined&&is$(s,{t:Sequence});
}
//exists
function nn$(e) {
  return e!==null&&e!==undefined&&e!==$_null();
}
x$.ne$=ne$;
x$.nn$=nn$;
