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
@nomodel
shared interface Visitor{
    shared default void openTag(String name){}
    shared default void closeTag(String name){}
    shared default void text(String text){}
    shared void tag(String name){
    }
}

@nomodel
shared interface Visitable{
    shared formal void visit(Visitor visitor);
    shared void visitAroundText(Visitor visitor, String name, String text){
    }
    shared default void visitAroundText2(Visitor visitor, String name, String text){
    }
    shared void visitAroundTags(Visitor visitor, String name, Tag|String* tags){
    }
}

@nomodel
shared class Html(head = null, body = null) satisfies Visitable {
    shared Head? head;
    shared Body? body;

    shared actual void visit(Visitor visitor){
    }
}

@nomodel
shared class Head(title = null) satisfies Visitable {
    shared String? title;
    shared actual void visit(Visitor visitor) {
    }
}

@nomodel
shared interface Tag satisfies Visitable {}
@nomodel
shared interface InlineTag satisfies Tag{}

@nomodel
shared class Container<TagType>(String name, TagType|String* initialTags) satisfies Visitable 
    given TagType satisfies Tag {

    shared Sequential<TagType|String> tags = initialTags;

    shared actual void visit(Visitor visitor) {
        visitAroundTags(visitor, "body", *tags);
    }
}

@nomodel
shared class Body(Tag|String* initialTags) extends Container<Tag>("body", *initialTags) {}

@nomodel
shared class Block(String name, Tag|String* initialTags) extends Container<Tag>(name, *initialTags) satisfies Tag {}

@nomodel
shared class P(Tag|String* tags) extends Block("p", *tags){}

@nomodel
shared class Inline(String name, InlineTag|String* initialTags) extends Container<InlineTag>(name, *initialTags) satisfies InlineTag {}

@nomodel
shared class B(InlineTag|String* tags) extends Inline("b", *tags){}