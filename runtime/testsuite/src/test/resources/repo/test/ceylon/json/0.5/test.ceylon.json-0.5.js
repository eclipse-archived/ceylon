(function (define) {
    define(function (require, exports, module) {
        var $$metamodel$$ = {"$mod-deps": ["ceylon.language\/0.5", "ceylon.test\/0.5", "ceylon.json\/0.5"], "$mod-name": "test.ceylon.json", "$mod-version": "0.5", "test.ceylon.json": {"testUsage": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$nm": "testUsage"}, "run": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$an": {"shared": []}, "$nm": "run"}, "testParse": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$nm": "testParse"}, "testPrint": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$nm": "testPrint"}}};
        var $$$cl1 = require('ceylon/language/0.5/ceylon.language-0.5');
        var $$$cj2 = require('ceylon/json/0.5/ceylon.json-0.5');

        function testUsage() {
            var json$3 = $$$cj2.parse($$$cl1.String("{\"svn_url\":\"https://github.com/ceylon/ceylon-compiler\",\"has_downloads\":true,\"homepage\":\"http://ceylon-lang.org\",\"mirror_url\":null,\"has_issues\":true,\"updated_at\":\"2012-04-11T10:20:59Z\",\"forks\":22,\"clone_url\":\"https://github.com/ceylon/ceylon-compiler.git\",\"ssh_url\":\"git@github.com:ceylon/ceylon-compiler.git\",\"html_url\":\"https://github.com/ceylon/ceylon-compiler\",\"language\":\"Java\",\"organization\":{\"gravatar_id\":\"a38479e9dc888f68fb6911d4ce05d7cc\",\"url\":\"https://api.github.com/users/ceylon\",\"avatar_url\":\"https://secure.gravatar.com/avatar/a38479e9dc888f68fb6911d4ce05d7cc?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-orgs.png\",\"id\":579261,\"login\":\"ceylon\"},\"has_wiki\":true,\"fork\":false,\"git_url\":\"git://github.com/ceylon/ceylon-compiler.git\",\"created_at\":\"2011-01-24T14:25:50Z\",\"url\":\"https://api.github.com/repos/ceylon/ceylon-compiler\",\"size\":2413,\"private\":false,\"open_issues\":81,\"description\":\"Ceylon compiler (ceylonc: Java backend), Ceylon documentation generator (ceylond) and Ceylon ant tasks.\",\"owner\":{\"gravatar_id\":\"a38479e9dc888f68fb6911d4ce05d7cc\",\"url\":\"https://api.github.com/users/ceylon\",\"avatar_url\":\"https://secure.gravatar.com/avatar/a38479e9dc888f68fb6911d4ce05d7cc?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-orgs.png\",\"id\":579261,\"login\":\"ceylon\"},\"name\":\"ceylon-compiler\",\"watchers\":74,\"pushed_at\":\"2012-04-11T07:43:33Z\",\"id\":1287859}", 1426));
            var it$4 = json$3.$sort(function (x$6, y$7) {
                return x$6.getKey().compare(y$7.getKey());
            }).getIterator();
            var item$5;
            while ((item$5 = it$4.next()) !== $$$cl1.getFinished()) {
                var key$8 = item$5.getKey();
                var item$9 = item$5.getItem();
                $$$cl1.print($$$cl1.StringBuilder().appendAll([key$8.getString(), $$$cl1.String(" -> ", 4), item$9.getString()]).getString());
            }
            if (json$3.getInteger($$$cl1.String("open_issues", 11)).compare((0)).equals($$$cl1.getLarger())) {
                $$$cl1.print($$$cl1.String("Has issues", 10));
            }
            var url$10;
            if ((url$10 = (opt$11 = json$3.getObjectOrNull($$$cl1.String("organization", 12)), $$$cl1.JsCallable(opt$11, opt$11 !== null ? opt$11.getString : null))($$$cl1.String("url", 3))) !== null) {
                $$$cl1.print($$$cl1.StringBuilder().appendAll([$$$cl1.String("Has url ", 8), url$10.getString()]).getString());
            }
            var opt$11;
            var results$12 = $$$cj2.parse($$$cl1.String("\n{\"results\" : [\n {\n  \"module\": \"ceylon.collection\",\n  \"versions\": [\n   \"0.3.0\", \n   \"0.3.3\" \n  ],\n  \"authors\": [\n   \"Stéphane Épardaud\" \n  ],\n  \"doc\": \"A module for collections\",\n  \"license\": \"Apache Software License\"\n },\n {\n  \"module\": \"ceylon.io\",\n  \"versions\": [\n   \"0.3.0\", \n   \"0.3.3\" \n  ],\n  \"authors\": [\n   \"Stéphane Épardaud\" \n  ],\n  \"doc\": \"A module for io\",\n  \"license\": \"Apache Software License\"\n }\n]}\n", 413));
            var it$13 = results$12.getArray($$$cl1.String("results", 7)).getObjects().getIterator();
            var mod$14;
            while ((mod$14 = it$13.next()) !== $$$cl1.getFinished()) {
                $$$cl1.print($$$cl1.StringBuilder().appendAll([$$$cl1.String("Module: ", 8), mod$14.getString($$$cl1.String("module", 6)).getString()]).getString());
                var it$15 = mod$14.getArray($$$cl1.String("versions", 8)).getStrings().getIterator();
                var version$16;
                while ((version$16 = it$15.next()) !== $$$cl1.getFinished()) {
                    $$$cl1.print($$$cl1.StringBuilder().appendAll([$$$cl1.String(" Version: ", 10), version$16.getString()]).getString());
                }
            }
        };
        var $$$ct17 = require('ceylon/test/0.5/ceylon.test-0.5');

        function testPrint() {
            var o1$18 = ($$$cj2.Object($$$cl1.empty));
            $$$ct17.assertEquals($$$cl1.String("{}", 2), o1$18.getString());
            var o2$19 = $$$cj2.Object([$$$cl1.Entry($$$cl1.String("s", 1), $$$cl1.String("asd", 3), [
                {t: $$$cl1.String},
                {t: $$$cl1.String}
            ]), $$$cl1.Entry($$$cl1.String("i", 1), (12), [
                {t: $$$cl1.String},
                {t: $$$cl1.Integer}
            ]), $$$cl1.Entry($$$cl1.String("f", 1), $$$cl1.Float(12.34), [
                {t: $$$cl1.String},
                {t: $$$cl1.Float}
            ]), $$$cl1.Entry($$$cl1.String("true", 4), true, [
                {t: $$$cl1.String},
                {t: $$$cl1.$true}
            ]), $$$cl1.Entry($$$cl1.String("false", 5), false, [
                {t: $$$cl1.String},
                {t: $$$cl1.$false}
            ]), $$$cl1.Entry($$$cl1.String("null", 4), $$$cj2.getNil(), [
                {t: $$$cl1.String},
                {t: $$$cj2.nil}
            ]), $$$cl1.Entry($$$cl1.String("o", 1), (values$20 = $$$cl1.Tuple($$$cl1.Entry($$$cl1.String("i", 1), (2), [
                {t: $$$cl1.String},
                {t: $$$cl1.Integer}
            ]), $$$cl1.empty, [
                {t: $$$cl1.Entry, a: [
                    {t: $$$cl1.String},
                    {t: $$$cl1.Integer}
                ]},
                {t: $$$cl1.Entry, a: [
                    {t: $$$cl1.String},
                    {t: $$$cl1.Integer}
                ]},
                {t: $$$cl1.Empty}
            ]), $$$cj2.Object(values$20)), [
                {t: $$$cl1.String},
                {t: $$$cj2.Object}
            ]), $$$cl1.Entry($$$cl1.String("a", 1), (values$21 = $$$cl1.Tuple($$$cl1.String("a", 1), $$$cl1.Tuple((2), $$$cl1.Tuple(true, $$$cl1.empty, [
                {t: $$$cl1.Boolean},
                {t: $$$cl1.Boolean},
                {t: $$$cl1.Empty}
            ]), [
                { t: 'u', l: [
                    {t: $$$cl1.Integer},
                    {t: $$$cl1.Boolean}
                ]},
                {t: $$$cl1.Integer},
                {t: $$$cl1.Tuple, a: [
                    {t: $$$cl1.Boolean},
                    {t: $$$cl1.Boolean},
                    {t: $$$cl1.Empty}
                ]}
            ]), [
                { t: 'u', l: [
                    {t: $$$cl1.String},
                    {t: $$$cl1.Integer},
                    {t: $$$cl1.Boolean}
                ]},
                {t: $$$cl1.String},
                {t: $$$cl1.Tuple, a: [
                    { t: 'u', l: [
                        {t: $$$cl1.Integer},
                        {t: $$$cl1.Boolean}
                    ]},
                    {t: $$$cl1.Integer},
                    {t: $$$cl1.Tuple, a: [
                        {t: $$$cl1.Boolean},
                        {t: $$$cl1.Boolean},
                        {t: $$$cl1.Empty}
                    ]}
                ]}
            ]), $$$cj2.Array(values$21)), [
                {t: $$$cl1.String},
                {t: $$$cj2.Array}
            ])].reifyCeylonType([
                {t: $$$cl1.Entry, a: [
                    {t: $$$cl1.String},
                    {t: $$$cl1.String}
                ]},
                {t: $$$cl1.Null}
            ]));
            var values$20, values$21;
            $$$ct17.assertEquals($$$cl1.String("{\"a\":[\"a\",2,true],\"false\":false,\"s\":\"asd\",\"f\":12.34,\"null\":null,\"i\":12,\"true\":true,\"o\":{\"i\":2}}", 95), o2$19.getString());
        };
        function testParse() {
            var o1$22 = $$$cj2.parse($$$cl1.String("{}", 2));
            $$$ct17.assertEquals((0), o1$22.getSize());
            var o2$23 = $$$cj2.parse($$$cl1.String("{\"foo\": \"bar\"}", 14));
            $$$ct17.assertEquals((1), o2$23.getSize());
            $$$ct17.assertEquals($$$cl1.String("bar", 3), o2$23.item($$$cl1.String("foo", 3)));
            var o3$24 = $$$cj2.parse($$$cl1.String("{\"s\": \"bar\", \"t\": true, \"f\": false, \"n\": null}", 46));
            $$$ct17.assertEquals((4), o3$24.getSize());
            $$$ct17.assertEquals($$$cl1.String("bar", 3), o3$24.item($$$cl1.String("s", 1)));
            $$$ct17.assertEquals(true, o3$24.item($$$cl1.String("t", 1)));
            $$$ct17.assertEquals(false, o3$24.item($$$cl1.String("f", 1)));
            $$$ct17.assertEquals($$$cj2.getNil(), o3$24.item($$$cl1.String("n", 1)));
            var o4$25 = $$$cj2.parse($$$cl1.String("{\"i\": 12, \"f\": 12.34, \"ie\": 12e10, \"fe\": 12.34e10}", 50));
            $$$ct17.assertEquals((4), o4$25.getSize());
            $$$ct17.assertEquals((12), o4$25.item($$$cl1.String("i", 1)));
            $$$ct17.assertEquals($$$cl1.Float(12.34), o4$25.item($$$cl1.String("f", 1)));
            $$$ct17.assertEquals($$$cl1.Float(12.0e10).getInteger(), o4$25.item($$$cl1.String("ie", 2)));
            $$$ct17.assertEquals($$$cl1.Float(12.34e10), o4$25.item($$$cl1.String("fe", 2)));
            var o5$26 = $$$cj2.parse($$$cl1.String("{\"i\": -12, \"f\": -12.34, \"ie\": -12e10, \"fe\": -12.34e10}", 54));
            $$$ct17.assertEquals((4), o5$26.getSize());
            $$$ct17.assertEquals((-(12)), o5$26.item($$$cl1.String("i", 1)));
            $$$ct17.assertEquals($$$cl1.Float(12.34).getNegativeValue(), o5$26.item($$$cl1.String("f", 1)));
            $$$ct17.assertEquals((-$$$cl1.Float(12.0e10).getInteger()), o5$26.item($$$cl1.String("ie", 2)));
            $$$ct17.assertEquals($$$cl1.Float(12.34e10).getNegativeValue(), o5$26.item($$$cl1.String("fe", 2)));
            var o6$27 = $$$cj2.parse($$$cl1.String("{\"ie\": 12E10, \"fe\": 12.34E10}", 29));
            $$$ct17.assertEquals((2), o6$27.getSize());
            $$$ct17.assertEquals($$$cl1.Float(12.0e10).getInteger(), o6$27.item($$$cl1.String("ie", 2)));
            $$$ct17.assertEquals($$$cl1.Float(12.34e10), o6$27.item($$$cl1.String("fe", 2)));
            var o7$28 = $$$cj2.parse($$$cl1.String("{\"ie\": 12e+10, \"fe\": 12.34e+10}", 31));
            $$$ct17.assertEquals((2), o7$28.getSize());
            $$$ct17.assertEquals($$$cl1.Float(12.0e10).getInteger(), o7$28.item($$$cl1.String("ie", 2)));
            $$$ct17.assertEquals($$$cl1.Float(12.34e10), o7$28.item($$$cl1.String("fe", 2)));
            var o8$29 = $$$cj2.parse($$$cl1.String("{\"ie\": 12e-10, \"fe\": 12.34e-10}", 31));
            $$$ct17.assertEquals((2), o8$29.getSize());
            $$$ct17.assertEquals($$$cl1.Float(12.0e-10), o8$29.item($$$cl1.String("ie", 2)));
            $$$ct17.assertEquals($$$cl1.Float(12.34e-10), o8$29.item($$$cl1.String("fe", 2)));
            var o9$30 = $$$cj2.parse($$$cl1.String("{\"s\": \"escapes \\\\ \\\" \\/ \\b \\f \\t \\n \\r \\u0053 \\u3042\"}", 54));
            $$$ct17.assertEquals((1), o9$30.getSize());
            $$$ct17.assertEquals($$$cl1.String("escapes \\ \" / \b \f \t \n \r S あ", 27), o9$30.item($$$cl1.String("s", 1)));
            var o10$31 = $$$cj2.parse($$$cl1.String("{\"obj\": {\"gee\": \"bar\"}}", 23));
            $$$ct17.assertEquals((1), o10$31.getSize());
            var obj$32;
            if ($$$cl1.isOfType((obj$32 = o10$31.item($$$cl1.String("obj", 3))), {t: $$$cj2.Object})) {
                $$$ct17.assertEquals($$$cl1.String("bar", 3), obj$32.item($$$cl1.String("gee", 3)));
            } else {
                $$$ct17.fail();
            }
            var o11$33 = $$$cj2.parse($$$cl1.String("{\"arr\": [1, 2, 3]}", 18));
            $$$ct17.assertEquals((1), o11$33.getSize());
            var arr$34;
            if ($$$cl1.isOfType((arr$34 = o11$33.item($$$cl1.String("arr", 3))), {t: $$$cj2.Array})) {
                $$$ct17.assertEquals((3), arr$34.getSize());
                $$$ct17.assertEquals((1), arr$34.item((0)));
                $$$ct17.assertEquals((2), arr$34.item((1)));
                $$$ct17.assertEquals((3), arr$34.item((2)));
            } else {
                $$$ct17.fail();
            }
            var o12$35 = $$$cj2.parse($$$cl1.String("{\"svn_url\":\"https://github.com/ceylon/ceylon-compiler\",\"has_downloads\":true,\"homepage\":\"http://ceylon-lang.org\",\"mirror_url\":null,\"has_issues\":true,\"updated_at\":\"2012-04-11T10:20:59Z\",\"forks\":22,\"clone_url\":\"https://github.com/ceylon/ceylon-compiler.git\",\"ssh_url\":\"git@github.com:ceylon/ceylon-compiler.git\",\"html_url\":\"https://github.com/ceylon/ceylon-compiler\",\"language\":\"Java\",\"organization\":{\"gravatar_id\":\"a38479e9dc888f68fb6911d4ce05d7cc\",\"url\":\"https://api.github.com/users/ceylon\",\"avatar_url\":\"https://secure.gravatar.com/avatar/a38479e9dc888f68fb6911d4ce05d7cc?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-orgs.png\",\"id\":579261,\"login\":\"ceylon\"},\"has_wiki\":true,\"fork\":false,\"git_url\":\"git://github.com/ceylon/ceylon-compiler.git\",\"created_at\":\"2011-01-24T14:25:50Z\",\"url\":\"https://api.github.com/repos/ceylon/ceylon-compiler\",\"size\":2413,\"private\":false,\"open_issues\":81,\"description\":\"Ceylon compiler (ceylonc: Java backend), Ceylon documentation generator (ceylond) and Ceylon ant tasks.\",\"owner\":{\"gravatar_id\":\"a38479e9dc888f68fb6911d4ce05d7cc\",\"url\":\"https://api.github.com/users/ceylon\",\"avatar_url\":\"https://secure.gravatar.com/avatar/a38479e9dc888f68fb6911d4ce05d7cc?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-orgs.png\",\"id\":579261,\"login\":\"ceylon\"},\"name\":\"ceylon-compiler\",\"watchers\":74,\"pushed_at\":\"2012-04-11T07:43:33Z\",\"id\":1287859}", 1426));
            $$$ct17.assertEquals((26), o12$35.getSize());
            $$$ct17.assertEquals($$$cl1.String("https://github.com/ceylon/ceylon-compiler.git", 45), o12$35.item($$$cl1.String("clone_url", 9)));
            $$$ct17.assertEquals($$$cl1.String("2011-01-24T14:25:50Z", 20), o12$35.item($$$cl1.String("created_at", 10)));
            $$$ct17.assertEquals($$$cl1.String("Ceylon compiler (ceylonc: Java backend), Ceylon documentation generator (ceylond) and Ceylon ant tasks.", 103), o12$35.item($$$cl1.String("description", 11)));
            $$$ct17.assertEquals(false, o12$35.item($$$cl1.String("fork", 4)));
            $$$ct17.assertEquals((22), o12$35.item($$$cl1.String("forks", 5)));
            $$$ct17.assertEquals($$$cl1.String("git://github.com/ceylon/ceylon-compiler.git", 43), o12$35.item($$$cl1.String("git_url", 7)));
            $$$ct17.assertEquals(true, o12$35.item($$$cl1.String("has_downloads", 13)));
            $$$ct17.assertEquals(true, o12$35.item($$$cl1.String("has_issues", 10)));
            $$$ct17.assertEquals(true, o12$35.item($$$cl1.String("has_wiki", 8)));
            $$$ct17.assertEquals($$$cl1.String("http://ceylon-lang.org", 22), o12$35.item($$$cl1.String("homepage", 8)));
            $$$ct17.assertEquals($$$cl1.String("https://github.com/ceylon/ceylon-compiler", 41), o12$35.item($$$cl1.String("html_url", 8)));
            $$$ct17.assertEquals((1287859), o12$35.item($$$cl1.String("id", 2)));
            $$$ct17.assertEquals($$$cl1.String("Java", 4), o12$35.item($$$cl1.String("language", 8)));
            $$$ct17.assertEquals($$$cj2.getNil(), o12$35.item($$$cl1.String("mirror_url", 10)));
            $$$ct17.assertEquals($$$cl1.String("ceylon-compiler", 15), o12$35.item($$$cl1.String("name", 4)));
            $$$ct17.assertEquals((81), o12$35.item($$$cl1.String("open_issues", 11)));
            var org$36;
            if ($$$cl1.isOfType((org$36 = o12$35.item($$$cl1.String("organization", 12))), {t: $$$cj2.Object})) {
                $$$ct17.assertEquals((5), org$36.getSize());
                $$$ct17.assertEquals($$$cl1.String("https://secure.gravatar.com/avatar/a38479e9dc888f68fb6911d4ce05d7cc?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-orgs.png", 154), org$36.item($$$cl1.String("avatar_url", 10)));
                $$$ct17.assertEquals($$$cl1.String("a38479e9dc888f68fb6911d4ce05d7cc", 32), org$36.item($$$cl1.String("gravatar_id", 11)));
                $$$ct17.assertEquals((579261), org$36.item($$$cl1.String("id", 2)));
                $$$ct17.assertEquals($$$cl1.String("ceylon", 6), org$36.item($$$cl1.String("login", 5)));
                $$$ct17.assertEquals($$$cl1.String("https://api.github.com/users/ceylon", 35), org$36.item($$$cl1.String("url", 3)));
            }
            var owner$37;
            if ($$$cl1.isOfType((owner$37 = o12$35.item($$$cl1.String("owner", 5))), {t: $$$cj2.Object})) {
                $$$ct17.assertEquals((5), owner$37.getSize());
                $$$ct17.assertEquals($$$cl1.String("https://secure.gravatar.com/avatar/a38479e9dc888f68fb6911d4ce05d7cc?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-orgs.png", 154), owner$37.item($$$cl1.String("avatar_url", 10)));
                $$$ct17.assertEquals($$$cl1.String("a38479e9dc888f68fb6911d4ce05d7cc", 32), owner$37.item($$$cl1.String("gravatar_id", 11)));
                $$$ct17.assertEquals((579261), owner$37.item($$$cl1.String("id", 2)));
                $$$ct17.assertEquals($$$cl1.String("ceylon", 6), owner$37.item($$$cl1.String("login", 5)));
                $$$ct17.assertEquals($$$cl1.String("https://api.github.com/users/ceylon", 35), owner$37.item($$$cl1.String("url", 3)));
            }
            $$$ct17.assertEquals(false, o12$35.item($$$cl1.String("private", 7)));
            $$$ct17.assertEquals($$$cl1.String("2012-04-11T07:43:33Z", 20), o12$35.item($$$cl1.String("pushed_at", 9)));
            $$$ct17.assertEquals((2413), o12$35.item($$$cl1.String("size", 4)));
            $$$ct17.assertEquals($$$cl1.String("git@github.com:ceylon/ceylon-compiler.git", 41), o12$35.item($$$cl1.String("ssh_url", 7)));
            $$$ct17.assertEquals($$$cl1.String("https://github.com/ceylon/ceylon-compiler", 41), o12$35.item($$$cl1.String("svn_url", 7)));
            $$$ct17.assertEquals($$$cl1.String("2012-04-11T10:20:59Z", 20), o12$35.item($$$cl1.String("updated_at", 10)));
            $$$ct17.assertEquals($$$cl1.String("https://api.github.com/repos/ceylon/ceylon-compiler", 51), o12$35.item($$$cl1.String("url", 3)));
            $$$ct17.assertEquals((74), o12$35.item($$$cl1.String("watchers", 8)));
        };
        function run() {
            $$$ct17.suite($$$cl1.String("ceylon.collection", 17), [$$$cl1.Entry($$$cl1.String("Parse", 5), testParse, [
                {t: $$$cl1.String},
                {t: $$$cl1.Callable, a: [
                    {t: $$$cl1.Anything},
                    {t: $$$cl1.Empty}
                ]}
            ]), $$$cl1.Entry($$$cl1.String("Print", 5), testPrint, [
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
        exports.$$metamodel$$ = $$metamodel$$;
    });
}(typeof define === 'function' && define.amd ? define : function (factory) {
            if (typeof exports !== 'undefined') {
                factory(require, exports, module);
            } else {
                throw 'no module loader';
            }
        }));
