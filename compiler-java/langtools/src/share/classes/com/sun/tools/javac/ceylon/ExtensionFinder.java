package com.sun.tools.javac.ceylon;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;

import static com.sun.tools.javac.code.Kinds.*;
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
    private final Resolve rs;

    private ExtensionFinder(Context context) {
        syms = Symtab.instance(context);
        types = Types.instance(context);
        rs = Resolve.instance(context);
    }

    private static class RouteElement {
        public final Type type; // the type we are extending from
        public Symbol sym;      // the symbol we are extending with

        public RouteElement(Type type, Symbol sym) {
            this.type = type;
            this.sym = sym;
        }
    }

    private abstract class Finder {
        public class Route {
            private List<RouteElement> elements = List.<RouteElement>nil();
            private final Type target;

            public Route(List<RouteElement> elements) {
                for (RouteElement element : elements) {
                    this.elements = this.elements.prepend(new RouteElement(element.type, element.sym));
                }
                target = elements.head.type;
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

        protected abstract boolean isTarget(Type type);

        public void visit(Type source) {
            if (isTarget(source)) {
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
                    visit(sym.ceylonIntroducedType());
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

    private class TypeFinder extends Finder {
        private final Type target;

        public TypeFinder(Type target) {
            this.target = target;
        }

        protected boolean isTarget(Type type) {
            return types.isSubtype(type, target);
        }
    }

    private class MethodFinder extends Finder {
        private final DiagnosticPosition pos;
        private final Env<AttrContext> env;
        private final Name name;
        private final List<Type> argtypes;
        private final List<Type> typeargtypes;

        public MethodFinder(DiagnosticPosition pos, Env<AttrContext> env, Name name, List<Type> argtypes, List<Type> typeargtypes) {
            this.pos = pos;
            this.env = env;
            this.name = name;
            this.argtypes = argtypes;
            this.typeargtypes = typeargtypes;
        }

        protected boolean isTarget(Type type) {
            return rs.resolveQualifiedMethod(env, type, name, argtypes, typeargtypes).kind == MTH;
        }
    }

    public static class Route {
        private final List<RouteElement> elements;

        private Route(List<RouteElement> elements) {
            this.elements = elements;
        }

        public JCExpression apply(JCExpression tree, TreeMaker make) {
            for (RouteElement element : elements) {
                Symbol sym = element.sym;
                tree = sym.doCeylonExtension(tree, make);
                tree.setType(sym.ceylonIntroducedType());
            }
            return tree;
        }
    }

    private Route findUniqueRoute(Type source, Finder finder) {
        finder.visit(source);
        finder.cull();
        if (finder.routes.size() == 1)
            return new Route(finder.routes.head.elements);
        return null;
    }

    /**
     * Find a unique route of extensions to convert from one type
     * to the specified other type.
     */
    public Route findUniqueRoute(Type source, Type target) {
        return findUniqueRoute(source, new TypeFinder(target));
    }

    /**
     * Find a unique route of extensions to convert from one type
     * to an unspecified other type that has the specified method.
     */
    public Route findUniqueRoute(Type source,
                                 Name name, List<Type> argtypes, List<Type> typeargtypes,
                                 DiagnosticPosition pos, Env<AttrContext> env) {
        return findUniqueRoute(source, new MethodFinder(pos, env, name, argtypes, typeargtypes));
    }
}
