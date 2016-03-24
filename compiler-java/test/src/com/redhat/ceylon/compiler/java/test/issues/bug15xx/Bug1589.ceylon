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
{Element*} bug1589_concat<Element>({Element*}* iterables)
        => { for (elements in iterables) for (element in elements) element };
[]|[Element]|[Element,Element] bug1589_noneOneOrTwo<Element>(Element element)
        => [element];

[String, String, String] bug1589_notempty = ["one", "two", "three"];
{String*} bug1589_spreadNotempty = bug1589_concat(*bug1589_notempty.map(bug1589_noneOneOrTwo<String>));
{String*} bug1589_spreadWrappedNotempty = bug1589_concat(*{*bug1589_notempty.map(bug1589_noneOneOrTwo<String>)});
{Nothing*} bug1589_spreadEmpty = bug1589_concat(*[].map(bug1589_noneOneOrTwo<Nothing>));
{Nothing*} bug1589_spreadWrappedEmpty = bug1589_concat(*{*[].map(bug1589_noneOneOrTwo<Nothing>)});

object bug1589_myEmpty satisfies {Nothing*} {
    shared actual Iterator<Nothing> iterator() {
        object iterator satisfies Iterator<Nothing> {
            shared actual Finished next() => finished;
        }
        return iterator;
    }
    
    shared actual {Nothing*} map<Result>(Result collecting(Nothing elem))
            => this; // don't try to cast this to Sequential!
}

{Nothing*} bug1589_spreadMyEmpty = bug1589_concat(*bug1589_myEmpty.map(bug1589_noneOneOrTwo<Nothing>));
{Nothing*} bug1589_spreadWrappedMyEmpty = bug1589_concat(*{*bug1589_myEmpty.map(bug1589_noneOneOrTwo<Nothing>)});

{Nothing*} bug1589_wrap4 = bug1589_concat(*{*{*[*{
    bug1589_myEmpty.map(bug1589_noneOneOrTwo<Nothing>)}]}});


{Element+} bug1589_concatPlus<Element>({Element+}+ iterables)
        => { for (elements in iterables) for (element in elements) element };

{String+} bug1589_spreadNotemptyPlus = bug1589_concatPlus(*{bug1589_notempty});
{String+} bug1589_spreadWrappedNotemptyPlus = bug1589_concatPlus(*{*{bug1589_notempty}});

object bug1589_myNotempty satisfies {String+} {
    shared actual Iterator<String> iterator() => bug1589_notempty.iterator();
}

{String+} bug1589_spreadMyNotemptyPlus = bug1589_concatPlus(*{bug1589_myNotempty});
{String+} bug1589_spreadWrappedMyNotemptyPlus = bug1589_concatPlus(*{*{bug1589_myNotempty}});

void bug1589() {
    // sanity check
    assert (bug1589_myNotempty.sequence() == bug1589_notempty.sequence());
    assert (bug1589_myEmpty.sequence() == empty.sequence());
    // tests
    assert (bug1589_spreadNotempty.sequence() == bug1589_notempty.sequence());
    assert (bug1589_spreadWrappedNotempty.sequence() == bug1589_notempty.sequence());
    assert (bug1589_spreadEmpty.sequence() == empty.sequence());
    assert (bug1589_spreadWrappedEmpty.sequence() == empty.sequence());
    assert (bug1589_spreadMyEmpty.sequence() == bug1589_myEmpty.sequence());
    assert (bug1589_spreadWrappedMyEmpty.sequence() == bug1589_myEmpty.sequence());
    assert (bug1589_wrap4.sequence() == bug1589_myEmpty.sequence());
    assert (bug1589_spreadNotemptyPlus.sequence() == bug1589_notempty.sequence());
    assert (bug1589_spreadWrappedNotemptyPlus.sequence() == bug1589_myNotempty.sequence());
    assert (bug1589_spreadMyNotemptyPlus.sequence() == bug1589_notempty.sequence());
    assert (bug1589_spreadWrappedMyNotemptyPlus.sequence() == bug1589_myNotempty.sequence());
}
