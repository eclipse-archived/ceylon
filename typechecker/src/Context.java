import com.redhat.ceylon.compiler.model.Module;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Structure;

import static com.redhat.ceylon.compiler.util.PrintUtil.importPathToString;

/**
 * Keep compiler contextual information like the package stack and the current module
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
class Context {
    private LinkedList<Package> packageStack = new LinkedList<Package>();
    private Module module;
    private Package defaultPackage;
    private List<PhasedUnit> phasedUnits = new ArrayList<PhasedUnit>();

    Context() {
        final Package pkg = new Package();
        pkg.setName( new ArrayList<String>(0) );
        packageStack.add(pkg);
    }

    public Module getModule() {
        return module;
    }

    public void push(String path) {
        createPackageAndAddToModule(path);
    }

    private void createPackageAndAddToModule(String path) {
        Package pkg = new Package();
        final Package lastPkg = packageStack.peekLast();
        List<String> parentName = lastPkg.getName();
        final ArrayList<String> name = new ArrayList<String>(parentName.size() + 1);
        name.addAll( parentName );
        name.add(path);
        pkg.setName(name);
        if (module != null) {
            bindPackageToCurrentModule(pkg);
        }
        //FIXME this is a total hack, should be an implicit import of the ceylon.language module (unless we compile ceylon.language :) )
        //add ceylon.language elements to the package
        if (defaultPackage != null) {
            final List<Structure> packageMembers = pkg.getMembers();
            for ( Structure structure : defaultPackage.getMembers() ) {
                packageMembers.add(structure);
            }
        }
        packageStack.addLast(pkg);
    }

    private void bindPackageToCurrentModule(Package pkg) {
        module.getPackages().add(pkg);
        pkg.setModule(module);
    }

    public void pop() {
        removeLastPackageAndModuleIfNecessary();
    }

    private void removeLastPackageAndModuleIfNecessary() {
        packageStack.pollLast();
        final boolean moveAboveModuleLevel = module != null && module.getName().size() > packageStack.size();
        if (moveAboveModuleLevel) {
            module = null;
        }
    }

    public void defineModule() {
        if ( module == null ) {
            module = new Module();
            final Package currentPkg = packageStack.peekLast();
            final List<String> moduleName = currentPkg.getName();
            if (moduleName.size() == 0) {
                throw new RuntimeException("Module cannot be top level");
            }
            module.setName(moduleName);
            bindPackageToCurrentModule(currentPkg);
        }
        else {
            StringBuilder error = new StringBuilder("Found two modules within the same hierarchy: '");
            error.append( importPathToString( module.getName() ) )
                .append( "' and '" )
                .append( importPathToString( packageStack.peekLast().getName() ) )
                .append("'");
            //throw new RuntimeException( error.toString() );
            System.err.println(error);
        }
    }

    public Package getPackage() {
        return packageStack.peekLast();
    }

    public void setDefaultPackage(Package pkg) {
        defaultPackage = pkg;
    }

    public Package getDefaultPackage() {
        return defaultPackage;
    }

    public void addStagedUnit(PhasedUnit phasedUnit) {
        this.phasedUnits.add(phasedUnit);
    }

    public List<PhasedUnit> getPhasedUnits() {
        return phasedUnits;
    }
}
