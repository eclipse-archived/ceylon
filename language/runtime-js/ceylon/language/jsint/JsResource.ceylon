/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
native class JsResource(uri, path, mod) satisfies Resource {
  shared dynamic mod; 
  shared String path;
  shared actual String uri;
  shared actual native Integer size;
  shared actual native String textContent(String encoding);
}
