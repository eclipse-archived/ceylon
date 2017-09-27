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
Integer bug1227_ifBreak_returnElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            function ohNo() => from + 1;
            break;
        }
    }
    else {
        return 0;
    }
    return from;
}
@noanno
Integer bug1227_ifBreak_returnElse_opt() {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in 1..10) {
        if (someBoolean) {
            from = index;
            break;
        }
    }
    else {
        return 0;
    }
    return from;
}
@noanno
Integer bug1227_ifBreak_specifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            value x = from + 1;
            break;
        }
    }
    else {
        from = 0;
    }
    return from;
}
@noanno
Integer bug1227_ifBreak_specifiedElse_opt() {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in 1..10) {
        if (someBoolean) {
            from = index;
            value x = from + 1;
            break;
        }
    }
    else {
        from = 0;
    }
    return from;
}
@noanno
Integer bug1227_ifBreak_elseBreak_specifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            function ohNo() => from + 1;
            break;
        } else {
            from = index + 1;
            class C() {
                value x = from;
            }
            break;
        }
    }
    else {
        from = 0;
    }
    return from;
}
@noanno
Integer bug1227_ifBreak_elseBreak_specifiedElse_opt() {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in 1..10) {
        if (someBoolean) {
            from = index;
            function ohNo() => from + 1;
            break;
        } else {
            from = index + 1;
            class C() {
                value x = from;
            }
            break;
        }
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_ifReturn_specifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            function ohNo() => from + 1;
            return from;
        }
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_ifReturn_specifiedElse_opt() {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in 1..10) {
        if (someBoolean) {
            from = index;
            function ohNo() => from + 1;
            return from;
        }
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_ifThrow_specifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            throw;
        }
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_ifThrow_specifiedElse_opt() {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in 1..10) {
        if (someBoolean) {
            from = index;
            throw;
        }
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_for2_ifReturn_specifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        for (index2 in l) {
            if (someBoolean) {
                from = index + index2;
                return from;
            }
        }
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_declaredInside(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    for (index in l) {
        Integer from;
        if (someBoolean) {
            from = index;
            function ohNo() => from + 1;
            break;
        }
    }
    else {
        return 0;
    }
    return 0;
}

@noanno
Integer bug1227_onlySpecifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        throw;
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_function(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from();
    for (index in l) {
        if (someBoolean) {
            from = ()=>index;
            break;
        }
    }
    else {
        return 0;
    }
    return from();
}
