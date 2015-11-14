package com.redhat.ceylon.model.typechecker.model;

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