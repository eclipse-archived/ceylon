/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The type of the value that indicates that an [[Iterator]] 
 is exhausted and has no more values to return."
see (interface Iterator)
tagged("Streams")
shared abstract class Finished() of finished {}

"A value that indicates that an [[Iterator]] is exhausted 
 and has no more values to return."
see (interface Iterator)
tagged("Streams")
shared object finished extends Finished() {
    string => "finished";
}

