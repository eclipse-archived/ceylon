package com.redhat.ceylon.model.typechecker.model;

import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backends;

public interface Scoped extends Sourced {

    /**
     * A period-separated name uniquely representing this 
     * scope. 
     */
    public String getQualifiedNameString();

    /**
     * The "real" containing scope, ignoring that conditions 
     * (in an assert, if, or while) each have their own "fake" 
     * scope.
     * 
     * @see ConditionScope
     */
    public Scope getContainer();
    
    /**
     * The containing scope, taking into account that conditions 
     * (in an assert, if, or while) each have their own "fake" 
     * scope.
     * 
     * @see ConditionScope
     */
    public Scope getScope();
    
    /**
     * Get a set of proposals for use in IDE completion.
     * 
     * @param unit
     * @param startingWith a pattern to match the name against
     * @param proximity the "distance" from the carat
     * @return
     */
    public Map<String, DeclarationWithProximity> 
    getMatchingDeclarations(Unit unit, 
            String startingWith,
            int proximity,
            Cancellable canceller);

    /**
     * Get a list of all declarations belonging directly to
     * this scope, even declarations which aren't normally
     * visible.
     * 
     * Calling this method necessarily loads the whole 
     * containing scope, so please don't, at least not 
     * unless you really have to.
     * 
     * @return A list of everything directly contained in 
     *         this scope
     */
    public List<Declaration> getMembers();
    
    /**
     * Resolve a qualified reference.
     * 
     * @param name the name of the member
     * @param signature the signature of the parameter list,
     *        or null if we have no parameter list
     * @param ellipsis true if we are looking for a member
     *        with a variadic parameter
     * 
     * @return the best matching member
     */
    public Declaration getMember(String name, 
            List<Type> signature, 
            boolean variadic);
    
    /**
     * Resolve an unqualified reference.
     * 
     * @param name the name of the member
     * @param signature the signature of the parameter list,
     *        or null if we have no parameter list
     * @param ellipsis true if we are looking for a member
     *        with a variadic parameter
     * 
     * @return the best matching member
     */
    //TODO: should be renamed getBase() since it also looks 
    //      in containing scopes
    public Declaration getMemberOrParameter(Unit unit, 
            String name, 
            List<Type> signature, 
            boolean variadic);
    
    /**
     * Get a member declared directly in this scope.
     * 
     * @param name the name of the member
     * @param signature the signature of the parameter list,
     *        or null if we have no parameter list
     * @param ellipsis true if we are looking for a member
     *        with a variadic parameter
     * 
     * @return the best matching member
     */
    public Declaration getDirectMember(String name, 
            List<Type> signature, 
            boolean variadic);

    /**
     * Get a member declared directly in this scope for any of the given backends
     * 
     * @param name the name of the member
     * @param backends the native backends
     * 
     * @return the best matching member
     */
    public Declaration getDirectMemberForBackend(String name, Backends backends);

    /**
     * Is the given declaration inherited from
     * a supertype of this type or an outer
     * type?
     * 
     * @return true if it is
     */
    public boolean isInherited(Declaration d);
    
    /**
     * Get the containing type which inherits the given 
     * declaration.
     * 
     * @return null if the declaration is not inherited!!
     */
    public TypeDeclaration getInheritingDeclaration(Declaration d);

    public Type getDeclaringType(Declaration d);
    
    public boolean isToplevel();
    
    /**
     * Returns the native backend defined for this scope
     * or any of its containing scopes. Will return
     * <code>null</code> if no specific backend was
     * defined.
     */
    public Backends getScopedBackends();
}
