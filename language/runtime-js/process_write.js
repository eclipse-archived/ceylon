(function() {
  if ((typeof process !== "undefined") && (process.stdout !== undefined)) {
    return function(string) {
      if(string)process.stdout.write(string.valueOf());
    }
  } else if ((typeof console !== "undefined") && (console.log !== undefined)) {
    return function(line) {
        console.log(line?line.valueOf():'');
    }
  }
  return function(){};
})()
