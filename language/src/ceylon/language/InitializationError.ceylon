/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"[[Assertion failure|AssertionError]] that occurs when a 
 value reference could not be initialized, including when:
 
 - a toplevel value could not be initialized due to 
   recursive dependencies upon other toplevel values, 
 - an uninitialized [[late]] value is evaluated, 
 - an initialized `late` but non-`variable` value is
   reassigned."
see (function late)
since("1.1.0")
shared class InitializationError(String description)
        extends AssertionError(description) {}