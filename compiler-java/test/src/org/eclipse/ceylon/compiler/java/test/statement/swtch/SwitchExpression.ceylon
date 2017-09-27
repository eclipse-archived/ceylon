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
void switchExpression(Object val) {
    String s1 = switch (val) 
        case (true) "true" 
        case (false) "false" 
        case (is Float) (val + val).string 
        else val.string;
    Anything val2 = val;
    String? s2 = switch (val2) 
        case (is String?) val2
        else "asd";
    switch(val2)
    case (is String?){ print(val2); }
    else{ print("asd");}
}

@noanno
class SwitchExpressionTop(Anything a){}

@noanno
class SwitchExpressionBottom(Anything val) extends SwitchExpressionTop(switch (val) 
        case (is String?) val
        else "asd"){
    
    Integer? arg = null;
    value y0 = switch (arg) case (null) 0.0 case (is Integer) 1.0;
    value y1 = switch (arg) case (null) "null" case (is Integer) arg.string;
    value y2 = switch (arg) case (null) 0.0 case (is Integer) arg.string;
    value y3 = switch (arg) case (null) 0.0 case (is Integer) null;
}

void switchExpressionTest(){
    SwitchExpressionBottom("asd");
}
