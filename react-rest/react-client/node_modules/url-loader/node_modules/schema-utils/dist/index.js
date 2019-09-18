"use strict";

const validate = require('./validate');

const validateError = require('./ValidationError');

module.exports = validate.default;
module.exports.ValidateError = validateError.default;