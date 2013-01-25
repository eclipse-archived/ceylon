(function (define) {
    define(function (require, exports, module) {
        var $$metamodel$$ = {"$mod-deps": ["ceylon.test\/0.5"], "$mod-name": "test.ceylon.test", "$mod-version": "0.5", "test.ceylon.test": {"$pkg-shared": "1", "run": {"$t": {"$md": "ceylon.language", "$pk": "ceylon.language", "$nm": "Anything"}, "$mt": "mthd", "$an": {"doc": ["\"Run the module `test.ceylon.test`.\""]}, "$nm": "run"}}};
        var $$$cl1 = require('ceylon/language/0.5/ceylon.language-0.5');
        var $$$ct17 = require('ceylon/test/0.5/ceylon.test-0.5');

        function run() {
            $$$ct17.suite($$$cl1.String("ceylon.test", 11));
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
