package com.redhat.ceylon.cmr.api;

public class ModuleVersionArtifact implements Comparable<ModuleVersionArtifact> {
    private String suffix;
    private Integer majorBinaryVersion;
    private Integer minorBinaryVersion;
    
    public ModuleVersionArtifact(String suffix, Integer majorBinaryVersion, Integer minorBinaryVersion) {
        this.suffix = suffix;
        this.majorBinaryVersion = majorBinaryVersion;
        this.minorBinaryVersion = minorBinaryVersion;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getMajorBinaryVersion() {
        return majorBinaryVersion;
    }

    public void setMajorBinaryVersion(Integer majorBinaryVersion) {
        this.majorBinaryVersion = majorBinaryVersion;
    }

    public Integer getMinorBinaryVersion() {
        return minorBinaryVersion;
    }

    public void setMinorBinaryVersion(Integer minorBinaryVersion) {
        this.minorBinaryVersion = minorBinaryVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModuleVersionArtifact that = (ModuleVersionArtifact) o;
        return that.suffix.toUpperCase().equals(suffix.toUpperCase())
                && eq(that.majorBinaryVersion, majorBinaryVersion)
                && eq(that.minorBinaryVersion, minorBinaryVersion);
    }

    private boolean eq(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        if (o1 != null && o2 != null) {
            return o1.equals(o2);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public int compareTo(ModuleVersionArtifact obj) {
        ModuleVersionArtifact that = (ModuleVersionArtifact)obj;
        int res = suffix.toUpperCase().compareTo(that.suffix.toUpperCase());
        if (res == 0) {
            res = cmp(majorBinaryVersion, that.majorBinaryVersion);
            if (res == 0) {
                res = cmp(minorBinaryVersion, that.minorBinaryVersion);
            }
        }
        return res;
    }

    private <T extends Comparable<T>> int cmp(T o1, T o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null && o2 != null) {
            return -1;
        }
        if (o1 != null && o2 == null) {
            return 1;
        }
        return o1.compareTo(o2);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(suffix.toUpperCase());
        if (majorBinaryVersion != null || minorBinaryVersion != null) {
            str.append(" (#");
        }
        if (majorBinaryVersion != null) {
            str.append(majorBinaryVersion);
        }
        if (minorBinaryVersion != null) {
            str.append(".");
            str.append(minorBinaryVersion);
        }
        if (majorBinaryVersion != null || minorBinaryVersion != null) {
            str.append(" )");
        }
        return str.toString();
    }
    
}