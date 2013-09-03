/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

@noanno
class Bug1155() {
    
    // TODO String(String, String*) being a String(String+)
    
    // TODO Test a (String=, String*)
    // TODO Test a (Sequential, Sequential*)
    // TODO Test a (Sequential=, Sequential*)
    // TODO Test a (Sequential+)
    // TODO Test a (Sequence+)
    
    
    shared String star(String* variadic) 
        => "star(; ``variadic``)";
    shared String plus(String+ variadic) 
        => "plus(; ``variadic``)";
    shared String unaryOpt(String s1="d1") 
        => "unaryOpt(``s1``)";
    shared String unaryStar(String s1, String* variadic) 
        => "unaryStar(``s1``; ``variadic``)";
    shared String unaryOptStar(String s1="d1", String* variadic) 
        => "unaryOptStar(``s1``; ``variadic``)";
    shared String unaryPlus(String s1, String+ variadic) 
        => "unaryPlus(``s1``; ``variadic``)";
    shared String binaryOpt(String s1, String s2="d2") 
        => "binaryOpt(``s1``, ``s2``)";
    shared String binaryOptOpt(String s1="d1", String s2="d2") 
        => "binaryOptOpt(``s1``, ``s2``)";
    shared String binaryStar(String s1, String s2, String* variadic) 
        => "binaryStar(``s1``, ``s2``; ``variadic``)";
    shared String binaryPlus(String s1, String s2, String+ variadic) 
        => "binaryPlus(``s1``, ``s2``; ``variadic``)";
    /*
    shared String ternaryStar(String s1, String s2, String s3, String* variadic) 
        => "ternaryStar(``s1``, ``s2``, ``s3``; ``variadic``)";
    shared String ternaryPlus(String s1, String s2, String s3, String+ variadic) 
        => "ternaryPlus(``s1``, ``s2``, ``s3``; ``variadic``)";
    */
}

@noanno
void bug1155_unaryOpt(Bug1155 b) {
    void eq(String expect, String got) {
        if (expect != got) {
            throw Exception("Expected ``expect`` but got ``got``.");
        }
    }
    
    // A String(String=) is a String(String=)
    String(String=) unaryOptRef1Opt = b.unaryOpt;
    eq("unaryOpt(d1)", unaryOptRef1Opt());
    eq("unaryOpt(s1)", unaryOptRef1Opt("s1"));
    
    // A String(String=) is a String(String)
    String(String) unaryOptRef1 = b.unaryOpt;
    eq("unaryOpt(s1)", unaryOptRef1("s1"));
    
    // A String(String=) is a String()
    String() unaryOptRef0 = b.unaryOpt;
    eq("unaryOpt(d1)", unaryOptRef0());
}

@noanno
void bug1155_binaryOpt(Bug1155 b) {
    void eq(String expect, String got) {
        if (expect != got) {
            throw Exception("Expected ``expect`` but got ``got``.");
        }
    }
    
    // A String(String, String=) is a String(String, String=)
    String(String, String=) binaryOptRefOpt = b.binaryOpt;
    eq("binaryOpt(s1, d2)", binaryOptRefOpt("s1"));
    eq("binaryOpt(s1, s2)", binaryOptRefOpt("s1", "s2"));
    
    // A String(String, String=) is a String(String, String)
    String(String, String) binaryOptRef = b.binaryOpt;
    eq("binaryOpt(s1, s2)", binaryOptRef("s1", "s2"));
    
    // A String(String, String=) is a String(String)
    String(String) binaryOptRef1 = b.binaryOpt;
    eq("binaryOpt(s1, d2)", binaryOptRef1("s1"));
}

@noanno
void bug1155_binaryOptOpt(Bug1155 b) {
    void eq(String expect, String got) {
        if (expect != got) {
            throw Exception("Expected ``expect`` but got ``got``.");
        }
    }
    
    // A String(String=, String=) is a String(String=, String=)
    String(String=, String=) binaryOptOptRef = b.binaryOptOpt;
    eq("binaryOptOpt(d1, d2)", binaryOptOptRef());
    eq("binaryOptOpt(s1, d2)", binaryOptOptRef("s1"));
    eq("binaryOptOpt(s1, s2)", binaryOptOptRef("s1", "s2"));
    
    // A String(String=, String=) is a String(String, String=)
    String(String, String=) binaryOptOptRefOpt = b.binaryOptOpt;
    eq("binaryOptOpt(s1, d2)", binaryOptOptRefOpt("s1"));
    eq("binaryOptOpt(s1, s2)", binaryOptOptRefOpt("s1", "s2"));
    
    // A String(String=, String=) is a String(String, String)
    String(String, String) binaryOptOptRef2 = b.binaryOptOpt;
    eq("binaryOptOpt(s1, s2)", binaryOptOptRef2("s1", "s2"));
    
    // A String(String=, String=) is a String(String)
    String(String) binaryOptOptRef1 = b.binaryOptOpt;
    eq("binaryOptOpt(s1, d2)", binaryOptOptRef1("s1"));
    
    // A String(String=, String=) is a String()
    String() binaryOptOptRef0 = b.binaryOptOpt;
    eq("binaryOptOpt(d1, d2)", binaryOptOptRef0());
}

@noanno
void bug1155_unaryOptStar(Bug1155 b) {
    void eq(String expect, String got) {
        if (expect != got) {
            throw Exception("Expected ``expect`` but got ``got``.");
        }
    }
    
    // A String(String=, String*) is a String()
    String() unaryOptStarRef0 = b.unaryOptStar;
    eq("unaryOptStar(d1; {})", unaryOptStarRef0());
    
    
    // A String(String=, String*) is a String(String)
    String(String) unaryOptStarRef1 = b.unaryOptStar;
    eq("unaryOptStar(s1; {})", unaryOptStarRef1("s1"));
    // spread
    eq("unaryOptStar(s1; {})", unaryOptStarRef1(*["s1"]));
    
    // A String(String=, String*) is a String(String=)
    String(String=) unaryOptStarRef1Opt = b.unaryOptStar;
    eq("unaryOptStar(d1; {})", unaryOptStarRef1Opt());
    eq("unaryOptStar(s1; {})", unaryOptStarRef1Opt("s1"));
    // spread
    eq("unaryOptStar(d1; {})", unaryOptStarRef1Opt(*[]));
    eq("unaryOptStar(s1; {})", unaryOptStarRef1Opt(*["s1"]));
    
    // A String(String=, String*) is a String(String, String*)
    String(String, String*) unaryOptStarRef1Star = b.unaryOptStar;
    eq("unaryOptStar(s1; {})", unaryOptStarRef1Star("s1"));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef1Star("s1", "s2"));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef1Star("s1", "s2", "s3"));
    // spread
    eq("unaryOptStar(s1; {})", unaryOptStarRef1Star(*["s1"]));
    eq("unaryOptStar(s1; {})", unaryOptStarRef1Star("s1", *[]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef1Star(*["s1", "s2"]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef1Star("s1", *["s2"]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef1Star("s1", "s2", *[]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef1Star(*["s1", "s2", "s3"]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef1Star("s1", *["s2", "s3"]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef1Star("s1", "s2", *["s3"]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef1Star("s1", "s2", "s3", *[]));
    
    // A String(String=, String*) is a String(String=, String*)
    String(String=, String*) unaryOptStarRef1OptStar = b.unaryOptStar;
    eq("unaryOptStar(d1; {})", unaryOptStarRef1OptStar());
    eq("unaryOptStar(s1; {})", unaryOptStarRef1OptStar("s1"));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef1OptStar("s1", "s2"));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef1OptStar("s1", "s2", "s3"));
    // spread
    eq("unaryOptStar(d1; {})", unaryOptStarRef1OptStar(*[]));
    eq("unaryOptStar(s1; {})", unaryOptStarRef1OptStar(*["s1"]));
    eq("unaryOptStar(s1; {})", unaryOptStarRef1OptStar("s1", *[]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef1OptStar(*["s1", "s2"]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef1OptStar("s1", *["s2"]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef1OptStar("s1", "s2", *[]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef1OptStar(*["s1", "s2", "s3"]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef1OptStar("s1", *["s2", "s3"]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef1OptStar("s1", "s2", *["s3"]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef1OptStar("s1", "s2", "s3", *[]));
    
    // A String(String=, String*) is a String(String, String=)
    String(String, String=) unaryOptStarRef2Opt = b.unaryOptStar;
    eq("unaryOptStar(s1; {})", unaryOptStarRef2Opt("s1"));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2Opt("s1", "s2"));
    // spread
    eq("unaryOptStar(s1; {})", unaryOptStarRef2Opt(*["s1"]));
    eq("unaryOptStar(s1; {})", unaryOptStarRef2Opt("s1", *[]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2Opt(*["s1", "s2"]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2Opt("s1", *["s2"]));
    
    // A String(String=, String*) is a String(String, String)
    String(String, String) unaryOptStarRef2 = b.unaryOptStar;
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2("s1", "s2"));
    // spread
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2(*["s1", "s2"]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2("s1", *["s2"]));
    
    // A String(String=, String*) is a String(String=, String=)
    String(String=, String=) unaryOptStarRef2OptOpt = b.unaryOptStar;
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2OptOpt("s1", "s2"));
    //spread
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2OptOpt(*["s1", "s2"]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2OptOpt("s1", *["s2"]));
    
    // A String(String=, String*) is a String(String, String+)
    String(String, String+) unaryOptStarRef2OptPlus = b.unaryOptStar;
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2OptPlus("s1", "s2"));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef2OptPlus("s1", "s2", "s3"));
    //spread
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2OptPlus(*["s1", "s2"]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2OptPlus("s1", *["s2"]));
    eq("unaryOptStar(s1; [s2])", unaryOptStarRef2OptPlus("s1", "s2", *[]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef2OptPlus(*["s1", "s2", "s3"]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef2OptPlus("s1", *["s2", "s3"]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef2OptPlus("s1", "s2", *["s3"]));
    eq("unaryOptStar(s1; [s2, s3])", unaryOptStarRef2OptPlus("s1", "s2", "s3", *[]));
    
    
}

@noanno
void bug1155_star(Bug1155 b) {
    void eq(String expect, String got) {
        if (expect != got) {
            throw Exception("Expected ``expect`` but got ``got``.");
        }
    }
    // A String(String*) is a String()
    String() starRef0 = b.star;
    eq("star(; {})", starRef0());
    
    // A String(String*) is a String(String*)
    String(String*) starRef0Star = b.star;
    eq("star(; {})", starRef0Star());
    eq("star(; [s1])", starRef0Star("s1"));
    eq("star(; [s1, s2])", starRef0Star("s1", "s2"));
    // spread
    eq("star(; {})", starRef0Star(*[]));
    eq("star(; [s1])", starRef0Star(*["s1"]));
    eq("star(; [s1, s2])", starRef0Star(*["s1", "s2"]));
    
    // A String(String*) is a String(String+)
    String(String+) starRef0Plus = b.star;
    eq("star(; [s1])", starRef0Plus("s1"));
    eq("star(; [s1, s2])", starRef0Plus("s1", "s2"));
    // spread
    eq("star(; [s1])", starRef0Plus(*["s1"]));
    eq("star(; [s1, s2])", starRef0Plus(*["s1", "s2"]));
    
    // A String(String*) is a String(String)
    String(String) starRef1 = b.star;
    eq("star(; [s1])", starRef1("s1"));
    // spread
    eq("star(; [s1])", starRef1(*["s1"]));
    
    // A String(String*) is a String(String=)
    String(String=) starRef1Opt = b.star;
    eq("star(; {})", starRef1Opt());
    eq("star(; [s1])", starRef1Opt("s1"));
    // spread
    eq("star(; {})", starRef1Opt(*[]));
    eq("star(; [s1])", starRef1Opt(*["s1"]));
    
    // A String(String*) is a String(String, String*)
    String(String, String*) starRef1Star = b.star;
    eq("star(; [s1])", starRef1Star("s1"));
    eq("star(; [s1, s2])", starRef1Star("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef1Star("s1", "s2", "s3"));
    // spread
    eq("star(; [s1])", starRef1Star(*["s1"]));
    eq("star(; [s1, s2])", starRef1Star(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef1Star("s1", *["s2"]));
    eq("star(; [s1, s2, s3])", starRef1Star(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef1Star("s1", *["s2", "s3"]));
    
    // A String(String*) is a String(String=, String*)
    String(String=, String*) starRef1OptStar = b.star;
    eq("star(; [s1])", starRef1OptStar("s1"));
    eq("star(; {})", starRef1OptStar());
    eq("star(; [s1, s2])", starRef1OptStar("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef1OptStar("s1", "s2", "s3"));
    // spread
    eq("star(; [s1])", starRef1OptStar(*["s1"]));
    eq("star(; {})", starRef1OptStar(*[]));
    eq("star(; [s1, s2])", starRef1OptStar(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef1OptStar("s1", *["s2"]));
    eq("star(; [s1, s2, s3])", starRef1OptStar(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef1OptStar("s1", *["s2", "s3"]));
    
    // A String(String*) is a String(String, String+)
    String(String, String+) starRef1Plus = b.star;
    eq("star(; [s1, s2])", starRef1Plus("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef1Plus("s1", "s2", "s3"));
    // spread
    eq("star(; [s1, s2])", starRef1Plus(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef1Plus("s1", *["s2"]));
    eq("star(; [s1, s2, s3])", starRef1Plus(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef1Plus("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef1Plus("s1", "s2", *["s3"]));
    
    // A String(String*) is a String(String, String)
    String(String, String) starRef2 = b.star;
    eq("star(; [s1, s2])", starRef2("s1", "s2"));
    // spread
    eq("star(; [s1, s2])", starRef2(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef2("s1", *["s2"]));
    
    // A String(String*) is a String(String, String=)
    String(String, String=) starRef2Opt = b.star;
    eq("star(; [s1, s2])", starRef2Opt("s1", "s2"));
    eq("star(; [s1])", starRef2Opt("s1"));
    // spread
    eq("star(; [s1, s2])", starRef2Opt(*["s1", "s2"]));
    eq("star(; [s1])", starRef2Opt(*["s1"]));
    eq("star(; [s1, s2])", starRef2Opt("s1", *["s2"]));
    eq("star(; [s1])", starRef2Opt("s1", *[]));
    
    // A String(String*) is a String(String=, String=)
    String(String=, String=) starRef2OptOpt = b.star;
    eq("star(; [s1, s2])", starRef2OptOpt("s1", "s2"));
    eq("star(; [s1])", starRef2OptOpt("s1"));
    eq("star(; {})", starRef2OptOpt());
    // spread
    eq("star(; [s1, s2])", starRef2OptOpt(*["s1", "s2"]));
    eq("star(; [s1])", starRef2OptOpt(*["s1"]));
    eq("star(; {})", starRef2OptOpt(*[]));
    eq("star(; [s1, s2])", starRef2OptOpt("s1", *["s2"]));
    eq("star(; [s1])", starRef2OptOpt("s1", *[]));
    
    // A String(String*) is a String(String , String, String*) 
    String(String, String, String*) starRef2Star = b.star;
    eq("star(; [s1, s2])", starRef2Star("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef2Star("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef2Star("s1", "s2", "s3", "s4"));
    // spread
    eq("star(; [s1, s2])", starRef2Star(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef2Star("s1", *["s2"]));
    eq("star(; [s1, s2])", starRef2Star("s1", "s2", *[]));
    eq("star(; [s1, s2, s3])", starRef2Star("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3])", starRef2Star(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef2Star("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef2Star("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef2Star("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef2Star(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2Star("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2Star("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2Star("s1", "s2", "s3", *["s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2Star("s1", "s2", "s3", "s4", *[]));
    
    // A String(String*) is a String(String, String=, String*)
    String(String, String=, String*) starRef2OptStar = b.star;
    eq("star(; [s1])", starRef2OptStar("s1"));
    eq("star(; [s1, s2])", starRef2OptStar("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef2OptStar("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef2OptStar("s1", "s2", "s3", "s4"));
    // spread
    eq("star(; [s1])", starRef2OptStar(*["s1"]));
    eq("star(; [s1, s2])", starRef2OptStar(*["s1", "s2"]));
    eq("star(; [s1])", starRef2OptStar("s1", *[]));
    eq("star(; [s1, s2])", starRef2OptStar("s1", *["s2"]));
    eq("star(; [s1, s2])", starRef2OptStar("s1", "s2", *[]));
    eq("star(; [s1, s2, s3])", starRef2OptStar("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3])", starRef2OptStar(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef2OptStar("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef2OptStar("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef2OptStar("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef2OptStar(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2OptStar("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2OptStar("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2OptStar("s1", "s2", "s3", *["s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2OptStar("s1", "s2", "s3", "s4", *[]));
    
    // A String(String*) is a String(String=, String=, String*)
    String(String=, String=, String*) starRef2OptOptStar = b.star;
    eq("star(; {})", starRef2OptOptStar());
    eq("star(; [s1])", starRef2OptOptStar("s1"));
    eq("star(; [s1, s2])", starRef2OptOptStar("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef2OptOptStar("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef2OptOptStar("s1", "s2", "s3", "s4"));
    // spread
    eq("star(; {})", starRef2OptOptStar(*[]));
    eq("star(; [s1])", starRef2OptOptStar(*["s1"]));
    eq("star(; [s1, s2])", starRef2OptOptStar(*["s1", "s2"]));
    eq("star(; [s1])", starRef2OptOptStar("s1", *[]));
    eq("star(; [s1, s2])", starRef2OptOptStar("s1", *["s2"]));
    eq("star(; [s1, s2])", starRef2OptOptStar("s1", "s2", *[]));
    eq("star(; [s1, s2, s3])", starRef2OptOptStar("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3])", starRef2OptOptStar(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef2OptOptStar("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef2OptOptStar("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef2OptOptStar("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef2OptOptStar(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2OptOptStar("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2OptOptStar("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2OptOptStar("s1", "s2", "s3", *["s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2OptOptStar("s1", "s2", "s3", "s4", *[]));
    
    // A String(String*) is a String(String, String, String+)
    String(String, String, String+) starRef2Plus = b.star;
    eq("star(; [s1, s2, s3])", starRef2Plus("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef2Plus("s1", "s2", "s3", "s4"));
    //spread
    eq("star(; [s1, s2, s3])", starRef2Plus(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef2Plus("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef2Plus("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef2Plus("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef2Plus(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2Plus("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2Plus("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2Plus("s1", "s2", "s3", *["s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef2Plus("s1", "s2", "s3", "s4", *[]));

    // A String(String*) is a String(String, String, String)
    String(String, String, String) starRef3 = b.star;
    eq("star(; [s1, s2, s3])", starRef3("s1", "s2", "s3"));
    // spread
    eq("star(; [s1, s2, s3])", starRef3(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef3("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef3("s1", "s2", *["s3"]));
    
    // A String(String*) is a String(String, String, String=)
    String(String, String, String=) starRef3Opt = b.star;
    eq("star(; [s1, s2, s3])", starRef3Opt("s1", "s2", "s3"));
    eq("star(; [s1, s2])", starRef3Opt("s1", "s2"));
    // spread
    eq("star(; [s1, s2, s3])", starRef3Opt(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2])", starRef3Opt(*["s1", "s2"]));
    eq("star(; [s1, s2, s3])", starRef3Opt("s1", *["s2", "s3"]));
    eq("star(; [s1, s2])", starRef3Opt("s1", *["s2"]));
    eq("star(; [s1, s2, s3])", starRef3Opt("s1", "s2", *["s3"]));
    eq("star(; [s1, s2])", starRef3Opt("s1", "s2", *[]));
    
    // A String(String*) is a String(String, String=, String=)
    String(String, String=, String=) starRef3OptOpt = b.star;
    eq("star(; [s1, s2, s3])", starRef3OptOpt("s1", "s2", "s3"));
    eq("star(; [s1, s2])", starRef3OptOpt("s1", "s2"));
    eq("star(; [s1])", starRef3OptOpt("s1"));
    // spread
    eq("star(; [s1, s2, s3])", starRef3OptOpt(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2])", starRef3OptOpt(*["s1", "s2"]));
    eq("star(; [s1])", starRef3OptOpt(*["s1"]));
    eq("star(; [s1, s2, s3])", starRef3OptOpt("s1", *["s2", "s3"]));
    eq("star(; [s1, s2])", starRef3OptOpt("s1", *["s2"]));
    eq("star(; [s1])", starRef3OptOpt("s1", *[]));
    eq("star(; [s1, s2, s3])", starRef3OptOpt("s1", "s2", *["s3"]));
    eq("star(; [s1, s2])", starRef3OptOpt("s1", "s2", *[]));
    eq("star(; [s1])", starRef3OptOpt("s1", *[]));
    
    // A String(String*) is a String(String=, String=, String=)
    String(String=, String=, String=) starRef3OptOptOpt = b.star;
    eq("star(; [s1, s2, s3])", starRef3OptOptOpt("s1", "s2", "s3"));
    eq("star(; [s1, s2])", starRef3OptOptOpt("s1", "s2"));
    eq("star(; [s1])", starRef3OptOptOpt("s1"));
    eq("star(; {})", starRef3OptOptOpt());
    // spread
    eq("star(; [s1, s2, s3])", starRef3OptOptOpt(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2])", starRef3OptOptOpt(*["s1", "s2"]));
    eq("star(; [s1])", starRef3OptOptOpt(*["s1"]));
    eq("star(; {})", starRef3OptOptOpt(*[]));
    eq("star(; [s1, s2, s3])", starRef3OptOptOpt("s1", *["s2", "s3"]));
    eq("star(; [s1, s2])", starRef3OptOptOpt("s1", *["s2"]));
    eq("star(; [s1])", starRef3OptOptOpt("s1", *[]));
    eq("star(; [s1, s2, s3])", starRef3OptOptOpt("s1", "s2", *["s3"]));
    eq("star(; [s1, s2])", starRef3OptOptOpt("s1", "s2", *[]));
    eq("star(; [s1])", starRef3OptOptOpt("s1", *[]));
    
    // A String(String*) is a String(String, String, String, String*)
    String(String, String, String, String*) starRef3Star = b.star;
    eq("star(; [s1, s2, s3])", starRef3Star("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef3Star("s1", "s2", "s3", "s4"));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("star(; [s1, s2, s3])", starRef3Star(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef3Star("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef3Star("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef3Star("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef3Star(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3Star("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3Star("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3Star("s1", "s2", "s3", *["s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3Star("s1", "s2", "s3", "s4", *[]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3Star(*["s1", "s2", "s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3Star("s1", *["s2", "s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3Star("s1", "s2", *["s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", *["s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", *["s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String*) is a String(String, String, String=, String*)
    String(String, String, String=, String*) starRef3OptStar = b.star;
    eq("star(; [s1, s2])", starRef3OptStar("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef3OptStar("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef3OptStar("s1", "s2", "s3", "s4"));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("star(; [s1, s2])", starRef3OptStar(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef3OptStar("s1", *["s2"]));
    eq("star(; [s1, s2])", starRef3OptStar("s1", "s2", *[]));
    eq("star(; [s1, s2, s3])", starRef3OptStar(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef3OptStar("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef3OptStar("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef3OptStar("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptStar(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptStar("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptStar("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptStar("s1", "s2", "s3", *["s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptStar("s1", "s2", "s3", "s4", *[]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptStar(*["s1", "s2", "s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptStar("s1", *["s2", "s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptStar("s1", "s2", *["s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", *["s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", *["s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String*) is a String(String, String=, String=, String*)
    String(String, String=, String=, String*) starRef3OptOptStar = b.star;
    eq("star(; [s1])", starRef3OptOptStar("s1"));
    eq("star(; [s1, s2])", starRef3OptOptStar("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef3OptOptStar("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptStar("s1", "s2", "s3", "s4"));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptStar("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("star(; [s1])", starRef3OptOptStar(*["s1"]));
    eq("star(; [s1])", starRef3OptOptStar("s1", *[]));
    eq("star(; [s1, s2])", starRef3OptOptStar(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef3OptOptStar("s1", *["s2"]));
    eq("star(; [s1, s2])", starRef3OptOptStar("s1", "s2", *[]));
    eq("star(; [s1, s2, s3])", starRef3OptOptStar(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef3OptOptStar("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef3OptOptStar("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef3OptOptStar("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptStar(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptStar("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptStar("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptStar("s1", "s2", "s3", *["s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptStar("s1", "s2", "s3", "s4", *[]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptStar(*["s1", "s2", "s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptStar("s1", *["s2", "s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptStar("s1", "s2", *["s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptStar("s1", "s2", "s3", *["s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptStar("s1", "s2", "s3", "s4", *["s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptStar("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String*) is a String(String=, String=, String=, String*)
    String(String=, String=, String=, String*) starRef3OptOptOptStar = b.star;
    eq("star(; {})", starRef3OptOptOptStar());
    eq("star(; [s1])", starRef3OptOptOptStar("s1"));
    eq("star(; [s1, s2])", starRef3OptOptOptStar("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef3OptOptOptStar("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptOptStar("s1", "s2", "s3", "s4"));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptOptStar("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("star(; {})", starRef3OptOptOptStar(*[]));
    eq("star(; [s1])", starRef3OptOptOptStar(*["s1"]));
    eq("star(; [s1])", starRef3OptOptOptStar("s1", *[]));
    eq("star(; [s1, s2])", starRef3OptOptOptStar(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef3OptOptOptStar("s1", *["s2"]));
    eq("star(; [s1, s2])", starRef3OptOptOptStar("s1", "s2", *[]));
    eq("star(; [s1, s2, s3])", starRef3OptOptOptStar(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef3OptOptOptStar("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef3OptOptOptStar("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef3OptOptOptStar("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptOptStar(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptOptStar("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptOptStar("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptOptStar("s1", "s2", "s3", *["s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3OptOptOptStar("s1", "s2", "s3", "s4", *[]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptOptStar(*["s1", "s2", "s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptOptStar("s1", *["s2", "s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptOptStar("s1", "s2", *["s3", "s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptOptStar("s1", "s2", "s3", *["s4", "s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptOptStar("s1", "s2", "s3", "s4", *["s5"]));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3OptOptOptStar("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String*) is a String(String, String, String, String+)
    String(String, String, String, String+) starRef3Plus = b.star;
    eq("star(; [s1, s2, s3, s4])", starRef3Plus("s1", "s2", "s3", "s4"));
    eq("star(; [s1, s2, s3, s4, s5])", starRef3Plus("s1", "s2", "s3", "s4", "s5"));
    // spread
    eq("star(; [s1, s2, s3, s4])", starRef3Plus(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3Plus("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3Plus("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3Plus("s1", "s2", "s3", *["s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef3Plus("s1", "s2", "s3", "s4", *[]));
    
    // A String(String*) is a String(String, String, String, String)
    String(String, String, String, String) starRef4 = b.star;
    eq("star(; [s1, s2, s3, s4])", starRef4("s1", "s2", "s3", "s4"));
    // spread
    eq("star(; [s1, s2, s3, s4])", starRef4(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4("s1", "s2", "s3", *["s4"]));
    
    // A String(String*) is a String(String, String, String, String=)
    String(String, String, String, String=) starRef4Opt = b.star;
    eq("star(; [s1, s2, s3])", starRef4Opt("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef4Opt("s1", "s2", "s3", "s4"));
    // spread
    eq("star(; [s1, s2, s3])", starRef4Opt(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef4Opt("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef4Opt("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef4Opt("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef4Opt(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4Opt("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4Opt("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4Opt("s1", "s2", "s3", *["s4"]));
    
    // A String(String*) is a String(String, String, String=, String=)
    String(String, String, String=, String=) starRef4OptOpt = b.star;
    eq("star(; [s1, s2])", starRef4OptOpt("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef4OptOpt("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOpt("s1", "s2", "s3", "s4"));
    // spread
    eq("star(; [s1, s2])", starRef4OptOpt(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef4OptOpt("s1", *["s2"]));
    eq("star(; [s1, s2])", starRef4OptOpt("s1", "s2", *[]));
    eq("star(; [s1, s2, s3])", starRef4OptOpt(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef4OptOpt("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef4OptOpt("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef4OptOpt("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOpt(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOpt("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOpt("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOpt("s1", "s2", "s3", *["s4"]));
    
    // A String(String*) is a String(String, String=, String=, String=)
    String(String, String=, String=, String=) starRef4OptOptOpt = b.star;
    eq("star(; [s1])", starRef4OptOptOpt("s1"));
    eq("star(; [s1, s2])", starRef4OptOptOpt("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef4OptOptOpt("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOptOpt("s1", "s2", "s3", "s4"));
    // spread
    eq("star(; [s1])", starRef4OptOptOpt(*["s1"]));
    eq("star(; [s1])", starRef4OptOptOpt("s1", *[]));
    eq("star(; [s1, s2])", starRef4OptOptOpt(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef4OptOptOpt("s1", *["s2"]));
    eq("star(; [s1, s2])", starRef4OptOptOpt("s1", "s2", *[]));
    eq("star(; [s1, s2, s3])", starRef4OptOptOpt(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef4OptOptOpt("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef4OptOptOpt("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef4OptOptOpt("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOptOpt(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOptOpt("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOptOpt("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOptOpt("s1", "s2", "s3", *["s4"]));
    
    // A String(String*) is a String(String=, String=, String=, String=)
    String(String=, String=, String=, String=) starRef4OptOptOptOpt = b.star;
    eq("star(; {})", starRef4OptOptOptOpt());
    eq("star(; [s1])", starRef4OptOptOptOpt("s1"));
    eq("star(; [s1, s2])", starRef4OptOptOptOpt("s1", "s2"));
    eq("star(; [s1, s2, s3])", starRef4OptOptOptOpt("s1", "s2", "s3"));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOptOptOpt("s1", "s2", "s3", "s4"));
    // spread
    eq("star(; {})", starRef4OptOptOptOpt(*[]));
    eq("star(; [s1])", starRef4OptOptOptOpt(*["s1"]));
    eq("star(; [s1])", starRef4OptOptOptOpt("s1", *[]));
    eq("star(; [s1, s2])", starRef4OptOptOptOpt(*["s1", "s2"]));
    eq("star(; [s1, s2])", starRef4OptOptOptOpt("s1", *["s2"]));
    eq("star(; [s1, s2])", starRef4OptOptOptOpt("s1", "s2", *[]));
    eq("star(; [s1, s2, s3])", starRef4OptOptOptOpt(*["s1", "s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef4OptOptOptOpt("s1", *["s2", "s3"]));
    eq("star(; [s1, s2, s3])", starRef4OptOptOptOpt("s1", "s2", *["s3"]));
    eq("star(; [s1, s2, s3])", starRef4OptOptOptOpt("s1", "s2", "s3", *[]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOptOptOpt(*["s1", "s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOptOptOpt("s1", *["s2", "s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOptOptOpt("s1", "s2", *["s3", "s4"]));
    eq("star(; [s1, s2, s3, s4])", starRef4OptOptOptOpt("s1", "s2", "s3", *["s4"]));
    
}

@noanno
void bug1155_plus(Bug1155 b) {
    void eq(String expect, String got) {
        if (expect != got) {
            throw Exception("Expected ``expect`` but got ``got``.");
        }
    }
    
    // A String(String+) is a String(String+)
    String(String+) plusRef0Plus = b.plus;
    eq("plus(; [s1])", plusRef0Plus("s1"));
    eq("plus(; [s1, s2])", plusRef0Plus("s1", "s2"));
    // spread
    eq("plus(; [s1])", plusRef0Plus(*["s1"]));
    eq("plus(; [s1, s2])", plusRef0Plus(*["s1", "s2"]));
    
    // A String(String+) is a String(String)
    String(String) plusRef1 = b.plus;
    eq("plus(; [s1])", plusRef1("s1"));
    // spread
    eq("plus(; [s1])", plusRef1(*["s1"]));
    
    // A String(String+) is a String(String, String*)
    String(String, String*) plusRef1Star = b.plus;
    eq("plus(; [s1])", plusRef1Star("s1"));
    eq("plus(; [s1, s2])", plusRef1Star("s1", "s2"));
    eq("plus(; [s1, s2, s3])", plusRef1Star("s1", "s2", "s3"));
    // spread
    eq("plus(; [s1])", plusRef1Star(*["s1"]));
    eq("plus(; [s1, s2])", plusRef1Star(*["s1", "s2"]));
    eq("plus(; [s1, s2])", plusRef1Star("s1", *["s2"]));
    eq("plus(; [s1, s2, s3])", plusRef1Star(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef1Star("s1", *["s2", "s3"]));
    
    // A String(String+) is a String(String, String+)
    String(String, String+) plusRef1Plus = b.plus;
    eq("plus(; [s1, s2])", plusRef1Plus("s1", "s2"));
    eq("plus(; [s1, s2, s3])", plusRef1Plus("s1", "s2", "s3"));
    // spread
    eq("plus(; [s1, s2])", plusRef1Plus(*["s1", "s2"]));
    eq("plus(; [s1, s2])", plusRef1Plus("s1", *["s2"]));
    eq("plus(; [s1, s2, s3])", plusRef1Plus(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef1Plus("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef1Plus("s1", "s2", *["s3"]));
    
    
    // A String(String+) is a String(String, String=)
    String(String, String=) plusRef2Opt = b.plus;
    eq("plus(; [s1, s2])", plusRef2Opt("s1", "s2"));
    eq("plus(; [s1])", plusRef2Opt("s1"));
    // spread
    eq("plus(; [s1, s2])", plusRef2Opt(*["s1", "s2"]));
    eq("plus(; [s1])", plusRef2Opt(*["s1"]));
    eq("plus(; [s1, s2])", plusRef2Opt("s1", *["s2"]));
    eq("plus(; [s1])", plusRef2Opt("s1", *[]));
    
    // A String(String+) is a String(String , String, String*) 
    String(String, String, String*) plusRef2Star = b.plus;
    eq("plus(; [s1, s2])", plusRef2Star("s1", "s2"));
    eq("plus(; [s1, s2, s3])", plusRef2Star("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Star("s1", "s2", "s3", "s4"));
    // spread
    eq("plus(; [s1, s2])", plusRef2Star(*["s1", "s2"]));
    eq("plus(; [s1, s2])", plusRef2Star("s1", *["s2"]));
    eq("plus(; [s1, s2])", plusRef2Star("s1", "s2", *[]));
    eq("plus(; [s1, s2, s3])", plusRef2Star("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3])", plusRef2Star(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef2Star("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef2Star("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2, s3])", plusRef2Star("s1", "s2", "s3", *[]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Star(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Star("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Star("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Star("s1", "s2", "s3", *["s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Star("s1", "s2", "s3", "s4", *[]));
    
    // A String(String+) is a String(String, String=, String*)
    String(String, String=, String*) plusRef2OptStar = b.plus;
    eq("plus(; [s1])", plusRef2OptStar("s1"));
    eq("plus(; [s1, s2])", plusRef2OptStar("s1", "s2"));
    eq("plus(; [s1, s2, s3])", plusRef2OptStar("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3, s4])", plusRef2OptStar("s1", "s2", "s3", "s4"));
    // spread
    eq("plus(; [s1])", plusRef2OptStar(*["s1"]));
    eq("plus(; [s1, s2])", plusRef2OptStar(*["s1", "s2"]));
    eq("plus(; [s1])", plusRef2OptStar("s1", *[]));
    eq("plus(; [s1, s2])", plusRef2OptStar("s1", *["s2"]));
    eq("plus(; [s1, s2])", plusRef2OptStar("s1", "s2", *[]));
    eq("plus(; [s1, s2, s3])", plusRef2OptStar("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3])", plusRef2OptStar(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef2OptStar("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef2OptStar("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2, s3])", plusRef2OptStar("s1", "s2", "s3", *[]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2OptStar(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2OptStar("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2OptStar("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2OptStar("s1", "s2", "s3", *["s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2OptStar("s1", "s2", "s3", "s4", *[]));
    
    // A String(String+) is a String(String, String, String+)
    String(String, String, String+) plusRef2Plus = b.plus;
    eq("plus(; [s1, s2, s3])", plusRef2Plus("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Plus("s1", "s2", "s3", "s4"));
    //spread
    eq("plus(; [s1, s2, s3])", plusRef2Plus(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef2Plus("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef2Plus("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2, s3])", plusRef2Plus("s1", "s2", "s3", *[]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Plus(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Plus("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Plus("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Plus("s1", "s2", "s3", *["s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef2Plus("s1", "s2", "s3", "s4", *[]));
    
    // A String(String+) is a String(String, String, String)
    String(String, String, String) plusRef3 = b.plus;
    eq("plus(; [s1, s2, s3])", plusRef3("s1", "s2", "s3"));
    // spread
    eq("plus(; [s1, s2, s3])", plusRef3(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3("s1", "s2", *["s3"]));
    
    // A String(String+) is a String(String, String, String=)
    String(String, String, String=) plusRef3Opt = b.plus;
    eq("plus(; [s1, s2, s3])", plusRef3Opt("s1", "s2", "s3"));
    eq("plus(; [s1, s2])", plusRef3Opt("s1", "s2"));
    // spread
    eq("plus(; [s1, s2, s3])", plusRef3Opt(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2])", plusRef3Opt(*["s1", "s2"]));
    eq("plus(; [s1, s2, s3])", plusRef3Opt("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2])", plusRef3Opt("s1", *["s2"]));
    eq("plus(; [s1, s2, s3])", plusRef3Opt("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2])", plusRef3Opt("s1", "s2", *[]));
    
    // A String(String+) is a String(String, String=, String=)
    String(String, String=, String=) plusRef3OptOpt = b.plus;
    eq("plus(; [s1, s2, s3])", plusRef3OptOpt("s1", "s2", "s3"));
    eq("plus(; [s1, s2])", plusRef3OptOpt("s1", "s2"));
    eq("plus(; [s1])", plusRef3OptOpt("s1"));
    // spread
    eq("plus(; [s1, s2, s3])", plusRef3OptOpt(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2])", plusRef3OptOpt(*["s1", "s2"]));
    eq("plus(; [s1])", plusRef3OptOpt(*["s1"]));
    eq("plus(; [s1, s2, s3])", plusRef3OptOpt("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2])", plusRef3OptOpt("s1", *["s2"]));
    eq("plus(; [s1])", plusRef3OptOpt("s1", *[]));
    eq("plus(; [s1, s2, s3])", plusRef3OptOpt("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2])", plusRef3OptOpt("s1", "s2", *[]));
    eq("plus(; [s1])", plusRef3OptOpt("s1", *[]));
    
    
    // A String(String+) is a String(String, String, String, String*)
    String(String, String, String, String*) plusRef3Star = b.plus;
    eq("plus(; [s1, s2, s3])", plusRef3Star("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3, s4])", plusRef3Star("s1", "s2", "s3", "s4"));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3Star("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("plus(; [s1, s2, s3])", plusRef3Star(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3Star("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3Star("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3Star("s1", "s2", "s3", *[]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3Star(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3Star("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3Star("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3Star("s1", "s2", "s3", *["s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3Star("s1", "s2", "s3", "s4", *[]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3Star(*["s1", "s2", "s3", "s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3Star("s1", *["s2", "s3", "s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3Star("s1", "s2", *["s3", "s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3Star("s1", "s2", "s3", *["s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3Star("s1", "s2", "s3", "s4", *["s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3Star("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String+) is a String(String, String, String=, String*)
    String(String, String, String=, String*) plusRef3OptStar = b.plus;
    eq("plus(; [s1, s2])", plusRef3OptStar("s1", "s2"));
    eq("plus(; [s1, s2, s3])", plusRef3OptStar("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptStar("s1", "s2", "s3", "s4"));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptStar("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("plus(; [s1, s2])", plusRef3OptStar(*["s1", "s2"]));
    eq("plus(; [s1, s2])", plusRef3OptStar("s1", *["s2"]));
    eq("plus(; [s1, s2])", plusRef3OptStar("s1", "s2", *[]));
    eq("plus(; [s1, s2, s3])", plusRef3OptStar(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3OptStar("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3OptStar("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3OptStar("s1", "s2", "s3", *[]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptStar(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptStar("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptStar("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptStar("s1", "s2", "s3", *["s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptStar("s1", "s2", "s3", "s4", *[]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptStar(*["s1", "s2", "s3", "s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptStar("s1", *["s2", "s3", "s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptStar("s1", "s2", *["s3", "s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptStar("s1", "s2", "s3", *["s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptStar("s1", "s2", "s3", "s4", *["s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptStar("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String+) is a String(String, String=, String=, String*)
    String(String, String=, String=, String*) plusRef3OptOptStar = b.plus;
    eq("plus(; [s1])", plusRef3OptOptStar("s1"));
    eq("plus(; [s1, s2])", plusRef3OptOptStar("s1", "s2"));
    eq("plus(; [s1, s2, s3])", plusRef3OptOptStar("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptOptStar("s1", "s2", "s3", "s4"));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptOptStar("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("plus(; [s1])", plusRef3OptOptStar(*["s1"]));
    eq("plus(; [s1])", plusRef3OptOptStar("s1", *[]));
    eq("plus(; [s1, s2])", plusRef3OptOptStar(*["s1", "s2"]));
    eq("plus(; [s1, s2])", plusRef3OptOptStar("s1", *["s2"]));
    eq("plus(; [s1, s2])", plusRef3OptOptStar("s1", "s2", *[]));
    eq("plus(; [s1, s2, s3])", plusRef3OptOptStar(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3OptOptStar("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3OptOptStar("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2, s3])", plusRef3OptOptStar("s1", "s2", "s3", *[]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptOptStar(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptOptStar("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptOptStar("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptOptStar("s1", "s2", "s3", *["s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3OptOptStar("s1", "s2", "s3", "s4", *[]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptOptStar(*["s1", "s2", "s3", "s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptOptStar("s1", *["s2", "s3", "s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptOptStar("s1", "s2", *["s3", "s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptOptStar("s1", "s2", "s3", *["s4", "s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptOptStar("s1", "s2", "s3", "s4", *["s5"]));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3OptOptStar("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String+) is a String(String, String, String, String+)
    String(String, String, String, String+) plusRef3Plus = b.plus;
    eq("plus(; [s1, s2, s3, s4])", plusRef3Plus("s1", "s2", "s3", "s4"));
    eq("plus(; [s1, s2, s3, s4, s5])", plusRef3Plus("s1", "s2", "s3", "s4", "s5"));
    // spread
    eq("plus(; [s1, s2, s3, s4])", plusRef3Plus(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3Plus("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3Plus("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3Plus("s1", "s2", "s3", *["s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef3Plus("s1", "s2", "s3", "s4", *[]));
    
    // A String(String+) is a String(String, String, String, String)
    String(String, String, String, String) plusRef4 = b.plus;
    eq("plus(; [s1, s2, s3, s4])", plusRef4("s1", "s2", "s3", "s4"));
    // spread
    eq("plus(; [s1, s2, s3, s4])", plusRef4(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4("s1", "s2", "s3", *["s4"]));
    
    // A String(String+) is a String(String, String, String, String=)
    String(String, String, String, String=) plusRef4Opt = b.plus;
    eq("plus(; [s1, s2, s3])", plusRef4Opt("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3, s4])", plusRef4Opt("s1", "s2", "s3", "s4"));
    // spread
    eq("plus(; [s1, s2, s3])", plusRef4Opt(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef4Opt("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef4Opt("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2, s3])", plusRef4Opt("s1", "s2", "s3", *[]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4Opt(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4Opt("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4Opt("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4Opt("s1", "s2", "s3", *["s4"]));
    
    // A String(String+) is a String(String, String, String=, String=)
    String(String, String, String=, String=) plusRef4OptOpt = b.plus;
    eq("plus(; [s1, s2])", plusRef4OptOpt("s1", "s2"));
    eq("plus(; [s1, s2, s3])", plusRef4OptOpt("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3, s4])", plusRef4OptOpt("s1", "s2", "s3", "s4"));
    // spread
    eq("plus(; [s1, s2])", plusRef4OptOpt(*["s1", "s2"]));
    eq("plus(; [s1, s2])", plusRef4OptOpt("s1", *["s2"]));
    eq("plus(; [s1, s2])", plusRef4OptOpt("s1", "s2", *[]));
    eq("plus(; [s1, s2, s3])", plusRef4OptOpt(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef4OptOpt("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef4OptOpt("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2, s3])", plusRef4OptOpt("s1", "s2", "s3", *[]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4OptOpt(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4OptOpt("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4OptOpt("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4OptOpt("s1", "s2", "s3", *["s4"]));
    
    // A String(String+) is a String(String, String=, String=, String=)
    String(String, String=, String=, String=) plusRef4OptOptOpt = b.plus;
    eq("plus(; [s1])", plusRef4OptOptOpt("s1"));
    eq("plus(; [s1, s2])", plusRef4OptOptOpt("s1", "s2"));
    eq("plus(; [s1, s2, s3])", plusRef4OptOptOpt("s1", "s2", "s3"));
    eq("plus(; [s1, s2, s3, s4])", plusRef4OptOptOpt("s1", "s2", "s3", "s4"));
    // spread
    eq("plus(; [s1])", plusRef4OptOptOpt(*["s1"]));
    eq("plus(; [s1])", plusRef4OptOptOpt("s1", *[]));
    eq("plus(; [s1, s2])", plusRef4OptOptOpt(*["s1", "s2"]));
    eq("plus(; [s1, s2])", plusRef4OptOptOpt("s1", *["s2"]));
    eq("plus(; [s1, s2])", plusRef4OptOptOpt("s1", "s2", *[]));
    eq("plus(; [s1, s2, s3])", plusRef4OptOptOpt(*["s1", "s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef4OptOptOpt("s1", *["s2", "s3"]));
    eq("plus(; [s1, s2, s3])", plusRef4OptOptOpt("s1", "s2", *["s3"]));
    eq("plus(; [s1, s2, s3])", plusRef4OptOptOpt("s1", "s2", "s3", *[]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4OptOptOpt(*["s1", "s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4OptOptOpt("s1", *["s2", "s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4OptOptOpt("s1", "s2", *["s3", "s4"]));
    eq("plus(; [s1, s2, s3, s4])", plusRef4OptOptOpt("s1", "s2", "s3", *["s4"]));
    
}


@noanno
void bug1155_unaryStar(Bug1155 b) {
    void eq(String expect, String got) {
        if (expect != got) {
            throw Exception("Expected ``expect`` but got ``got``.");
        }
    }
    
    // A String(String, String*) is a String(String+)
    /* TODO This should be allowed! #765
    String(String+) starRef0Plus = b.unaryStar;
    eq("unaryStar(; [s1])", starRef0Plus("s1"));
    eq("unaryStar(; [s1, s2])", starRef0Plus("s1", "s2"));
    // spread
    eq("unaryStar(; [s1])", starRef0Plus(*["s1"]));
    eq("unaryStar(; [s1, s2])", starRef0Plus(*["s1", "s2"]));
    */
    // A String(String, String*) is a String(String)
    String(String) starRef1 = b.unaryStar;
    eq("unaryStar(s1; {})", starRef1("s1"));
    // spread
    eq("unaryStar(s1; {})", starRef1(*["s1"]));
    
    // A String(String, String*) is a String(String, String*)
    String(String, String*) starRef1Star = b.unaryStar;
    eq("unaryStar(s1; {})", starRef1Star("s1"));
    eq("unaryStar(s1; [s2])", starRef1Star("s1", "s2"));
    eq("unaryStar(s1; [s2, s3])", starRef1Star("s1", "s2", "s3"));
    // spread
    eq("unaryStar(s1; {})", starRef1Star(*["s1"]));
    eq("unaryStar(s1; [s2])", starRef1Star(*["s1", "s2"]));
    eq("unaryStar(s1; [s2])", starRef1Star("s1", *["s2"]));
    eq("unaryStar(s1; [s2, s3])", starRef1Star(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef1Star("s1", *["s2", "s3"]));
    
    // A String(String, String*) is a String(String, String+)
    String(String, String+) starRef1Plus = b.unaryStar;
    eq("unaryStar(s1; [s2])", starRef1Plus("s1", "s2"));
    eq("unaryStar(s1; [s2, s3])", starRef1Plus("s1", "s2", "s3"));
    // spread
    eq("unaryStar(s1; [s2])", starRef1Plus(*["s1", "s2"]));
    eq("unaryStar(s1; [s2])", starRef1Plus("s1", *["s2"]));
    eq("unaryStar(s1; [s2, s3])", starRef1Plus(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef1Plus("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef1Plus("s1", "s2", *["s3"]));
    
    // A String(String, String*) is a String(String, String)
    String(String, String) starRef2 = b.unaryStar;
    eq("unaryStar(s1; [s2])", starRef2("s1", "s2"));
    // spread
    eq("unaryStar(s1; [s2])", starRef2(*["s1", "s2"]));
    eq("unaryStar(s1; [s2])", starRef2("s1", *["s2"]));
    
    // A String(String, String*) is a String(String, String=)
    String(String, String=) starRef2Opt = b.unaryStar;
    eq("unaryStar(s1; [s2])", starRef2Opt("s1", "s2"));
    eq("unaryStar(s1; {})", starRef2Opt("s1"));
    // spread
    eq("unaryStar(s1; [s2])", starRef2Opt(*["s1", "s2"]));
    eq("unaryStar(s1; {})", starRef2Opt(*["s1"]));
    eq("unaryStar(s1; [s2])", starRef2Opt("s1", *["s2"]));
    eq("unaryStar(s1; {})", starRef2Opt("s1", *[]));
    
    // A String(String, String*) is a String(String , String, String*) 
    String(String, String, String*) starRef2Star = b.unaryStar;
    eq("unaryStar(s1; [s2])", starRef2Star("s1", "s2"));
    eq("unaryStar(s1; [s2, s3])", starRef2Star("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Star("s1", "s2", "s3", "s4"));
    // spread
    eq("unaryStar(s1; [s2])", starRef2Star(*["s1", "s2"]));
    eq("unaryStar(s1; [s2])", starRef2Star("s1", *["s2"]));
    eq("unaryStar(s1; [s2])", starRef2Star("s1", "s2", *[]));
    eq("unaryStar(s1; [s2, s3])", starRef2Star("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3])", starRef2Star(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef2Star("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef2Star("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef2Star("s1", "s2", "s3", *[]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Star(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Star("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Star("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Star("s1", "s2", "s3", *["s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Star("s1", "s2", "s3", "s4", *[]));
    
    // A String(String, String*) is a String(String, String=, String*)
    String(String, String=, String*) starRef2OptStar = b.unaryStar;
    eq("unaryStar(s1; {})", starRef2OptStar("s1"));
    eq("unaryStar(s1; [s2])", starRef2OptStar("s1", "s2"));
    eq("unaryStar(s1; [s2, s3])", starRef2OptStar("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2OptStar("s1", "s2", "s3", "s4"));
    // spread
    eq("unaryStar(s1; {})", starRef2OptStar(*["s1"]));
    eq("unaryStar(s1; [s2])", starRef2OptStar(*["s1", "s2"]));
    eq("unaryStar(s1; {})", starRef2OptStar("s1", *[]));
    eq("unaryStar(s1; [s2])", starRef2OptStar("s1", *["s2"]));
    eq("unaryStar(s1; [s2])", starRef2OptStar("s1", "s2", *[]));
    eq("unaryStar(s1; [s2, s3])", starRef2OptStar("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3])", starRef2OptStar(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef2OptStar("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef2OptStar("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef2OptStar("s1", "s2", "s3", *[]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2OptStar(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2OptStar("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2OptStar("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2OptStar("s1", "s2", "s3", *["s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2OptStar("s1", "s2", "s3", "s4", *[]));
    
    // A String(String, String*) is a String(String, String, String+)
    String(String, String, String+) starRef2Plus = b.unaryStar;
    eq("unaryStar(s1; [s2, s3])", starRef2Plus("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Plus("s1", "s2", "s3", "s4"));
    //spread
    eq("unaryStar(s1; [s2, s3])", starRef2Plus(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef2Plus("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef2Plus("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef2Plus("s1", "s2", "s3", *[]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Plus(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Plus("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Plus("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Plus("s1", "s2", "s3", *["s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef2Plus("s1", "s2", "s3", "s4", *[]));

    // A String(String, String*) is a String(String, String, String)
    String(String, String, String) starRef3 = b.unaryStar;
    eq("unaryStar(s1; [s2, s3])", starRef3("s1", "s2", "s3"));
    // spread
    eq("unaryStar(s1; [s2, s3])", starRef3(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3("s1", "s2", *["s3"]));
    
    // A String(String, String*) is a String(String, String, String=)
    String(String, String, String=) starRef3Opt = b.unaryStar;
    eq("unaryStar(s1; [s2, s3])", starRef3Opt("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2])", starRef3Opt("s1", "s2"));
    // spread
    eq("unaryStar(s1; [s2, s3])", starRef3Opt(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2])", starRef3Opt(*["s1", "s2"]));
    eq("unaryStar(s1; [s2, s3])", starRef3Opt("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2])", starRef3Opt("s1", *["s2"]));
    eq("unaryStar(s1; [s2, s3])", starRef3Opt("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2])", starRef3Opt("s1", "s2", *[]));
    
    // A String(String, String*) is a String(String, String=, String=)
    String(String, String=, String=) starRef3OptOpt = b.unaryStar;
    eq("unaryStar(s1; [s2, s3])", starRef3OptOpt("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2])", starRef3OptOpt("s1", "s2"));
    eq("unaryStar(s1; {})", starRef3OptOpt("s1"));
    // spread
    eq("unaryStar(s1; [s2, s3])", starRef3OptOpt(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2])", starRef3OptOpt(*["s1", "s2"]));
    eq("unaryStar(s1; {})", starRef3OptOpt(*["s1"]));
    eq("unaryStar(s1; [s2, s3])", starRef3OptOpt("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2])", starRef3OptOpt("s1", *["s2"]));
    eq("unaryStar(s1; {})", starRef3OptOpt("s1", *[]));
    eq("unaryStar(s1; [s2, s3])", starRef3OptOpt("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2])", starRef3OptOpt("s1", "s2", *[]));
    eq("unaryStar(s1; {})", starRef3OptOpt("s1", *[]));
    
    // A String(String, String*) is a String(String, String, String, String*)
    String(String, String, String, String*) starRef3Star = b.unaryStar;
    eq("unaryStar(s1; [s2, s3])", starRef3Star("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Star("s1", "s2", "s3", "s4"));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("unaryStar(s1; [s2, s3])", starRef3Star(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3Star("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3Star("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3Star("s1", "s2", "s3", *[]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Star(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Star("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Star("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Star("s1", "s2", "s3", *["s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Star("s1", "s2", "s3", "s4", *[]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3Star(*["s1", "s2", "s3", "s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3Star("s1", *["s2", "s3", "s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3Star("s1", "s2", *["s3", "s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", *["s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", *["s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String, String*) is a String(String, String, String=, String*)
    String(String, String, String=, String*) starRef3OptStar = b.unaryStar;
    eq("unaryStar(s1; [s2])", starRef3OptStar("s1", "s2"));
    eq("unaryStar(s1; [s2, s3])", starRef3OptStar("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptStar("s1", "s2", "s3", "s4"));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("unaryStar(s1; [s2])", starRef3OptStar(*["s1", "s2"]));
    eq("unaryStar(s1; [s2])", starRef3OptStar("s1", *["s2"]));
    eq("unaryStar(s1; [s2])", starRef3OptStar("s1", "s2", *[]));
    eq("unaryStar(s1; [s2, s3])", starRef3OptStar(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3OptStar("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3OptStar("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3OptStar("s1", "s2", "s3", *[]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptStar(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptStar("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptStar("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptStar("s1", "s2", "s3", *["s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptStar("s1", "s2", "s3", "s4", *[]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptStar(*["s1", "s2", "s3", "s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", *["s2", "s3", "s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", "s2", *["s3", "s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", *["s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", *["s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String, String*) is a String(String, String=, String=, String*)
    String(String, String=, String=, String*) starRef3OptOptStar = b.unaryStar;
    eq("unaryStar(s1; {})", starRef3OptOptStar("s1"));
    eq("unaryStar(s1; [s2])", starRef3OptOptStar("s1", "s2"));
    eq("unaryStar(s1; [s2, s3])", starRef3OptOptStar("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptOptStar("s1", "s2", "s3", "s4"));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptOptStar("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("unaryStar(s1; {})", starRef3OptOptStar(*["s1"]));
    eq("unaryStar(s1; {})", starRef3OptOptStar("s1", *[]));
    eq("unaryStar(s1; [s2])", starRef3OptOptStar(*["s1", "s2"]));
    eq("unaryStar(s1; [s2])", starRef3OptOptStar("s1", *["s2"]));
    eq("unaryStar(s1; [s2])", starRef3OptOptStar("s1", "s2", *[]));
    eq("unaryStar(s1; [s2, s3])", starRef3OptOptStar(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3OptOptStar("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3OptOptStar("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef3OptOptStar("s1", "s2", "s3", *[]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptOptStar(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptOptStar("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptOptStar("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptOptStar("s1", "s2", "s3", *["s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3OptOptStar("s1", "s2", "s3", "s4", *[]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptOptStar(*["s1", "s2", "s3", "s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptOptStar("s1", *["s2", "s3", "s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptOptStar("s1", "s2", *["s3", "s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptOptStar("s1", "s2", "s3", *["s4", "s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptOptStar("s1", "s2", "s3", "s4", *["s5"]));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3OptOptStar("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String, String*) is a String(String, String, String, String+)
    String(String, String, String, String+) starRef3Plus = b.unaryStar;
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Plus("s1", "s2", "s3", "s4"));
    eq("unaryStar(s1; [s2, s3, s4, s5])", starRef3Plus("s1", "s2", "s3", "s4", "s5"));
    // spread
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Plus(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Plus("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Plus("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Plus("s1", "s2", "s3", *["s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef3Plus("s1", "s2", "s3", "s4", *[]));
    
    // A String(String, String*) is a String(String, String, String, String)
    String(String, String, String, String) starRef4 = b.unaryStar;
    eq("unaryStar(s1; [s2, s3, s4])", starRef4("s1", "s2", "s3", "s4"));
    // spread
    eq("unaryStar(s1; [s2, s3, s4])", starRef4(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4("s1", "s2", "s3", *["s4"]));
    
    // A String(String, String*) is a String(String, String, String, String=)
    String(String, String, String, String=) starRef4Opt = b.unaryStar;
    eq("unaryStar(s1; [s2, s3])", starRef4Opt("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4Opt("s1", "s2", "s3", "s4"));
    // spread
    eq("unaryStar(s1; [s2, s3])", starRef4Opt(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef4Opt("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef4Opt("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef4Opt("s1", "s2", "s3", *[]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4Opt(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4Opt("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4Opt("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4Opt("s1", "s2", "s3", *["s4"]));
    
    // A String(String, String*) is a String(String, String, String=, String=)
    String(String, String, String=, String=) starRef4OptOpt = b.unaryStar;
    eq("unaryStar(s1; [s2])", starRef4OptOpt("s1", "s2"));
    eq("unaryStar(s1; [s2, s3])", starRef4OptOpt("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4OptOpt("s1", "s2", "s3", "s4"));
    // spread
    eq("unaryStar(s1; [s2])", starRef4OptOpt(*["s1", "s2"]));
    eq("unaryStar(s1; [s2])", starRef4OptOpt("s1", *["s2"]));
    eq("unaryStar(s1; [s2])", starRef4OptOpt("s1", "s2", *[]));
    eq("unaryStar(s1; [s2, s3])", starRef4OptOpt(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef4OptOpt("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef4OptOpt("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef4OptOpt("s1", "s2", "s3", *[]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4OptOpt(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4OptOpt("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4OptOpt("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4OptOpt("s1", "s2", "s3", *["s4"]));
    
    // A String(String, String*) is a String(String, String=, String=, String=)
    String(String, String=, String=, String=) starRef4OptOptOpt = b.unaryStar;
    eq("unaryStar(s1; {})", starRef4OptOptOpt("s1"));
    eq("unaryStar(s1; [s2])", starRef4OptOptOpt("s1", "s2"));
    eq("unaryStar(s1; [s2, s3])", starRef4OptOptOpt("s1", "s2", "s3"));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4OptOptOpt("s1", "s2", "s3", "s4"));
    // spread
    eq("unaryStar(s1; {})", starRef4OptOptOpt(*["s1"]));
    eq("unaryStar(s1; {})", starRef4OptOptOpt("s1", *[]));
    eq("unaryStar(s1; [s2])", starRef4OptOptOpt(*["s1", "s2"]));
    eq("unaryStar(s1; [s2])", starRef4OptOptOpt("s1", *["s2"]));
    eq("unaryStar(s1; [s2])", starRef4OptOptOpt("s1", "s2", *[]));
    eq("unaryStar(s1; [s2, s3])", starRef4OptOptOpt(*["s1", "s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef4OptOptOpt("s1", *["s2", "s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef4OptOptOpt("s1", "s2", *["s3"]));
    eq("unaryStar(s1; [s2, s3])", starRef4OptOptOpt("s1", "s2", "s3", *[]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4OptOptOpt(*["s1", "s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4OptOptOpt("s1", *["s2", "s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4OptOptOpt("s1", "s2", *["s3", "s4"]));
    eq("unaryStar(s1; [s2, s3, s4])", starRef4OptOptOpt("s1", "s2", "s3", *["s4"]));
}


@noanno
void bug1155_unaryPlus(Bug1155 b) {
    void eq(String expect, String got) {
        if (expect != got) {
            throw Exception("Expected ``expect`` but got ``got``.");
        }
    }
    
    // A String(String, String+) is a String(String, String+)
    String(String, String+) starRef1Plus = b.unaryPlus;
    eq("unaryPlus(s1; [s2])", starRef1Plus("s1", "s2"));
    eq("unaryPlus(s1; [s2, s3])", starRef1Plus("s1", "s2", "s3"));
    // spread
    eq("unaryPlus(s1; [s2])", starRef1Plus(*["s1", "s2"]));
    eq("unaryPlus(s1; [s2])", starRef1Plus("s1", *["s2"]));
    eq("unaryPlus(s1; [s2, s3])", starRef1Plus(*["s1", "s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef1Plus("s1", *["s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef1Plus("s1", "s2", *["s3"]));
    
    // A String(String, String+) is a String(String, String)
    String(String, String) starRef2 = b.unaryPlus;
    eq("unaryPlus(s1; [s2])", starRef2("s1", "s2"));
    // spread
    eq("unaryPlus(s1; [s2])", starRef2(*["s1", "s2"]));
    eq("unaryPlus(s1; [s2])", starRef2("s1", *["s2"]));
    
    // A String(String, String+) is a String(String , String, String*) 
    String(String, String, String*) starRef2Star = b.unaryPlus;
    eq("unaryPlus(s1; [s2])", starRef2Star("s1", "s2"));
    eq("unaryPlus(s1; [s2, s3])", starRef2Star("s1", "s2", "s3"));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Star("s1", "s2", "s3", "s4"));
    // spread
    eq("unaryPlus(s1; [s2])", starRef2Star(*["s1", "s2"]));
    eq("unaryPlus(s1; [s2])", starRef2Star("s1", *["s2"]));
    eq("unaryPlus(s1; [s2])", starRef2Star("s1", "s2", *[]));
    eq("unaryPlus(s1; [s2, s3])", starRef2Star("s1", "s2", "s3"));
    eq("unaryPlus(s1; [s2, s3])", starRef2Star(*["s1", "s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef2Star("s1", *["s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef2Star("s1", "s2", *["s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef2Star("s1", "s2", "s3", *[]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Star(*["s1", "s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Star("s1", *["s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Star("s1", "s2", *["s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Star("s1", "s2", "s3", *["s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Star("s1", "s2", "s3", "s4", *[]));
    
    // A String(String, String+) is a String(String, String, String+)
    String(String, String, String+) starRef2Plus = b.unaryPlus;
    eq("unaryPlus(s1; [s2, s3])", starRef2Plus("s1", "s2", "s3"));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Plus("s1", "s2", "s3", "s4"));
    //spread
    eq("unaryPlus(s1; [s2, s3])", starRef2Plus(*["s1", "s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef2Plus("s1", *["s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef2Plus("s1", "s2", *["s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef2Plus("s1", "s2", "s3", *[]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Plus(*["s1", "s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Plus("s1", *["s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Plus("s1", "s2", *["s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Plus("s1", "s2", "s3", *["s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef2Plus("s1", "s2", "s3", "s4", *[]));

    // A String(String, String+) is a String(String, String, String)
    String(String, String, String) starRef3 = b.unaryPlus;
    eq("unaryPlus(s1; [s2, s3])", starRef3("s1", "s2", "s3"));
    // spread
    eq("unaryPlus(s1; [s2, s3])", starRef3(*["s1", "s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef3("s1", *["s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef3("s1", "s2", *["s3"]));
    
    // A String(String, String+) is a String(String, String, String=)
    String(String, String, String=) starRef3Opt = b.unaryPlus;
    eq("unaryPlus(s1; [s2, s3])", starRef3Opt("s1", "s2", "s3"));
    eq("unaryPlus(s1; [s2])", starRef3Opt("s1", "s2"));
    // spread
    eq("unaryPlus(s1; [s2, s3])", starRef3Opt(*["s1", "s2", "s3"]));
    eq("unaryPlus(s1; [s2])", starRef3Opt(*["s1", "s2"]));
    eq("unaryPlus(s1; [s2, s3])", starRef3Opt("s1", *["s2", "s3"]));
    eq("unaryPlus(s1; [s2])", starRef3Opt("s1", *["s2"]));
    eq("unaryPlus(s1; [s2, s3])", starRef3Opt("s1", "s2", *["s3"]));
    eq("unaryPlus(s1; [s2])", starRef3Opt("s1", "s2", *[]));
    
    // A String(String, String+) is a String(String, String, String, String*)
    String(String, String, String, String*) starRef3Star = b.unaryPlus;
    eq("unaryPlus(s1; [s2, s3])", starRef3Star("s1", "s2", "s3"));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Star("s1", "s2", "s3", "s4"));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("unaryPlus(s1; [s2, s3])", starRef3Star(*["s1", "s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef3Star("s1", *["s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef3Star("s1", "s2", *["s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef3Star("s1", "s2", "s3", *[]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Star(*["s1", "s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Star("s1", *["s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Star("s1", "s2", *["s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Star("s1", "s2", "s3", *["s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Star("s1", "s2", "s3", "s4", *[]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3Star(*["s1", "s2", "s3", "s4", "s5"]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3Star("s1", *["s2", "s3", "s4", "s5"]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3Star("s1", "s2", *["s3", "s4", "s5"]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", *["s4", "s5"]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", *["s5"]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String, String+) is a String(String, String, String=, String*)
    String(String, String, String=, String*) starRef3OptStar = b.unaryPlus;
    eq("unaryPlus(s1; [s2])", starRef3OptStar("s1", "s2"));
    eq("unaryPlus(s1; [s2, s3])", starRef3OptStar("s1", "s2", "s3"));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3OptStar("s1", "s2", "s3", "s4"));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("unaryPlus(s1; [s2])", starRef3OptStar(*["s1", "s2"]));
    eq("unaryPlus(s1; [s2])", starRef3OptStar("s1", *["s2"]));
    eq("unaryPlus(s1; [s2])", starRef3OptStar("s1", "s2", *[]));
    eq("unaryPlus(s1; [s2, s3])", starRef3OptStar(*["s1", "s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef3OptStar("s1", *["s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef3OptStar("s1", "s2", *["s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef3OptStar("s1", "s2", "s3", *[]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3OptStar(*["s1", "s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3OptStar("s1", *["s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3OptStar("s1", "s2", *["s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3OptStar("s1", "s2", "s3", *["s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3OptStar("s1", "s2", "s3", "s4", *[]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3OptStar(*["s1", "s2", "s3", "s4", "s5"]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", *["s2", "s3", "s4", "s5"]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", "s2", *["s3", "s4", "s5"]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", *["s4", "s5"]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", *["s5"]));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String, String+) is a String(String, String, String, String+)
    String(String, String, String, String+) starRef3Plus = b.unaryPlus;
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Plus("s1", "s2", "s3", "s4"));
    eq("unaryPlus(s1; [s2, s3, s4, s5])", starRef3Plus("s1", "s2", "s3", "s4", "s5"));
    // spread
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Plus(*["s1", "s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Plus("s1", *["s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Plus("s1", "s2", *["s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Plus("s1", "s2", "s3", *["s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef3Plus("s1", "s2", "s3", "s4", *[]));
    
    // A String(String, String+) is a String(String, String, String, String)
    String(String, String, String, String) starRef4 = b.unaryPlus;
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4("s1", "s2", "s3", "s4"));
    // spread
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4(*["s1", "s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4("s1", *["s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4("s1", "s2", *["s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4("s1", "s2", "s3", *["s4"]));
    
    // A String(String, String+) is a String(String, String, String, String=)
    String(String, String, String, String=) starRef4Opt = b.unaryPlus;
    eq("unaryPlus(s1; [s2, s3])", starRef4Opt("s1", "s2", "s3"));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4Opt("s1", "s2", "s3", "s4"));
    // spread
    eq("unaryPlus(s1; [s2, s3])", starRef4Opt(*["s1", "s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef4Opt("s1", *["s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef4Opt("s1", "s2", *["s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef4Opt("s1", "s2", "s3", *[]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4Opt(*["s1", "s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4Opt("s1", *["s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4Opt("s1", "s2", *["s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4Opt("s1", "s2", "s3", *["s4"]));
    
    // A String(String, String+) is a String(String, String, String=, String=)
    String(String, String, String=, String=) starRef4OptOpt = b.unaryPlus;
    eq("unaryPlus(s1; [s2])", starRef4OptOpt("s1", "s2"));
    eq("unaryPlus(s1; [s2, s3])", starRef4OptOpt("s1", "s2", "s3"));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4OptOpt("s1", "s2", "s3", "s4"));
    // spread
    eq("unaryPlus(s1; [s2])", starRef4OptOpt(*["s1", "s2"]));
    eq("unaryPlus(s1; [s2])", starRef4OptOpt("s1", *["s2"]));
    eq("unaryPlus(s1; [s2])", starRef4OptOpt("s1", "s2", *[]));
    eq("unaryPlus(s1; [s2, s3])", starRef4OptOpt(*["s1", "s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef4OptOpt("s1", *["s2", "s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef4OptOpt("s1", "s2", *["s3"]));
    eq("unaryPlus(s1; [s2, s3])", starRef4OptOpt("s1", "s2", "s3", *[]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4OptOpt(*["s1", "s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4OptOpt("s1", *["s2", "s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4OptOpt("s1", "s2", *["s3", "s4"]));
    eq("unaryPlus(s1; [s2, s3, s4])", starRef4OptOpt("s1", "s2", "s3", *["s4"]));
    
}

@noanno
void bug1155_binaryStar(Bug1155 b) {
    void eq(String expect, String got) {
        if (expect != got) {
            throw Exception("Expected ``expect`` but got ``got``.");
        }
    }
    
    // A String(String, String, String*) is a String(String, String+)
    /* TODO This should be true! #765
    String(String, String+) starRef1Plus = b.binaryStar;
    eq("binaryStar(s1, s2; {})", starRef1Plus("s1", "s2"));
    eq("binaryStar(s1, s2; [s3])", starRef1Plus("s1", "s2", "s3"));
    // spread
    eq("binaryStar(s1, s2; {})", starRef1Plus(*["s1", "s2"]));
    eq("binaryStar(s1, s2; {})", starRef1Plus("s1", *["s2"]));
    eq("binaryStar(s1, s2; [s3])", starRef1Plus(*["s1", "s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef1Plus("s1", *["s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef1Plus("s1", "s2", *["s3"]));
    */
    // A String(String, String, String*) is a String(String, String)
    String(String, String) starRef2 = b.binaryStar;
    eq("binaryStar(s1, s2; {})", starRef2("s1", "s2"));
    // spread
    eq("binaryStar(s1, s2; {})", starRef2(*["s1", "s2"]));
    eq("binaryStar(s1, s2; {})", starRef2("s1", *["s2"]));
    
    // A String(String, String, String*) is a String(String , String, String*) 
    String(String, String, String*) starRef2Star = b.binaryStar;
    eq("binaryStar(s1, s2; {})", starRef2Star("s1", "s2"));
    eq("binaryStar(s1, s2; [s3])", starRef2Star("s1", "s2", "s3"));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Star("s1", "s2", "s3", "s4"));
    // spread
    eq("binaryStar(s1, s2; {})", starRef2Star(*["s1", "s2"]));
    eq("binaryStar(s1, s2; {})", starRef2Star("s1", *["s2"]));
    eq("binaryStar(s1, s2; {})", starRef2Star("s1", "s2", *[]));
    eq("binaryStar(s1, s2; [s3])", starRef2Star("s1", "s2", "s3"));
    eq("binaryStar(s1, s2; [s3])", starRef2Star(*["s1", "s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef2Star("s1", *["s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef2Star("s1", "s2", *["s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef2Star("s1", "s2", "s3", *[]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Star(*["s1", "s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Star("s1", *["s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Star("s1", "s2", *["s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Star("s1", "s2", "s3", *["s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Star("s1", "s2", "s3", "s4", *[]));
    
    // A String(String, String, String*) is a String(String, String, String+)
    String(String, String, String+) starRef2Plus = b.binaryStar;
    eq("binaryStar(s1, s2; [s3])", starRef2Plus("s1", "s2", "s3"));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Plus("s1", "s2", "s3", "s4"));
    //spread
    eq("binaryStar(s1, s2; [s3])", starRef2Plus(*["s1", "s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef2Plus("s1", *["s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef2Plus("s1", "s2", *["s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef2Plus("s1", "s2", "s3", *[]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Plus(*["s1", "s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Plus("s1", *["s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Plus("s1", "s2", *["s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Plus("s1", "s2", "s3", *["s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef2Plus("s1", "s2", "s3", "s4", *[]));

    // A String(String, String, String*) is a String(String, String, String)
    String(String, String, String) starRef3 = b.binaryStar;
    eq("binaryStar(s1, s2; [s3])", starRef3("s1", "s2", "s3"));
    // spread
    eq("binaryStar(s1, s2; [s3])", starRef3(*["s1", "s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef3("s1", *["s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef3("s1", "s2", *["s3"]));
    
    // A String(String, String, String*) is a String(String, String, String=)
    String(String, String, String=) starRef3Opt = b.binaryStar;
    eq("binaryStar(s1, s2; [s3])", starRef3Opt("s1", "s2", "s3"));
    eq("binaryStar(s1, s2; {})", starRef3Opt("s1", "s2"));
    // spread
    eq("binaryStar(s1, s2; [s3])", starRef3Opt(*["s1", "s2", "s3"]));
    eq("binaryStar(s1, s2; {})", starRef3Opt(*["s1", "s2"]));
    eq("binaryStar(s1, s2; [s3])", starRef3Opt("s1", *["s2", "s3"]));
    eq("binaryStar(s1, s2; {})", starRef3Opt("s1", *["s2"]));
    eq("binaryStar(s1, s2; [s3])", starRef3Opt("s1", "s2", *["s3"]));
    eq("binaryStar(s1, s2; {})", starRef3Opt("s1", "s2", *[]));
    
    // A String(String, String, String*) is a String(String, String, String, String*)
    String(String, String, String, String*) starRef3Star = b.binaryStar;
    eq("binaryStar(s1, s2; [s3])", starRef3Star("s1", "s2", "s3"));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Star("s1", "s2", "s3", "s4"));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("binaryStar(s1, s2; [s3])", starRef3Star(*["s1", "s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef3Star("s1", *["s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef3Star("s1", "s2", *["s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef3Star("s1", "s2", "s3", *[]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Star(*["s1", "s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Star("s1", *["s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Star("s1", "s2", *["s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Star("s1", "s2", "s3", *["s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Star("s1", "s2", "s3", "s4", *[]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3Star(*["s1", "s2", "s3", "s4", "s5"]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3Star("s1", *["s2", "s3", "s4", "s5"]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3Star("s1", "s2", *["s3", "s4", "s5"]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3Star("s1", "s2", "s3", *["s4", "s5"]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", *["s5"]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String, String, String*) is a String(String, String, String=, String*)
    String(String, String, String=, String*) starRef3OptStar = b.binaryStar;
    eq("binaryStar(s1, s2; {})", starRef3OptStar("s1", "s2"));
    eq("binaryStar(s1, s2; [s3])", starRef3OptStar("s1", "s2", "s3"));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3OptStar("s1", "s2", "s3", "s4"));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("binaryStar(s1, s2; {})", starRef3OptStar(*["s1", "s2"]));
    eq("binaryStar(s1, s2; {})", starRef3OptStar("s1", *["s2"]));
    eq("binaryStar(s1, s2; {})", starRef3OptStar("s1", "s2", *[]));
    eq("binaryStar(s1, s2; [s3])", starRef3OptStar(*["s1", "s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef3OptStar("s1", *["s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef3OptStar("s1", "s2", *["s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef3OptStar("s1", "s2", "s3", *[]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3OptStar(*["s1", "s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3OptStar("s1", *["s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3OptStar("s1", "s2", *["s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3OptStar("s1", "s2", "s3", *["s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3OptStar("s1", "s2", "s3", "s4", *[]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3OptStar(*["s1", "s2", "s3", "s4", "s5"]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3OptStar("s1", *["s2", "s3", "s4", "s5"]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3OptStar("s1", "s2", *["s3", "s4", "s5"]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", *["s4", "s5"]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", *["s5"]));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3OptStar("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String, String, String*) is a String(String, String, String, String+)
    String(String, String, String, String+) starRef3Plus = b.binaryStar;
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Plus("s1", "s2", "s3", "s4"));
    eq("binaryStar(s1, s2; [s3, s4, s5])", starRef3Plus("s1", "s2", "s3", "s4", "s5"));
    // spread
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Plus(*["s1", "s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Plus("s1", *["s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Plus("s1", "s2", *["s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Plus("s1", "s2", "s3", *["s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef3Plus("s1", "s2", "s3", "s4", *[]));
    
    // A String(String, String, String*) is a String(String, String, String, String)
    String(String, String, String, String) starRef4 = b.binaryStar;
    eq("binaryStar(s1, s2; [s3, s4])", starRef4("s1", "s2", "s3", "s4"));
    // spread
    eq("binaryStar(s1, s2; [s3, s4])", starRef4(*["s1", "s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4("s1", *["s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4("s1", "s2", *["s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4("s1", "s2", "s3", *["s4"]));
    
    // A String(String, String, String*) is a String(String, String, String, String=)
    String(String, String, String, String=) starRef4Opt = b.binaryStar;
    eq("binaryStar(s1, s2; [s3])", starRef4Opt("s1", "s2", "s3"));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4Opt("s1", "s2", "s3", "s4"));
    // spread
    eq("binaryStar(s1, s2; [s3])", starRef4Opt(*["s1", "s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef4Opt("s1", *["s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef4Opt("s1", "s2", *["s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef4Opt("s1", "s2", "s3", *[]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4Opt(*["s1", "s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4Opt("s1", *["s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4Opt("s1", "s2", *["s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4Opt("s1", "s2", "s3", *["s4"]));
    
    // A String(String, String, String*) is a String(String, String, String=, String=)
    String(String, String, String=, String=) starRef4OptOpt = b.binaryStar;
    eq("binaryStar(s1, s2; {})", starRef4OptOpt("s1", "s2"));
    eq("binaryStar(s1, s2; [s3])", starRef4OptOpt("s1", "s2", "s3"));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4OptOpt("s1", "s2", "s3", "s4"));
    // spread
    eq("binaryStar(s1, s2; {})", starRef4OptOpt(*["s1", "s2"]));
    eq("binaryStar(s1, s2; {})", starRef4OptOpt("s1", *["s2"]));
    eq("binaryStar(s1, s2; {})", starRef4OptOpt("s1", "s2", *[]));
    eq("binaryStar(s1, s2; [s3])", starRef4OptOpt(*["s1", "s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef4OptOpt("s1", *["s2", "s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef4OptOpt("s1", "s2", *["s3"]));
    eq("binaryStar(s1, s2; [s3])", starRef4OptOpt("s1", "s2", "s3", *[]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4OptOpt(*["s1", "s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4OptOpt("s1", *["s2", "s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4OptOpt("s1", "s2", *["s3", "s4"]));
    eq("binaryStar(s1, s2; [s3, s4])", starRef4OptOpt("s1", "s2", "s3", *["s4"]));
    
}

@noanno
void bug1155_binaryPlus(String desc, String(String, String, String+) ref) {
    void eq(String expect, String got) {
        if (expect != got) {
            throw Exception("Expected ``expect`` but got ``got`` when using callable obtained via ``desc``");
        }
    }
    
    // A String(String, String, String+) is aString(String, String, String+)
    String(String, String, String+) starRef2Plus = ref;
    eq("binaryPlus(s1, s2; [s3])", starRef2Plus("s1", "s2", "s3"));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef2Plus("s1", "s2", "s3", "s4"));
    //spread
    eq("binaryPlus(s1, s2; [s3])", starRef2Plus(*["s1", "s2", "s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef2Plus("s1", *["s2", "s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef2Plus("s1", "s2", *["s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef2Plus("s1", "s2", "s3", *[]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef2Plus(*["s1", "s2", "s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef2Plus("s1", *["s2", "s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef2Plus("s1", "s2", *["s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef2Plus("s1", "s2", "s3", *["s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef2Plus("s1", "s2", "s3", "s4", *[]));
    
    // A String(String, String, String+) is aString(String, String, String)
    String(String, String, String) starRef3 = ref;
    eq("binaryPlus(s1, s2; [s3])", starRef3("s1", "s2", "s3"));
    // spread
    eq("binaryPlus(s1, s2; [s3])", starRef3(*["s1", "s2", "s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef3("s1", *["s2", "s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef3("s1", "s2", *["s3"]));
    
    // A String(String, String, String+) is aString(String, String, String, String*)
    String(String, String, String, String*) starRef3Star = ref;
    eq("binaryPlus(s1, s2; [s3])", starRef3Star("s1", "s2", "s3"));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Star("s1", "s2", "s3", "s4"));
    eq("binaryPlus(s1, s2; [s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", "s5"));
    //spread
    eq("binaryPlus(s1, s2; [s3])", starRef3Star(*["s1", "s2", "s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef3Star("s1", *["s2", "s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef3Star("s1", "s2", *["s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef3Star("s1", "s2", "s3", *[]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Star(*["s1", "s2", "s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Star("s1", *["s2", "s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Star("s1", "s2", *["s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Star("s1", "s2", "s3", *["s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Star("s1", "s2", "s3", "s4", *[]));
    eq("binaryPlus(s1, s2; [s3, s4, s5])", starRef3Star(*["s1", "s2", "s3", "s4", "s5"]));
    eq("binaryPlus(s1, s2; [s3, s4, s5])", starRef3Star("s1", *["s2", "s3", "s4", "s5"]));
    eq("binaryPlus(s1, s2; [s3, s4, s5])", starRef3Star("s1", "s2", *["s3", "s4", "s5"]));
    eq("binaryPlus(s1, s2; [s3, s4, s5])", starRef3Star("s1", "s2", "s3", *["s4", "s5"]));
    eq("binaryPlus(s1, s2; [s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", *["s5"]));
    eq("binaryPlus(s1, s2; [s3, s4, s5])", starRef3Star("s1", "s2", "s3", "s4", "s5", *[]));
    
    // A String(String, String, String+) is aString(String, String, String, String+)
    String(String, String, String, String+) starRef3Plus = ref;
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Plus("s1", "s2", "s3", "s4"));
    eq("binaryPlus(s1, s2; [s3, s4, s5])", starRef3Plus("s1", "s2", "s3", "s4", "s5"));
    // spread
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Plus(*["s1", "s2", "s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Plus("s1", *["s2", "s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Plus("s1", "s2", *["s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Plus("s1", "s2", "s3", *["s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef3Plus("s1", "s2", "s3", "s4", *[]));
    
    // A String(String, String, String+) is aString(String, String, String, String)
    String(String, String, String, String) starRef4 = ref;
    eq("binaryPlus(s1, s2; [s3, s4])", starRef4("s1", "s2", "s3", "s4"));
    // spread
    eq("binaryPlus(s1, s2; [s3, s4])", starRef4(*["s1", "s2", "s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef4("s1", *["s2", "s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef4("s1", "s2", *["s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef4("s1", "s2", "s3", *["s4"]));
    
    // A String(String, String, String+) is aString(String, String, String, String=)
    String(String, String, String, String=) starRef4Opt = ref;
    eq("binaryPlus(s1, s2; [s3])", starRef4Opt("s1", "s2", "s3"));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef4Opt("s1", "s2", "s3", "s4"));
    // spread
    eq("binaryPlus(s1, s2; [s3])", starRef4Opt(*["s1", "s2", "s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef4Opt("s1", *["s2", "s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef4Opt("s1", "s2", *["s3"]));
    eq("binaryPlus(s1, s2; [s3])", starRef4Opt("s1", "s2", "s3", *[]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef4Opt(*["s1", "s2", "s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef4Opt("s1", *["s2", "s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef4Opt("s1", "s2", *["s3", "s4"]));
    eq("binaryPlus(s1, s2; [s3, s4])", starRef4Opt("s1", "s2", "s3", *["s4"]));
    
}

void bug1155() {
    value b = Bug1155();
    
    bug1155_star(b);
    bug1155_plus(b);
    
    bug1155_unaryOpt(b);
    bug1155_unaryStar(b);
    bug1155_unaryPlus(b);
    bug1155_unaryOptStar(b);
    
    bug1155_binaryOpt(b);
    bug1155_binaryOptOpt(b);
    
    bug1155_binaryStar(b);
    
    bug1155_binaryPlus("method reference", 
        b.binaryPlus);
    bug1155_binaryPlus("anonymous", 
        function(String s1, String s2, String+ variadic) => b.binaryPlus(s1, s2, *variadic));
    bug1155_binaryPlus{
        desc="method argument"; 
        function ref(String s1, String s2, String+ variadic) 
            => b.binaryPlus(s1, s2, *variadic);
    };
    value x = Bug1155.binaryPlus;
    bug1155_binaryPlus("'static' method reference", 
        x(b));
    /*String mpl(Bug1155 b1)(String s1, String s2, String+ variadic) {
        return b1.binaryPlus(s1, s2, *variadic);
    }
    bug1155_binaryPlus("MPL", mpl(b));*/
    
    /* TODO
    bug1155_ternaryStar(b);
    bug1155_ternaryPlus(b);
    bug1155_quaternaryStar(b);
    bug1155_quaternaryPlus(b);
    */
}