function(name) {
  if ((typeof process !== "undefined") && (process.env !== undefined)) {
    return process.env[name];
  }
  return null;
}
