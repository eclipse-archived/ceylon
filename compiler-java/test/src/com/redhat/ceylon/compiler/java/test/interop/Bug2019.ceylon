import com.redhat.ceylon.compiler.java.test.interop.access{JavaBug2019}

shared void bug2019() {
    value jb = JavaBug2019();
    variable String s;
    
    s = jb.pubProt;
    @error:"method or attribute is not visible: 'setPubProt' of type 'JavaBug2019' is protected"
    jb.setPubProt("value");
    
    s = jb.pubDef;
    @error:"method or attribute is not visible: 'setPubDef' of type 'JavaBug2019' is package private"
    jb.setPubDef("value");
    
    s = jb.pubPriv;
    @error:"method or attribute is not defined: 'setPubPriv' in type 'JavaBug2019'"
    jb.setPubPriv("value");
    
    
    @error:"method or attribute is not visible: 'protPub' of type 'JavaBug2019' is protected"
    s = jb.protPub;
    jb.setProtPub("value");
    
    @error:"method or attribute is not visible: 'protDef' of type 'JavaBug2019' is protected"
    s = jb.protDef;
    @error:"method or attribute is not visible: 'setProtDef' of type 'JavaBug2019' is package private"
    jb.setProtDef("value");
    
    @error:"method or attribute is not visible: 'protPriv' of type 'JavaBug2019' is protected"
    s = jb.protPriv;
    @error:"method or attribute is not defined: 'setProtPriv' in type 'JavaBug2019'"
    jb.setProtPriv("value");
    
    
    @error:"method or attribute is not visible: 'defPub' of type 'JavaBug2019' is package private"
    s = jb.defPub;
    jb.setDefPub("value");
    
    @error:"method or attribute is not visible: 'defProt' of type 'JavaBug2019' is package private"
    s = jb.defProt;
    @error:"method or attribute is not visible: 'setDefProt' of type 'JavaBug2019' is protected"
    jb.setDefProt("value");
    
    @error:"method or attribute is not visible: 'defPriv' of type 'JavaBug2019' is package private"
    s = jb.defPriv;
    @error:"method or attribute is not defined: 'setDefPriv' in type 'JavaBug2019'"
    jb.setDefPriv("value");
    
    
    @error:"method or attribute is not defined: 'privPub' in type 'JavaBug2019'"
    s = jb.privPub;
    jb.setPrivPub("value");
    
    @error:"method or attribute is not defined: 'privProt' in type 'JavaBug2019'"
    s = jb.privProt;
    @error:"method or attribute is not visible: 'setPrivProt' of type 'JavaBug2019' is protected"
    jb.setPrivProt("value");
    
    @error:"method or attribute is not defined: 'privDef' in type 'JavaBug2019'"
    s = jb.privDef;
    @error:"method or attribute is not visible: 'setPrivDef' of type 'JavaBug2019' is package private"
    jb.setPrivDef("value");
}