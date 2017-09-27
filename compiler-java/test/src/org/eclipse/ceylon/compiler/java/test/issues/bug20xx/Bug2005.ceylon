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
//import java.sql {
//    Timestamp
//}

@noanno
shared void run() {
    /*Does not compile:
     Cannot find symbol
     symbol:   method $default$LogMsg$text(java.sql.Timestamp,java.lang.String,java.lang.String)
     location: class bugs.run_
     in lines 16 and 19
     */
    
    value log = SQLog();
    value logmsg = log.LogMsg {  //line 16
        //channel = "test";
        error = "17";
        //source = "something";  //line 19
    };
}

@noanno
shared class SQLog()  {
    
    shared class LogMsg(
        //timestamp = Timestamp(system.milliseconds),
        //channel = "",
        //source = "",
        text = "",
        severity = 0,
        error = ""/*,
        stack = empty,
        custom = null*/) {
        //shared variable Timestamp timestamp;
        //shared variable String channel;
        //shared variable String source;
        shared variable String text;
        shared variable Integer severity;
        shared variable String error;
        //shared variable String[] stack;
        //shared variable Object? custom;
    }
}
