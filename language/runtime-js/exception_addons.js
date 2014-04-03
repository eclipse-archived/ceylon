function $init$native$Throwable$before(exc) {
  var _caller=arguments.callee.caller.caller;
  exc.stack_trace=[];
  var ilc=0;
  var ilf=null;
  while(_caller) {
    exc.stack_trace.push(_caller);
    _caller = _caller.caller;
    if (_caller===ilf) {
      ilc++;
      if (ilc>2) {
        exc.stack_trace.push("CIRCULAR");
        _caller = null;
      }
    } else if (ilc===0 && _caller && exc.stack_trace.contains(_caller)) {
      ilc=1;
      ilf=_caller;
    }
  }
}
function printStackTrace(exc, _write) {
  if (_write===undefined)_write=getProcess().writeError;
  var _c = className(exc);
  if (exc.message.size > 0) {
    _c += ' "' + exc.message + '"';
  }
  _write(_c);_write(getOperatingSystem().newline);
  for (var i=0; i<exc.stack_trace.length; i++) {
    var f = exc.stack_trace[i];
    if (f == "CIRCULAR") {
      _write("    (Infinite loop detected)");
      _write(getOperatingSystem().newline);
      return;
    }
    var mm = getrtmm$$(f);
    if (mm) {
      var _src = '';
      if (i==0) {
        if (exc.$loc && exc.$file) _src = ' (' + exc.$file + " " + exc.$loc + ')';
      }
      var pkn=mm.d[0];
      if (pkn==='$')pkn='ceylon.language';
      _write("    at " + pkn + "::" + mm.d[mm.d.length-1] + _src);_write(getOperatingSystem().newline);
    }
  }
}
printStackTrace.$crtmm$=function(){
  return{mod:$CCMM$,d:['ceylon.language','printStackTrace'],$t:{t:Anything},$ps:[
    {$nm:'exception',$t:{t:Throwable},$mt:'prm'},{$nm:'write',$t:{t:Callable,a:{Return:{t:Anything},Arguments:{t:'T',l:[{t:String$}]}}},$mt:'prm','$def':1}
  ]};};
exports.printStackTrace=printStackTrace;

defineAttr(Throwable.$$.prototype, 'suppressed', function(){
  return this.$sups$||getEmpty();
},undefined,function(){return{mod:$CCMM$,$t:{t:Sequential,a:{Element$Sequential:{t:Throwable}}},$an:function(){return[shared(),native()];},d:['$','Throwable','$at','suppressed']};});
