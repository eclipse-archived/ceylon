import com.redhat.ceylon.compiler.java.test.interop.access{JavaBug2019}

shared void bug2019() {
    value jb = JavaBug2019();
    variable String s;
    
    s = jb.pubProt;
    @error:"protected method or attribute is not visible: 'setPubProt' of type 'JavaBug2019'"
    jb.setPubProt("value");
    
    s = jb.pubDef;
    @error:"package private method or attribute is not visible: 'setPubDef' of type 'JavaBug2019'"
    jb.setPubDef("value");
    
    s = jb.pubPriv;
    @error:"method or attribute does not exist: 'setPubPriv' in type 'JavaBug2019'"
    jb.setPubPriv("value");
    
    
    @error:"protected method or attribute is not visible: 'protPub' of type 'JavaBug2019'"
    s = jb.protPub;
    jb.setProtPub("value");
    
    @error:"protected method or attribute is not visible: 'protDef' of type 'JavaBug2019'"
    s = jb.protDef;
    @error:"package private method or attribute is not visible: 'setProtDef' of type 'JavaBug2019'"
    jb.setProtDef("value");
    
    @error:"protected method or attribute is not visible: 'protPriv' of type 'JavaBug2019'"
    s = jb.protPriv;
    @error:"method or attribute does not exist: 'setProtPriv' in type 'JavaBug2019'"
    jb.setProtPriv("value");
    
    
    @error:"package private method or attribute is not visible: 'defPub' of type 'JavaBug2019'"
    s = jb.defPub;
    jb.setDefPub("value");
    
    @error:"package private method or attribute is not visible: 'defProt' of type 'JavaBug2019'"
    s = jb.defProt;
    @error:"protected method or attribute is not visible: 'setDefProt' of type 'JavaBug2019'"
    jb.setDefProt("value");
    
    @error:"package private method or attribute is not visible: 'defPriv' of type 'JavaBug2019'"
    s = jb.defPriv;
    @error:"method or attribute does not exist: 'setDefPriv' in type 'JavaBug2019'"
    jb.setDefPriv("value");
    
    
    @error:"method or attribute does not exist: 'privPub' in type 'JavaBug2019'"
    s = jb.privPub;
    jb.setPrivPub("value");
    
    @error:"method or attribute does not exist: 'privProt' in type 'JavaBug2019'"
    s = jb.privProt;
    @error:"protected method or attribute is not visible: 'setPrivProt' of type 'JavaBug2019'"
    jb.setPrivProt("value");
    
    @error:"method or attribute does not exist: 'privDef' in type 'JavaBug2019'"
    s = jb.privDef;
    @error:"package private method or attribute is not visible: 'setPrivDef' of type 'JavaBug2019'"
    jb.setPrivDef("value");
}