package com.sun.tools.javac.ceylon;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import static com.sun.tools.javac.code.TypeTags.*;

public class ExtensionFinder {
    private static final Context.Key<ExtensionFinder> extensionFinderKey =
        new Context.Key<ExtensionFinder>();

    public static ExtensionFinder instance(Context context) {
        ExtensionFinder instance = context.get(extensionFinderKey);
        if (instance == null) {
            instance = new ExtensionFinder(context);
            context.put(extensionFinderKey, instance);
        }
        return instance;
    }
    
    private final Symtab syms;
    private final Types types;

    private ExtensionFinder(Context context) {
        syms = Symtab.instance(context);
        types = Types.instance(context);        
    }
    
    class Finder {
        private final Type target;
        
        public Finder(Type target) {
            this.target = target;
        }

        public class RouteElement {
            public final Type type; // the type we are extending from
            public Symbol sym;      // the symbol we are extending with
        
            public RouteElement(Type type, Symbol sym) {
                this.type = type;
                this.sym = sym;
            }
        }        

        public class Route {
            private List<RouteElement> elements = List.<RouteElement>nil();
            
            public Route(List<RouteElement> elements) {
                for (RouteElement element : elements) {
                    this.elements = this.elements.prepend(new RouteElement(element.type, element.sym));
                }
            }
            
            public boolean isLongerVersionOf(Route other) {
                RouteElement start = new RouteElement(target, null);
                return isSubset(other.elements.append(start), elements.append(start));
            }
            
            private boolean isSubset(List<RouteElement> a, List<RouteElement> b) {
                // If this is the last step of either list then we're done
                if (a.tail.isEmpty() || b.tail.isEmpty())
                    return false;
                
                // If we aren't starting from the same place then these are different routes
                Type source = a.head.type;
                if (!types.isSameType(source, b.head.type))
                    return false;
                
                // If we're going to the same place then check the next hop 
                Type target = a.tail.head.type;
                if (types.isSameType(target, b.tail.head.type))
                    return isSubset(a.tail, b.tail);
                
                // Have we found a subchain?
                for (RouteElement element : b.tail) {
                    if (types.isSameType(target, element.type))
                        return true;
                }

                // This route isn't a subset of the other
                return false;
            }
        }
        
        private List<RouteElement> stack = List.<RouteElement>nil();
        private List<Route> routes = List.<Route>nil();

        public void visit(Type source) {
            if (types.isSubtype(source, target)) {
                routes = routes.append(new Route(stack));
                return;
            }
            
            // Check we are not about to loop
            // FIXME: this is linear and hence potentially slow
            // XXX: should we report errors if loops are found?
            for (RouteElement element : stack) {
                if (types.isSameType(source, element.type))
                    return;
            }
            stack = stack.prepend(new RouteElement(source, null));

            // Visit class members
            if (source.tag == CLASS) {
                for (Symbol sym : ((ClassSymbol) source.tsym).members().getElements()) {
                    if (sym.attribute(syms.ceylonExtensionType.tsym) == null)
                        continue;

                    stack.head.sym = sym;
                    visit(((MethodSymbol) sym).getReturnType());
                }
            }
            
            // Pop our level off the stack before returning
            stack = stack.tail;
        }
        
        // Remove routes that contain multi-step conversions that are possible with a single step
        // XXX: with lots of routes this has the potential to be very slow!
        public void cull() {
            List<Route> tmp = List.<Route>nil();
            for (Route a : routes) {
                boolean culled = false;
                for (Route b : routes) {
                    if (a != b && a.isLongerVersionOf(b)) {
                        culled = true;
                        break;
                    }
                }
                if (!culled) {
                    tmp = tmp.append(a);
                }
            }
            routes = tmp;
        }
    }

    public Finder.Route extend(Type source, Type target) {
        Finder finder = new Finder(target);
        finder.visit(source);
        finder.cull();
        if (finder.routes.size() == 1)
            return finder.routes.head;
        return null;
    }
}
