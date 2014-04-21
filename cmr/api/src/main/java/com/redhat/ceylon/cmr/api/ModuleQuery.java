package com.redhat.ceylon.cmr.api;

public class ModuleQuery {
    protected String name;
    protected Type type;
    private Long start;
    private Long count;
    private long[] pagingInfo;
    private Integer binaryMajor;
    private Integer binaryMinor;
    private String memberName;

    public enum Type {
        SRC(ArtifactContext.SRC), 
        JVM(ArtifactContext.CAR, ArtifactContext.JAR), 
        JS(ArtifactContext.JS),
        CODE(ArtifactContext.CAR, ArtifactContext.JAR, ArtifactContext.JS),
        ALL(ArtifactContext.SRC, ArtifactContext.CAR, ArtifactContext.JAR, ArtifactContext.JS);
        
        private String[] suffixes;

        Type(String... suffixes){
            this.suffixes = suffixes;
        }

        public String[] getSuffixes() {
            return suffixes;
        }
    }
    
    public ModuleQuery(String name, Type type){
        this.name = name.toLowerCase();
        this.type = type;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public boolean isPaging() {
        return count != null || start != null;
    }

    public void setPagingInfo(long[] pagingInfo) {
        this.pagingInfo = pagingInfo;
    }

    public long[] getPagingInfo() {
        return pagingInfo;
    }

    public Integer getBinaryMajor() {
        return binaryMajor;
    }

    public void setBinaryMajor(Integer binaryMajor) {
        this.binaryMajor = binaryMajor;
    }

    public Integer getBinaryMinor() {
        return binaryMinor;
    }

    public void setBinaryMinor(Integer binaryMinor) {
        this.binaryMinor = binaryMinor;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return "ModuleQuery[name=" + name + ",type=" + type + "]";
    }

}
