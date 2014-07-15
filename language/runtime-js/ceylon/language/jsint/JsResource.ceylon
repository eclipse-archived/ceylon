native class JsResource(uri) satisfies Resource {
  shared actual String uri;
  shared actual native Integer size;
  shared actual native String textContent(String encoding);
}
