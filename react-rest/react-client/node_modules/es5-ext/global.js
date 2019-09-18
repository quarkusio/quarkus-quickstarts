module.exports = (function () {
	if (this) return this;

	// Unexpected strict mode (may happen if e.g. bundled into ESM module) be nice

	// Thanks @mathiasbynens -> https://mathiasbynens.be/notes/globalthis
	// In all ES5 engines global object inherits from Object.prototype
	// (if you approached one that doesn't please report)
	Object.defineProperty(Object.prototype, "__global__", {
		get: function () { return this; },
		configurable: true
	});
	try { return __global__; }
	finally { delete Object.prototype.__global__; }
})();
