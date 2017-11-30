/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Print the stack trace of the given [[Exception]] using the 
 given [[function|write]], or to 
 [[standard error|process.writeError]] if no function is 
 specified."
tagged("Environment")
shared native void printStackTrace(Throwable exception, 
        "A function that prints the given string.
         Defaults to [[process.writeError]]."
        void write(String string) 
                => process.writeError(string));
