# normalize.css [<img src="https://camo.githubusercontent.com/0d1347e7b4ede3d714150c863a44c932f3c4d84e/68747470733a2f2f6e65636f6c61732e6769746875622e696f2f6e6f726d616c697a652e6373732f6c6f676f2e737667" alt="normalize" width="90" height="90" align="right">][normalize.css]

[normalize.css] is a CSS library that provides consistent, cross-browser
default styling of HTML elements.

## Usage

```sh
npm install --save @csstools/normalize.css
```

### Usage in npm and webpack

Import [normalize.css] in CSS:

```css
@import '~@csstools/normalize.css';
```

Alternatively, import [normalize.css] in JS:

```js
import '@csstools/normalize.css';
```

In `webpack.config.js`, be sure to use the appropriate loaders:

```js
module.exports = {
  module: {
    rules: [
      {
        test: /\.css$/,
        use: [ 'style-loader', 'css-loader' ]
      }
    ]
  }
}
```

**Download**

See https://csstools.github.io/normalize.css/latest/normalize.css

## What does it do?

* Normalizes styles for a wide range of elements.
* Corrects bugs and common browser inconsistencies.
* Explains what code does using detailed comments.

## Browser support

* Chrome (last 3)
* Edge (last 3)
* Firefox (last 3)
* Firefox ESR
* Opera (last 3)
* Safari (last 3)
* iOS Safari (last 2)
* Internet Explorer 9+

## Contributing

Please read the [contribution guidelines](CONTRIBUTING.md) in order to make the
contribution process easy and effective for everyone involved.

## Similar Projects

- [opinionate.css](https://github.com/adamgruber/opinionate.css) - A supplement to normalize.css with opinionated rules
- [sanitize.css](https://github.com/csstools/sanitize.css) - An alternative to normalize.css, adhering to common developer expectations and preferences

## Acknowledgements

normalize.css is a project by [Jonathan Neal](https://github.com/jonathantneal),
co-created with [Nicolas Gallagher](https://github.com/necolas).

[normalize.css]: https://github.com/csstools/normalize.css
