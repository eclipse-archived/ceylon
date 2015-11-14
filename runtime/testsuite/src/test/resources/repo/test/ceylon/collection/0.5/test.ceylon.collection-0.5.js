(function (define) {
    define(function (require, exports, module) {
        var $$metamodel$$ = {"$mod-deps": ["ceylon.language\/0.5", "ceylon.test\/0.5", "ceylon.collection\/0.5"], "$mod-name": "test.ceylon.collection", "$mod-version": "0.5", "test.ceylon.collection": {"testSetRemove": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$nm": "testSetRemove"}, "testMap": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$nm": "testMap"}, "testMap2": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$nm": "testMap2"}, "testSet2": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$nm": "testSet2"}, "testSet": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$nm": "testSet"}, "testList": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$nm": "testList"}, "testMapRemove": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$nm": "testMapRemove"}, "run": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$an": {"shared": []}, "$nm": "run"}}};
        var $$$cl1 = require('ceylon/language/0.5/ceylon.language-0.5');
        var $$$cc38 = require('ceylon/collection/0.5/ceylon.collection-0.5');
        var $$$ct17 = require('ceylon/test/0.5/ceylon.test-0.5');

        function testList() {
            var l$39 = $$$cc38.LinkedList([
                {t: $$$cl1.String}
            ]);
            $$$ct17.assertEquals($$$cl1.String("[]", 2), l$39.getString());
            $$$ct17.assertEquals((0), l$39.getSize());
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("fu", 2))));
            l$39.add($$$cl1.String("fu", 2));
            $$$ct17.assertEquals($$$cl1.String("[fu]", 4), l$39.getString());
            $$$ct17.assertEquals((1), l$39.getSize());
            $$$ct17.assertEquals($$$cl1.String("fu", 2), l$39.item((0)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("fu", 2)));
            l$39.add($$$cl1.String("bar", 3));
            $$$ct17.assertEquals($$$cl1.String("[fu, bar]", 9), l$39.getString());
            $$$ct17.assertEquals((2), l$39.getSize());
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("fu", 2)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("bar", 3)));
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("stef", 4))));
            $$$ct17.assertEquals($$$cl1.String("fu", 2), l$39.item((0)));
            $$$ct17.assertEquals($$$cl1.String("bar", 3), l$39.item((1)));
            l$39.setItem((0), $$$cl1.String("foo", 3));
            $$$ct17.assertEquals($$$cl1.String("[foo, bar]", 10), l$39.getString());
            $$$ct17.assertEquals((2), l$39.getSize());
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("foo", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("bar", 3)));
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("fu", 2))));
            $$$ct17.assertEquals($$$cl1.String("foo", 3), l$39.item((0)));
            $$$ct17.assertEquals($$$cl1.String("bar", 3), l$39.item((1)));
            l$39.setItem((5), $$$cl1.String("empty", 5));
            $$$ct17.assertEquals($$$cl1.String("[foo, bar, empty, empty, empty, empty]", 38), l$39.getString());
            $$$ct17.assertEquals((6), l$39.getSize());
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("foo", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("bar", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("empty", 5)));
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("fu", 2))));
            $$$ct17.assertEquals($$$cl1.String("foo", 3), l$39.item((0)));
            $$$ct17.assertEquals($$$cl1.String("bar", 3), l$39.item((1)));
            $$$ct17.assertEquals($$$cl1.String("empty", 5), l$39.item((5)));
            l$39.insert((1), $$$cl1.String("stef", 4));
            $$$ct17.assertEquals($$$cl1.String("[foo, stef, bar, empty, empty, empty, empty]", 44), l$39.getString());
            $$$ct17.assertEquals((7), l$39.getSize());
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("foo", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("stef", 4)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("bar", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("empty", 5)));
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("fu", 2))));
            $$$ct17.assertEquals($$$cl1.String("foo", 3), l$39.item((0)));
            $$$ct17.assertEquals($$$cl1.String("stef", 4), l$39.item((1)));
            $$$ct17.assertEquals($$$cl1.String("bar", 3), l$39.item((2)));
            $$$ct17.assertEquals($$$cl1.String("empty", 5), l$39.item((6)));
            l$39.insert((0), $$$cl1.String("first", 5));
            $$$ct17.assertEquals($$$cl1.String("[first, foo, stef, bar, empty, empty, empty, empty]", 51), l$39.getString());
            $$$ct17.assertEquals((8), l$39.getSize());
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("first", 5)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("foo", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("stef", 4)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("bar", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("empty", 5)));
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("fu", 2))));
            $$$ct17.assertEquals($$$cl1.String("first", 5), l$39.item((0)));
            $$$ct17.assertEquals($$$cl1.String("foo", 3), l$39.item((1)));
            $$$ct17.assertEquals($$$cl1.String("stef", 4), l$39.item((2)));
            $$$ct17.assertEquals($$$cl1.String("bar", 3), l$39.item((3)));
            $$$ct17.assertEquals($$$cl1.String("empty", 5), l$39.item((7)));
            l$39.insert((10), $$$cl1.String("last", 4));
            $$$ct17.assertEquals($$$cl1.String("[first, foo, stef, bar, empty, empty, empty, empty, last, last, last]", 69), l$39.getString());
            $$$ct17.assertEquals((11), l$39.getSize());
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("first", 5)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("foo", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("stef", 4)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("bar", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("empty", 5)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("last", 4)));
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("fu", 2))));
            $$$ct17.assertEquals($$$cl1.String("first", 5), l$39.item((0)));
            $$$ct17.assertEquals($$$cl1.String("foo", 3), l$39.item((1)));
            $$$ct17.assertEquals($$$cl1.String("stef", 4), l$39.item((2)));
            $$$ct17.assertEquals($$$cl1.String("bar", 3), l$39.item((3)));
            $$$ct17.assertEquals($$$cl1.String("empty", 5), l$39.item((7)));
            $$$ct17.assertEquals($$$cl1.String("last", 4), l$39.item((10)));
            l$39.remove((0));
            $$$ct17.assertEquals($$$cl1.String("[foo, stef, bar, empty, empty, empty, empty, last, last, last]", 62), l$39.getString());
            $$$ct17.assertEquals((10), l$39.getSize());
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("first", 5))));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("foo", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("stef", 4)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("bar", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("empty", 5)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("last", 4)));
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("fu", 2))));
            $$$ct17.assertEquals($$$cl1.String("foo", 3), l$39.item((0)));
            $$$ct17.assertEquals($$$cl1.String("stef", 4), l$39.item((1)));
            $$$ct17.assertEquals($$$cl1.String("bar", 3), l$39.item((2)));
            $$$ct17.assertEquals($$$cl1.String("empty", 5), l$39.item((6)));
            $$$ct17.assertEquals($$$cl1.String("last", 4), l$39.item((9)));
            l$39.remove((1));
            $$$ct17.assertEquals($$$cl1.String("[foo, bar, empty, empty, empty, empty, last, last, last]", 56), l$39.getString());
            $$$ct17.assertEquals((9), l$39.getSize());
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("first", 5))));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("foo", 3)));
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("stef", 4))));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("bar", 3)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("empty", 5)));
            $$$ct17.assertTrue(l$39.contains($$$cl1.String("last", 4)));
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("fu", 2))));
            $$$ct17.assertEquals($$$cl1.String("foo", 3), l$39.item((0)));
            $$$ct17.assertEquals($$$cl1.String("bar", 3), l$39.item((1)));
            $$$ct17.assertEquals($$$cl1.String("empty", 5), l$39.item((5)));
            $$$ct17.assertEquals($$$cl1.String("last", 4), l$39.item((8)));
            l$39.remove((8));
            $$$ct17.assertEquals((8), l$39.getSize());
            $$$ct17.assertEquals($$$cl1.String("[foo, bar, empty, empty, empty, empty, last, last]", 50), l$39.getString());
            l$39.add($$$cl1.String("end", 3));
            $$$ct17.assertEquals($$$cl1.String("[foo, bar, empty, empty, empty, empty, last, last, end]", 55), l$39.getString());
            $$$ct17.assertEquals((9), l$39.getSize());
            l$39.clear();
            $$$ct17.assertEquals($$$cl1.String("[]", 2), l$39.getString());
            $$$ct17.assertEquals((0), l$39.getSize());
            $$$ct17.assertTrue((!l$39.contains($$$cl1.String("foo", 3))));
        };
        function testSet() {
            var set$40 = $$$cc38.HashSet([
                {t: $$$cl1.String}
            ]);
            $$$ct17.assertEquals($$$cl1.String("()", 2), set$40.getString());
            $$$ct17.assertEquals((0), set$40.getSize());
            set$40.add($$$cl1.String("fu", 2));
            $$$ct17.assertEquals($$$cl1.String("(fu)", 4), set$40.getString());
            $$$ct17.assertTrue(set$40.contains($$$cl1.String("fu", 2)));
            $$$ct17.assertEquals((1), set$40.getSize());
            set$40.add($$$cl1.String("fu", 2));
            $$$ct17.assertEquals($$$cl1.String("(fu)", 4), set$40.getString());
            $$$ct17.assertTrue(set$40.contains($$$cl1.String("fu", 2)));
            $$$ct17.assertEquals((1), set$40.getSize());
            set$40.add($$$cl1.String("stef", 4));
            $$$ct17.assertTrue(set$40.contains($$$cl1.String("fu", 2)));
            $$$ct17.assertTrue(set$40.contains($$$cl1.String("stef", 4)));
            $$$ct17.assertEquals((2), set$40.getSize());
            $$$ct17.assertTrue((!set$40.contains($$$cl1.String("bar", 3))));
            set$40.clear();
            $$$ct17.assertEquals($$$cl1.String("()", 2), set$40.getString());
            $$$ct17.assertEquals((0), set$40.getSize());
            $$$ct17.assertTrue((!set$40.contains($$$cl1.String("fu", 2))));
        };
        function testSetRemove() {
            var set$41 = $$$cc38.HashSet([
                {t: $$$cl1.String}
            ]);
            set$41.add($$$cl1.String("a", 1));
            set$41.add($$$cl1.String("b", 1));
            $$$ct17.assertEquals((2), set$41.getSize());
            set$41.remove($$$cl1.String("a", 1));
            $$$ct17.assertEquals((1), set$41.getSize());
            $$$ct17.assertFalse(set$41.contains($$$cl1.String("a", 1)));
            $$$ct17.assertTrue(set$41.contains($$$cl1.String("b", 1)));
            set$41.remove($$$cl1.String("b", 1));
            $$$ct17.assertEquals((0), set$41.getSize());
            $$$ct17.assertFalse(set$41.contains($$$cl1.String("a", 1)));
            $$$ct17.assertFalse(set$41.contains($$$cl1.String("b", 1)));
        };
        function testSet2() {
            var set$42 = $$$cc38.HashSet([
                {t: $$$cl1.String}
            ]);
            set$42.add($$$cl1.String("gravatar_id", 11));
            set$42.add($$$cl1.String("url", 3));
            set$42.add($$$cl1.String("avatar_url", 10));
            set$42.add($$$cl1.String("id", 2));
            set$42.add($$$cl1.String("login", 5));
            $$$ct17.assertEquals((5), set$42.getSize());
        };
        function run() {
            $$$ct17.suite($$$cl1.String("ceylon.collection", 17), [$$$cl1.Entry($$$cl1.String("Set", 3), testSet, [
                {t: $$$cl1.String},
                {t: $$$cl1.Callable, a: [
                    {t: $$$cl1.Anything},
                    {t: $$$cl1.Empty}
                ]}
            ]), $$$cl1.Entry($$$cl1.String("Set2", 4), testSet2, [
                {t: $$$cl1.String},
                {t: $$$cl1.Callable, a: [
                    {t: $$$cl1.Anything},
                    {t: $$$cl1.Empty}
                ]}
            ]), $$$cl1.Entry($$$cl1.String("SetRemove", 9), testSetRemove, [
                {t: $$$cl1.String},
                {t: $$$cl1.Callable, a: [
                    {t: $$$cl1.Anything},
                    {t: $$$cl1.Empty}
                ]}
            ]), $$$cl1.Entry($$$cl1.String("Map", 3), testMap, [
                {t: $$$cl1.String},
                {t: $$$cl1.Callable, a: [
                    {t: $$$cl1.Anything},
                    {t: $$$cl1.Empty}
                ]}
            ]), $$$cl1.Entry($$$cl1.String("Map2", 4), testMap2, [
                {t: $$$cl1.String},
                {t: $$$cl1.Callable, a: [
                    {t: $$$cl1.Anything},
                    {t: $$$cl1.Empty}
                ]}
            ]), $$$cl1.Entry($$$cl1.String("MapRemove", 9), testMapRemove, [
                {t: $$$cl1.String},
                {t: $$$cl1.Callable, a: [
                    {t: $$$cl1.Anything},
                    {t: $$$cl1.Empty}
                ]}
            ]), $$$cl1.Entry($$$cl1.String("List", 4), testList, [
                {t: $$$cl1.String},
                {t: $$$cl1.Callable, a: [
                    {t: $$$cl1.Anything},
                    {t: $$$cl1.Empty}
                ]}
            ])].reifyCeylonType([
                {t: $$$cl1.Entry, a: [
                    {t: $$$cl1.String},
                    {t: $$$cl1.Callable, a: [
                        {t: $$$cl1.Anything},
                        {t: $$$cl1.Empty}
                    ]}
                ]},
                {t: $$$cl1.Null}
            ]));
        }

        exports.run = run;
        function testMap() {
            var map$43 = $$$cc38.HashMap([
                {t: $$$cl1.String},
                {t: $$$cl1.String}
            ]);
            $$$ct17.assertEquals($$$cl1.String("{}", 2), map$43.getString());
            $$$ct17.assertEquals((0), map$43.getSize());
            $$$ct17.assertTrue((!map$43.defines($$$cl1.String("fu", 2))), $$$cl1.String("a", 1));
            map$43.put($$$cl1.String("fu", 2), $$$cl1.String("bar", 3));
            $$$ct17.assertEquals($$$cl1.String("{fu->bar}", 9), map$43.getString());
            $$$ct17.assertEquals((1), map$43.getSize());
            $$$ct17.assertTrue(map$43.defines($$$cl1.String("fu", 2)), $$$cl1.String("b", 1));
            map$43.put($$$cl1.String("fu", 2), $$$cl1.String("gee", 3));
            $$$ct17.assertEquals($$$cl1.String("{fu->gee}", 9), map$43.getString());
            $$$ct17.assertEquals((1), map$43.getSize());
            $$$ct17.assertTrue(map$43.defines($$$cl1.String("fu", 2)), $$$cl1.String("c", 1));
            map$43.put($$$cl1.String("stef", 4), $$$cl1.String("epardaud", 8));
            $$$ct17.assertEquals((2), map$43.getSize());
            $$$ct17.assertTrue(map$43.defines($$$cl1.String("fu", 2)), $$$cl1.String("d", 1));
            $$$ct17.assertTrue(map$43.defines($$$cl1.String("stef", 4)), $$$cl1.String("e", 1));
            $$$ct17.assertEquals($$$cl1.String("epardaud", 8), map$43.item($$$cl1.String("stef", 4)));
            $$$ct17.assertEquals($$$cl1.String("gee", 3), map$43.item($$$cl1.String("fu", 2)));
            $$$ct17.assertEquals(null, map$43.item($$$cl1.String("bar", 3)));
            map$43.clear();
            $$$ct17.assertEquals($$$cl1.String("{}", 2), map$43.getString());
            $$$ct17.assertEquals((0), map$43.getSize());
            $$$ct17.assertTrue((!map$43.defines($$$cl1.String("fu", 2))), $$$cl1.String("f", 1));
        };
        function testMapRemove() {
            var map$44 = $$$cc38.HashMap([
                {t: $$$cl1.String},
                {t: $$$cl1.String}
            ]);
            map$44.put($$$cl1.String("a", 1), $$$cl1.String("b", 1));
            map$44.put($$$cl1.String("c", 1), $$$cl1.String("d", 1));
            $$$ct17.assertEquals((2), map$44.getSize());
            map$44.remove($$$cl1.String("a", 1));
            $$$ct17.assertEquals((1), map$44.getSize());
            $$$ct17.assertEquals($$$cl1.String("d", 1), map$44.item($$$cl1.String("c", 1)));
            $$$ct17.assertEquals(null, map$44.item($$$cl1.String("a", 1)));
            map$44.remove($$$cl1.String("c", 1));
            $$$ct17.assertEquals((0), map$44.getSize());
            $$$ct17.assertEquals(null, map$44.item($$$cl1.String("c", 1)));
            $$$ct17.assertEquals(null, map$44.item($$$cl1.String("a", 1)));
        };
        function testMap2() {
            var map$45 = $$$cc38.HashMap([
                {t: $$$cl1.String},
                { t: 'u', l: [
                    {t: $$$cl1.String},
                    {t: $$$cl1.Integer}
                ]}
            ]);
            map$45.put($$$cl1.String("gravatar_id", 11), $$$cl1.String("a38479e9dc888f68fb6911d4ce05d7cc", 32));
            map$45.put($$$cl1.String("url", 3), $$$cl1.String("https://api.github.com/users/ceylon", 35));
            map$45.put($$$cl1.String("avatar_url", 10), $$$cl1.String("https://secure.gravatar.com/avatar/a38479e9dc888f68fb6911d4ce05d7cc?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-orgs.png", 154));
            map$45.put($$$cl1.String("id", 2), (579261));
            map$45.put($$$cl1.String("login", 5), $$$cl1.String("ceylon", 6));
            $$$ct17.assertEquals((5), map$45.getSize());
            $$$ct17.assertEquals((5), map$45.getKeys().getSize());
            $$$ct17.assertEquals((5), map$45.getValues().getSize());
        };
        exports.$$metamodel$$ = $$metamodel$$;
    });
}(typeof define === 'function' && define.amd ? define : function (factory) {
            if (typeof exports !== 'undefined') {
                factory(require, exports, module);
            } else {
                throw 'no module loader';
            }
        }));
