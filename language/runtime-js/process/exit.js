(function(){
  if (run$isNode() && (process.exit !== undefined)) {
    return function(code) {
        process.exit(code);
    }
  }
  return function() {}
})()
