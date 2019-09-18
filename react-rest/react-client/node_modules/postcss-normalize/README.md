# PostCSS Normalize [<img src="https://postcss.github.io/postcss/logo.svg" alt="PostCSS" width="90" height="90" align="right">][postcss]

[![NPM Version][npm-img]][npm-url]
[![Build Status][cli-img]][cli-url]
[![Support Chat][git-img]][git-url]

[PostCSS Normalize] lets you use the parts of [normalize.css] you need from
your [browserslist].

Use `@import-normalize` to determine where [normalize.css] rules should be
included. Duplicate `@import-normalize` rules will be removed. See all the
[Options] for more information.

```pcss
@import-normalize;
```

Results when [browserslist] is `last 3 versions`:

```css
/**
 * Add the correct display in IE 9-.
 */

audio,
video {
  display: inline-block;
}

/**
 * Remove the border on images inside links in IE 10-.
 */

img {
  border-style: none;
}
```

Results when [browserslist] is `last 2 versions`:

```css
/**
 * Remove the border on images inside links in IE 10-.
 */

img {
  border-style: none;
}
```

---

[PostCSS Normalize] uses the non-opinionated version of [normalize.css].

---

## Usage

Add [PostCSS Normalize] to your project:

```bash
npm install postcss-normalize --save-dev
```

Add a [browserslist] entry in `package.json`:

```json
{
  "browserslist": "last 2 versions"
}
```

Use [PostCSS Normalize] to process your CSS:

```js
import postcssNormalize from 'postcss-normalize';

postcssNormalize.process(YOUR_CSS /*, processOptions, pluginOptions */);
```

Or use it as a [PostCSS] plugin:

```js
import postcss from 'postcss';
import postcssNormalize from 'postcss-normalize';

postcss([
  postcssNormalize(/* pluginOptions */)
]).process(YOUR_CSS /*, processOptions */);
```

[PostCSS Normalize] runs in all Node environments, with special instructions for:

| [Node](INSTALL.md#node) | [PostCSS CLI](INSTALL.md#postcss-cli) | [Webpack](INSTALL.md#webpack) | [Create React App](INSTALL.md#create-react-app) | [Gulp](INSTALL.md#gulp) | [Grunt](INSTALL.md#grunt) |
| --- | --- | --- | --- | --- | --- |

## Options

### allowDuplicates

Allows you to insert multiple, duplicate insertions of [normalize.css] rules.
The default is `false`.

```js
postcssNormalize({
  allowDuplicates: true
});
```

### browsers

Allows you to override of the project’s [browserslist] for [PostCSS Normalize].
The default is `false`.

```js
postcssNormalize({
  browsers: 'last 2 versions'
});
```

### forceImport

Allows you to force an insertion of [normalize.css] rules at the beginning of
the CSS file if no insertion point is specified. The default is `false`.

```js
postcssNormalize({
  forceImport: true
});
```

[cli-img]: https://img.shields.io/travis/csstools/postcss-normalize.svg
[cli-url]: https://travis-ci.org/csstools/postcss-normalize
[git-img]: https://img.shields.io/badge/support-chat-blue.svg
[git-url]: https://gitter.im/postcss/postcss
[npm-img]: https://img.shields.io/npm/v/postcss-normalize.svg
[npm-url]: https://www.npmjs.com/package/postcss-normalize

[browserslist]: http://browserl.ist/
[normalize.css]: https://github.com/csstools/normalize.css
[Options]: #options
[PostCSS]: https://github.com/postcss/postcss
[PostCSS Normalize]: https://github.com/csstools/postcss-normalize
