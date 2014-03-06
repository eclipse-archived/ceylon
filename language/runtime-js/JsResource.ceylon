class JsResource(uri) satisfies Resource {
  shared actual String uri;
  shared actual Integer size => -1;
  shared actual String textContent(String encoding) {
    throw Exception("IMPL!");
  }
}
