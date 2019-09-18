'use strict';

function _interopDefault (ex) { return (ex && (typeof ex === 'object') && 'default' in ex) ? ex['default'] : ex; }

var postcss = _interopDefault(require('postcss'));
var postcssBrowserComments = _interopDefault(require('postcss-browser-comments'));
var fs = _interopDefault(require('fs'));

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) {
  try {
    var info = gen[key](arg);
    var value = info.value;
  } catch (error) {
    reject(error);
    return;
  }

  if (info.done) {
    resolve(value);
  } else {
    Promise.resolve(value).then(_next, _throw);
  }
}

function _asyncToGenerator(fn) {
  return function () {
    var self = this,
        args = arguments;
    return new Promise(function (resolve, reject) {
      var gen = fn.apply(self, args);

      function _next(value) {
        asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value);
      }

      function _throw(err) {
        asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err);
      }

      _next(undefined);
    });
  };
}

var index = postcss.plugin('postcss-normalize', opts => {
  const parsedNormalize = parseNormalize(opts);
  return (
    /*#__PURE__*/
    function () {
      var _ref = _asyncToGenerator(function* (root) {
        const normalizeRoot = yield parsedNormalize; // use @import postcss-normalize insertion point

        root.walkAtRules('import-normalize', atrule => {
          if (opts && opts.allowDuplicates) {
            // use any insertion point
            atrule.replaceWith(normalizeRoot);
          } else if (normalizeRoot.parent) {
            // remove duplicate insertions
            atrule.remove();
          } else {
            // use the first insertion point
            atrule.replaceWith(normalizeRoot);
          }
        });

        if (opts && opts.forceImport && !normalizeRoot.parent) {
          // prepend required normalize rules
          root.prepend(normalizeRoot);
        }
      });

      return function (_x) {
        return _ref.apply(this, arguments);
      };
    }()
  );
});

function parseNormalize(opts) {
  const from = require.resolve('@csstools/normalize.css');

  const postcssBrowserCommentsParser = postcssBrowserComments(opts);
  return new Promise((resolve, reject) => fs.readFile(from, 'utf8', (err, res) => {
    if (err) {
      reject(err);
    } else {
      resolve(res);
    }
  })).then(css => postcss.parse(css, {
    from
  })).then(root => {
    postcssBrowserCommentsParser(root);
    return root;
  });
}

module.exports = index;
