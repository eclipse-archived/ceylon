function(name) {
  if (run$isNode() && (process.env !== undefined)) {
    return process.env[name];
  }
  return null;
}
