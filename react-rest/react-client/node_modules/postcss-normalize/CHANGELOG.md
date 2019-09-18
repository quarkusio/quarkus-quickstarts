# Changes to PostCSS Normalize

### 7.0.1 (August 24, 2018)

- Use postcss-browser-comments v2.0.0 (major, but a patch for this project)

PostCSS Browser Comments was using an older version of PostCSS, requiring 2
versions of PostCSS to use PostCSS Normalize. This update resolves that.

### 7.0.0 (August 24, 2018)

- Use normalize.css v9.0.1 (major)

### 6.0.0 (June 16, 2018)

- Use normalize.css v8 (major)
- Include normalize.css comments
- Include normalize.css sourcemap

### 5.0.0 (June 7, 2018)

- Update `browserslist` to v3.2.8 (major)
- Update: `postcss` to v6.0.22 (patch)
- Update: Node support from v4 to v6 (major)

### 4.0.0 (June 21, 2017)

- Require insertion point. Make old behavior an option.
- Allow multiple insertion points.

### 3.0.0 (May 26, 2017)

- Use jonathantneal/normalize.css v7
- Change the insertion point to `@import-normalize` to avoid confusion or
  collision with standard import behavior

### 2.1.0 (May 26, 2017)

- Support an insertion point via `@import postcss-normalize`
- Update tree creation to avoid AST issues with source

### 2.0.1 (May 21, 2017)

- Update tree creation to avoid AST issues with other PostCSS plugins

### 2.0.0 (May 17, 2017)

- Support PostCSS 6
- Support Node 4

### 1.0.0 (May 2, 2017)

- Initial version
