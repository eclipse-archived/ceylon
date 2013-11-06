function $init$native$Exception$before(exc) {
  var _caller=arguments.callee.caller.caller;
  exc.stack_trace=[];
  while(_caller) {
    exc.stack_trace.push(_caller);
    _caller = _caller.caller;
  }
}
function printStackTrace(exc, _write) {
  if (_write===undefined)_write=getProcess().writeErrorLine;
  var _c = className(exc);
  if (exc.message.size > 0) {
    _c += ' "' + exc.message + '"';
  }
  _write(_c);
  for (var i=0; i<exc.stack_trace.length; i++) {
    var f = exc.stack_trace[i];
    var mm = f.$$metamodel$$;
    if (typeof(mm)==='function') {
      mm = mm();
      f.$$metamodel$$=mm;
    }
    if (mm) {
      var _src = '';
      if (i==0) {
        if (exc.$loc && exc.$file) _src = ' (' + exc.$file + " " + exc.$loc + ')';
      }
      _write("    at " + mm.d[0] + "::" + mm.d[mm.d.length-1] + _src);
    }
  }
}
printStackTrace.$$metamodel$$=function(){
  return{mod:$$METAMODEL$$,d:['ceylon.language','printStackTrace'],$t:{t:Anything},$ps:[
    {$nm:'exception',$t:{t:Exception},$mt:'prm'},{$nm:'write',$t:{t:Callable,a:{Return:{t:Anything},Arguments:{t:'T',l:[{t:String$}]}}},$mt:'prm','$def':1}
  ]};};
exports.printStackTrace=printStackTrace;
