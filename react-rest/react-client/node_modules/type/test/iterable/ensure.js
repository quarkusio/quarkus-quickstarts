"use strict";

var assert         = require("chai").assert
  , ensureIterable = require("../../iterable/ensure");

describe("iterable/ensure", function () {
	it("Should return input value", function () {
		var value = [];
		assert.equal(ensureIterable(value), value);
	});
	it("Should allow strings with allowString option", function () {
		var value = "foo";
		assert.equal(ensureIterable(value, { allowString: true }), value);
	});
	it("Should crash on invalid value", function () {
		try {
			ensureIterable("foo");
			throw new Error("Unexpected");
		} catch (error) {
			assert.equal(error.name, "TypeError");
			assert(error.message.includes("is not an iterable value"));
		}
	});
});
