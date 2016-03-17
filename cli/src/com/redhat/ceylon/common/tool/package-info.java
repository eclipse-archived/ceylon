/**
 * <p>API for the Ceylon toolset.</p>
 * 
 * <h3>Terminology</h3>
 * <p>Consider the following hypothetical command line:</p>
 * <pre>ceylon example --foo --bar=A -c -def baz</pre>
 * <p>In the above</p>
 * <dl>
 * <dt><em>program name</em></dt>
 *   <dd>is the first element of the command line, {@code ceylon} in the 
 *   example<dd>
 * <dt><em>arguments</em></dt>
 *   <dd>can refer to everything following the <em>program name</em>, but more 
 *   usually refers to non-<em>option</em> arguments.<dd>
 * <dt><em>options</em></dt>
 *   <dd>are <em>arguments</em> which begin with a {@code --} or a {@code -} 
 *   and appear before the first <em>end of options</em> indicator 
 *   (conventionally a {@code --} on its own) if any. 
 *   An end of options indicator is not illustrated in the example)</dd>
 * <dt><em>pure options</em></dt>
 *   <dd>are <em>options</em> that do not accept a value</dd>
 * <dt><em>option arguments</em></dt>
 *   <dd>are <em>options</em> that do accept a value. This term can also refer to the 
 *   value of an option argument.</dd>
 * <dt><em>long options</em></dt>
 *   <dd>are <em>options</em> beginning with a {@code --} ({@code --foo} and 
 *   {@code --bar} in the example).</dd>
 * <dt><em>short options</em></dt>
 *   <dd>are <em>options</em> beginning with a single {@code -}. The simple 
 *   case is illustrated by {@code -c} in the above example. Short options are 
 *   always a single character, but <em>pure</em> short options can be 
 *   combined with a following short option. In such a combination only the 
 *   last short option in the group is allowed to be an option argument. 
 *   Short option arguments (that is to say the value of the short option 
 *   argument) may be merged with the short option itself, or follow it 
 *   as a separate argument. 
 *   So there are multiple interpretations of the {@code -def} in the 
 *   example above:
 *   <ol>
 *     <li>{@code d} is a short option argument whose value is {@code ef}</li>
 *     <li>{@code d} is a short pure option and {@code e} is a short option 
 *     argument whose value is {@code f}</li>
 *     <li>{@code d} and {@code e} are short pure options and {@code f} is a 
 *     short option argument whose value is {@code baz}</li>
 *     <li>{@code d}, {@code e} and {@code f} are short pure options and 
 *     {@code baz} is a plain argument (not an option's argument).</li>
 *   </ol>
 *   </dd>
 * 
 * <h3>Tools</h3>
 * <p>A <em>tool</em> is a class which implements {@link Tool} and adheres 
 * to the conventions it requires.</p>  
 */
package com.redhat.ceylon.common.tool;