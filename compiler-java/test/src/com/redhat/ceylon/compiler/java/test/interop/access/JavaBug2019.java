package com.redhat.ceylon.compiler.java.test.interop.access;

public class JavaBug2019 {
    public String getPubProt() { // or package private
        return "";
    }
    protected void setPubProt(String prop) {
    }
    
    public String getPubDef() { // or package private
        return "";
    }
    void setPubDef(String prop) {
    }
    
    public String getPubPriv() { // or package private
        return "";
    }
    private void setPubPriv(String prop) {
    }
    
    
    protected String getProtPub() { // or package private
        return "";
    }
    public void setProtPub(String prop) {
    }
    
    protected String getProtDef() { // or package private
        return "";
    }
    void setProtDef(String prop) {
    }
    
    protected String getProtPriv() { // or package private
        return "";
    }
    private void setProtPriv(String prop) {
    }
    
    String getDefPub() { // or package private
        return "";
    }
    public void setDefPub(String prop) {
    }
    
    String getDefProt() { // or package private
        return "";
    }
    protected void setDefProt(String prop) {
    }
    
    String getDefPriv() { // or package private
        return "";
    }
    private void setDefPriv(String prop) {
    }
    
    
    private String getPrivPub() { // or package private
        return "";
    }
    public void setPrivPub(String prop) {
    }
    
    private String getPrivProt() { // or package private
        return "";
    }
    protected void setPrivProt(String prop) {
    }
    
    private String getPrivDef() { // or package private
        return "";
    }
    void setPrivDef(String prop) {
    }
}
