native class JsResource(uri, path, mod) satisfies Resource {
  shared dynamic mod; 
  shared String path;
  shared actual String uri;
  shared actual native Integer size;
  shared actual native String textContent(String encoding);
}
