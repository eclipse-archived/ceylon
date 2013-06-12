/*
 * Copyright 2011 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Importing the collection module from Herd
import ceylon.collection { ... }

"Return the first letter of a word in lower case"
shared Character firstLetter(String word) {
    assert (exists f=word.first);
    return f.lowercased;
}

"Group words in a text together on the basis of their first letter"
shared Map<Character, {String+}> groupWordsByFirstLetter(String text) {
    return group(text.split({ ' ', '.', ',', ';', '\n' }), firstLetter);
}

"The runnable method of the module." 
shared void run(){
    String text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam
                   nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat
                   volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation
                   ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.
                   Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse
                   molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero
                   eros et accumsan et iusto odio dignissim qui blandit praesent luptatum
                   zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber
                   tempor cum soluta nobis eleifend option congue nihil imperdiet doming
                   id quod mazim placerat facer possim assum. Typi non habent claritatem
                   insitam; est usus legentis in iis qui facit eorum claritatem.
                   Investigationes demonstraverunt lectores legere me lius quod ii legunt
                   saepius. Claritas est etiam processus dynamicus, qui sequitur mutationem
                   consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc
                   putamus parum claram, anteposuerit litterarum formas humanitatis per
                   seacula quarta decima et quinta decima. Eodem modo typi, qui nunc nobis
                   videntur parum clari, fiant sollemnes in futurum";
    value groups = groupWordsByFirstLetter(text);
    value sortedGroups = groups.sort(byKey(byIncreasing((Character c) => c)));
    value lines = "\n".join { for(entry in sortedGroups) entry.string };
    print(lines);
}

