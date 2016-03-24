(function(){
  if ((typeof process !== "undefined") && (process.exit !== undefined)) {
    return function(code) {
        process.exit(code);
    }
  }
  return function() {}
})()
