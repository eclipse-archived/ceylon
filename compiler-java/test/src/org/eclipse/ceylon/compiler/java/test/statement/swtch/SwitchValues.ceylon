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
class SwitchValues() {
    void definiteStringValues(String name) {
        // with else
        switch (name)
        case ("Tako", "Gavin") { print("Spain"); }
        case ("Emmanuel", "Stef", "David") { print("France"); }
        else { print("Somewhere else"); }
    }
    
    void optionalStringValues(String? name) {
        // singleton null case
        switch (name)
        case ("Tako", "Gavin") { print("Spain"); }
        case ("Emmanuel", "Stef", "David") { print("France"); }
        case (null) { print("Nowhere"); }
        else { print("Somewhere else"); }
        // non-singleton null case => if/else chain
        switch (name)
        case ("Tako", "Gavin") { print("Spain"); }
        case ("Emmanuel", "Stef", "David",null) { print("France or null"); }
        else { print("Somewhere else"); }
        // null covered by else
        switch (name)
        case ("Tako", "Gavin") { print("Spain"); }
        case ("Emmanuel", "Stef", "David") { print("France"); }
        else { print("Somewhere else"); }
    }
    
    void definiteCharacterValues(Character ch) {
        switch (ch)
        case('+') { print("plus"); }
        case('-') { print("minus"); }
        case('0', '1', '2', '3', '4', '5', '6', '7', '8', '9') { print("digit"); }
        else { print("invalid character"); }
    }
    
    void optionalCharacterValues(Character? ch) {
        // singleton null case
        switch (ch)
        case('+') { print("plus"); }
        case('-') { print("minus"); }
        case('0', '1', '2', '3', '4', '5', '6', '7', '8', '9') { print("digit"); }
        case(null) { print("null"); }
        else { print("invalid character"); }
        // non-singleton null case
        switch (ch)
        case('+') { print("plus"); }
        case('-') { print("minus"); }
        case('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', null) { print("digit or null"); }
        else { print("invalid character"); }
        // null covered by else
        switch (ch)
        case('+') { print("plus"); }
        case('-') { print("minus"); }
        case('0', '1', '2', '3', '4', '5', '6', '7', '8', '9') { print("digit"); }
        else { print("invalid character"); }
    }
    
    void definiteIntegerValues(Integer num) {
        switch (num)
        case (-1) { print("negative identity"); }
        case (0) { print("additive identity"); }
        case (1) { print("multiplicative identity"); }
        else { print("something else"); }
    }
    
    void optionalIntegerValues(Integer? num) {
        // singleton null case
        switch (num)
        case (-1) { print("negative identity"); }
        case (0) { print("additive identity"); }
        case (1) { print("multiplicative identity"); }
        case (null) { print("null"); }
        else { print("something else"); }
        // non-singleton null case
        switch (num)
        case (-1) { print("negative identity"); }
        case (0) { print("additive identity"); }
        case (1,null) { print("multiplicative identity or null"); }
        else { print("something else"); }
        // null covered by else
        switch (num)
        case (-1) { print("negative identity"); }
        case (0) { print("additive identity"); }
        case (1) { print("multiplicative identity"); }
        case (null) { print("null"); }
        else { print("something else"); }
    }
    
    void definiteMixedValues(Integer|String|Character val) {
        switch(val)
        case (1) { print("integer 1"); }
        case ("1") { print("string 1"); }
        case ('1') { print("character 1"); }
        else { print("something else"); }
    }
    
    void optionalMixedValues(Integer|String|Character|Null val) {
        // singleton null case
        switch(val)
        case (1) { print("integer 1"); }
        case ("1") { print("string 1"); }
        case ('1') { print("character 1"); }
        case (null) { print("null"); }
        else { print("something else"); }
        // non-singleton null case
        switch(val)
        case (1) { print("integer 1"); }
        case ("1") { print("string 1"); }
        case ('1', null) { print("character 1, or null"); }
        else { print("something else"); }
        // null covered by else
        switch(val)
        case (1) { print("integer 1"); }
        case ("1") { print("string 1"); }
        case ('1') { print("character 1"); }
        else { print("something else"); }
    }
    
    
    void definiteBooleanValues(Boolean b) {
        switch (b)
        case (true) { print("t"); }
        case (false) { print("f"); }
        switch (b)
        case (true) { print("t"); }
        else { print("f"); }
    }
    
    void optionalBooleanValues(Boolean? b) {
        switch (b)
        case (true) { print("t"); }
        case (false) { print("f"); }
        case (null) { print("n"); }
        
        switch (b)
        case (true) { print("t"); }
        case (false, null) { print("f"); }
        
        switch (b)
        case (true) { print("t"); }
        case (false) { print("f"); }
        else { print("n"); }
        
    }
}