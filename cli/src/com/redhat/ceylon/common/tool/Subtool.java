package com.redhat.ceylon.common.tool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Must be not be annotated with @Argument or @Option or @OptionArgument
 * Must be the *last* @Argument by order -- all following arguments are 
 * implictly consumed by the subtool
 * Must nominate a ToolLoader via a @ParserFactory
 *   (will need to provide some base classes to make that easier
 *    e.g. EnumToolLoader)
 * What about argument rearrangement? In theory it shouldn't be needed should it?
 * What about the role of -- with subtools?
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subtool {

    String argumentName() default "subcommand";
    int order() default 0;
    Class<? extends Tool>[] classes() default {};
    
}
