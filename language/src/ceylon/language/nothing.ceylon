/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A value getter of type `Nothing`. The expression `nothing`
 is formally assignable to any type, but produces an 
 exception when evaluated.
 
 (This is most useful for tool-generated implementations of
 `formal` members.)"
throws (class AssertionError, 
        "when evaluated")
tagged("Basic types")
shared Nothing nothing {
    "nothing may not be evaluated"
    assert (false);
}
