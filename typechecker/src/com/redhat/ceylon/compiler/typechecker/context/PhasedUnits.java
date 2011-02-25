package com.redhat.ceylon.compiler.typechecker.context;

import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains phased units
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PhasedUnits {
    private List<PhasedUnit> phasedUnits = new ArrayList<PhasedUnit>();
    private Map<VirtualFile,PhasedUnit> phasedUnitPerFile = new HashMap<VirtualFile,PhasedUnit>();

    public void addPhasedUnit(VirtualFile unitFile, PhasedUnit phasedUnit) {
        //TODO do we need the ordering??, we could get rid of the List and use map.valueSet()
        this.phasedUnits.add(phasedUnit);
        this.phasedUnitPerFile.put(unitFile, phasedUnit);
    }

    public List<PhasedUnit> getPhasedUnits() {
        return phasedUnits;
    }

    public PhasedUnit getPhasedUnit(VirtualFile file) {
        return phasedUnitPerFile.get(file);
    }
}
