"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var jest_mock_1 = require("jest-mock");
var jest_util_1 = require("jest-util");
var jsdom_1 = require("jsdom");
var JSDOMEnvironment = /** @class */ (function () {
    function JSDOMEnvironment(config, options) {
        if (options === void 0) { options = {}; }
        this.dom = new jsdom_1.JSDOM("<!DOCTYPE html>", Object.assign({
            pretendToBeVisual: true,
            runScripts: "dangerously",
            url: config.testURL,
            virtualConsole: new jsdom_1.VirtualConsole().sendTo(options.console || console),
            resources: options.resources,
        }, config.testEnvironmentOptions));
        this.global = this.dom.window.document.defaultView;
        // Node's error-message stack size is limited at 10, but it's pretty useful
        // to see more than that when a test fails.
        this.global.Error.stackTraceLimit = 100;
        jest_util_1.installCommonGlobals(this.global, config.globals);
        // Report uncaught errors.
        this.errorEventListener = function (event) {
            if (userErrorListenerCount === 0 && event.error) {
                process.emit("uncaughtException", event.error);
            }
        };
        this.global.addEventListener("error", this.errorEventListener);
        // However, don't report them as uncaught if the user listens to 'error' event.
        // In that case, we assume the might have custom error handling logic.
        var originalAddListener = this.global.addEventListener;
        var originalRemoveListener = this.global.removeEventListener;
        var userErrorListenerCount = 0;
        this.global.addEventListener = function (name) {
            if (name === "error") {
                userErrorListenerCount++;
            }
            return originalAddListener.apply(this, arguments);
        };
        this.global.removeEventListener = function (name) {
            if (name === "error") {
                userErrorListenerCount--;
            }
            return originalRemoveListener.apply(this, arguments);
        };
        this.moduleMocker = new jest_mock_1.ModuleMocker(this.global);
        var timerConfig = {
            idToRef: function (id) { return id; },
            refToId: function (ref) { return ref; },
        };
        this.fakeTimers = new jest_util_1.FakeTimers({
            config: config,
            global: this.global,
            moduleMocker: this.moduleMocker,
            timerConfig: timerConfig,
        });
    }
    JSDOMEnvironment.prototype.setup = function () {
        return Promise.resolve();
    };
    JSDOMEnvironment.prototype.teardown = function () {
        if (this.fakeTimers) {
            this.fakeTimers.dispose();
        }
        if (this.global) {
            if (this.errorEventListener) {
                this.global.removeEventListener("error", this.errorEventListener);
            }
            // Dispose "document" to prevent "load" event from triggering.
            Object.defineProperty(this.global, "document", { value: undefined });
            this.global.close();
        }
        this.errorEventListener = undefined;
        this.global = undefined;
        this.dom = undefined;
        this.fakeTimers = undefined;
        return Promise.resolve();
    };
    JSDOMEnvironment.prototype.runScript = function (script) {
        if (this.dom) {
            return this.dom.runVMScript(script);
        }
        return null;
    };
    return JSDOMEnvironment;
}());
module.exports = JSDOMEnvironment;
