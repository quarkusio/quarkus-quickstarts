// Polyfills friendly, therefore ES5 syntax

"use strict";

var isObject = require("../object/is");

var iteratorSymbol = Symbol.iterator;

if (!iteratorSymbol) {
	throw new Error("Cannot initialize iterator/is due to Symbol.iterator not being implemented");
}

module.exports = function (value/*, options*/) {
	if (!isObject(value)) {
		var options = arguments[1];
		if (!isObject(options) || !options.allowString || typeof value !== "string") return false;
	}
	try { return typeof value[iteratorSymbol] === "function"; }
	catch (error) { return false; }
};
