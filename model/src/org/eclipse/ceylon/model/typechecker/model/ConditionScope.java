/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

/**
 * The scope belonging to a condition in a condition list
 * (of an assert, if, or while). This is not an ordinary
 * scope because really things belonging to a condition
 * scope are visible in the outer scope to which the 
 * condition list belongs.
 * 
 * This is needed because each previous condition in the
 * list can refine a declaration of the outer scope. And
 * then in an assert statement, it is the refined 
 * declaration that is visible from the outer scope.
 *
 */
public class ConditionScope extends ControlBlock {}