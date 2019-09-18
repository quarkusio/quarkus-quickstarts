/**
Performs a deep comparison of the two values including support for circular dependencies, prototype, and enumerable properties.

@param obj - The value being compared.
@param ref - The reference value used for comparison.

@return true when the two values are equal, otherwise false.
 */
export function deepEqual(obj: any, ref: any, options?: deepEqual.Options): boolean;

declare namespace deepEqual {

    interface Options {

        /**
        Allow partial match.

        @default false
        */
        readonly part?: boolean;

        /**
        Compare the objects' prototypes.

        @default true
        */
        readonly prototype?: boolean;

        /**
        Compare symbol properties.

        @default false
        */
        readonly symbols?: boolean;
    }
}


/**
Clone any value, object, or array.

@param obj - The value being cloned.
@param options - The second number to add.

@returns A deep clone of `obj`.
*/
export function clone<T>(obj: T, options?: clone.Options): T;

declare namespace clone {

    interface Options {

        /**
        Clone the object's prototype.

        @default true
        */
        readonly prototype?: boolean;

        /**
        Include symbol properties.

        @default false
        */
        readonly symbols?: boolean;
    }
}


/**
Merge all the properties of source into target.

@param target - The object being modified.
@param source - The object used to copy properties from.
@param isNullOverride - When true, null value from `source` overrides existing value in `target`. Defaults to true.
@param isMergeArrays - When true, array value from `source` is merged with the existing value in `target`. Defaults to true.

@returns The `target` object.
*/
export function merge<T1 extends object, T2 extends object>(target: T1, source: T2, isNullOverride?: boolean, isMergeArrays?: boolean): T1 & T2;


/**
Apply options to a copy of the defaults.

@param defaults - An object with the default values to use of `options` does not contain the same keys.
@param options - The options used to override the `defaults`.
@param isNullOverride - When true, null value from `options` overrides existing value in `defaults`. Defaults to false.

@returns A copy of `defaults` with `options` keys overriding any conflicts.
*/
export function applyToDefaults<T extends object>(defaults: Partial<T>, options: Partial<T> | boolean | null, isNullOverride?: boolean): Partial<T>;


/**
Clone an object or array with specific keys shallowly cloned.

@param obj - The object being cloned.
@param keys - An array of string keys indicating which `obj` properties to shallow copy instead of deep clone. Use dot-notation to indicate nested keys (e.g. "a.b").

@returns A deep clone of `obj` with the requested `keys` shallowly cloned.
*/
export function cloneWithShallow<T>(obj: T, keys: string[], options?: clone.Options): T;


/**
Apply options to a copy of the defaults with specific keys shallowly cloned.

@param defaults - An object with the default values to use of `options` does not contain the same keys.
@param options - The options used to override the `defaults`.
@param keys - An array of string keys indicating which `options` properties to shallow copy instead of deep clone. Use dot-notation to indicate nested keys (e.g. "a.b").

@returns A copy of `defaults` with `options` keys overriding any conflicts.
*/
export function applyToDefaultsWithShallow<T extends object>(defaults: Partial<T>, options: Partial<T> | boolean | null, keys: string[]): Partial<T>;


/**
Find the common unique items in two arrays.

@param array1 - The first array to compare.
@param array2 - The second array to compare.
@param justFirst - If true, return the first overlapping value. Defaults to false.

@return - An array of the common items. If `justFirst` is true, returns the first common item.
*/
export function intersect<T1, T2>(array1: intersect.Array<T1>, array2: intersect.Array<T2>, justFirst?: false): Array<T1 | T2>;
export function intersect<T1, T2>(array1: intersect.Array<T1>, array2: intersect.Array<T2>, justFirst: true): T1 | T2;

declare namespace intersect {

    type Array<T> = ArrayLike<T> | Set<T> | null;
}


/**
Checks if the reference value contains the provided values.

@param ref - The reference string, array, or object.
@param values - A single or array of values to find within `ref`. If `ref` is an object, `values` can be a key name, an array of key names, or an object with key-value pairs to compare.

@return true if the value contains the provided values, otherwise false.
*/
export function contain(ref: string, values: string | string[], options?: contain.Options): boolean;
export function contain(ref: any[], values: any, options?: contain.Options): boolean;
export function contain(ref: object, values: string | string[] | object, options?: contain.Options): boolean;

declare namespace contain {

    interface Options {

        /**
        Perform a deep comparison.

        @default false
        */
        readonly deep?: boolean;

        /**
        Allow only one occurrence of each value.

        @default false
        */
        readonly once?: boolean;

        /**
        Allow only values explicitly listed.

        @default false
        */
        readonly only?: boolean;

        /**
        Allow partial match.

        @default false
        */
        readonly part?: boolean;

        /**
        Include symbol properties.

        @default false
        */
        readonly symbols?: boolean;
    }
}


/**
Flatten an array with sub arrays

@param array - an array of items or other arrays to flatten.
@param target - if provided, an array to shallow copy the flattened `array` items to

@return a flat array of the provided values (appended to `target` is provided).
*/
export function flatten<T>(array: ArrayLike<T | ReadonlyArray<T>>, target?: ArrayLike<T | ReadonlyArray<T>>): T[];


/**
Convert an object key chain string to reference.

@param obj - the object from which to look up the value.
@param chain - the string path of the requested value. The chain string is split into key names using `options.separator`, or an array containing each individual key name. A chain including negative numbers will work like negative indices on an array.

@return The value referenced by the chain if found, otherwise undefined. If chain is null, undefined, or false, the object itself will be returned.
*/
export function reach(obj: object | null, chain: string | (string | number)[] | false | null | undefined, options?: reach.Options): any;

declare namespace reach {

    interface Options {

        /**
        String to split chain path on. Defaults to '.'.

        @default false
        */
        readonly separator?: string;

        /**
        Value to return if the path or value is not present. No default value.

        @default false
        */
        readonly default?: any;

        /**
        If true, will throw an error on missing member in the chain. Default to false.

        @default false
        */
        readonly strict?: boolean;

        /**
        If true, allows traversing functions for properties. false will throw an error if a function is part of the chain.

        @default false
        */
        readonly functions?: boolean;
    }
}


/**
Replace string parameters (using format "{path.to.key}") with their corresponding object key values using `Hoek.reach()`.

@param obj - the object from which to look up the value.
@param template - the string containing {} enclosed key paths to be replaced.

@return The template string with the {} enclosed keys replaced with looked-up values.
*/
export function reachTemplate(obj: object | null, template: string, options?: reach.Options): string;


/**
Throw an error if condition is falsey.

@param condition - If `condition` is not truthy, an exception is thrown.
@param error - The error thrown if the condition fails.

@return Does not return a value but throws if the `condition` is falsey.
*/
export function assert(condition: any, error: Error): void;


/**
Throw an error if condition is falsey.

@param condition - If `condition` is not truthy, an exception is thrown.
@param args - Any number of values, concatenated together (space separated) to create the error message.

@return Does not return a value but throws if the `condition` is falsey.
*/
export function assert(condition: any, ...args: any): void;


/**
A benchmarking timer, using the internal node clock for maximum accuracy.
 */
export class Bench {

    constructor();

    /** The starting timestamp expressed in the number of milliseconds since the epoch. */
    ts: number;

    /** The time in milliseconds since the object was created. */
    elapsed(): number;

    /** Reset the `ts` value to now. */
    reset(): void;

    /** The current time in milliseconds since the epoch. */
    static now(): number;
}


/**
Escape string for Regex construction by prefixing all reserved characters with a backslash.

@param string - The string to be escaped.

@return The escaped string.
*/
export function escapeRegex(string: string): string;


/**
Escape string for usage as an attribute value in HTTP headers.

@param attribute - The string to be escaped.

@return The escaped string. Will throw on invalid characters that are not supported to be escaped.
*/
export function escapeHeaderAttribute(attribute: string): string;


/**
Escape string for usage in HTML.

@param string - The string to be escaped.

@return The escaped string.
*/
export function escapeHtml(string: string): string;


/**
Escape string for usage in JSON.

@param string - The string to be escaped.

@return The escaped string.
*/
export function escapeJson(string: string): string;


/**
Wraps a function to ensure it can only execute once.

@param method - The function to be wrapped.

@return The wrapped function.
 */
export function once<T extends Function>(method: T): T;


/**
A reusable no-op function.
*/
export function ignore(...ignore: any): void;


/**
Generate a random file name within a given path and optional extension.

@param path - The file path within to generate a temporary file.
@param extension - File extension.

@return The generarted filename.
*/
export function uniqueFilename(path: string, extension?: string): string;


/**
Converts a JavaScript value to a JavaScript Object Notation (JSON) string with protection against thrown errors.

@param value A JavaScript value, usually an object or array, to be converted.
@param replacer The JSON.stringify() `replacer` argument.
@param space Adds indentation, white space, and line break characters to the return-value JSON text to make it easier to read.

@return The JSON string. If the operation fails, an error string value is returned (no exception thrown).
*/
export function stringify(value: any, replacer?: any, space?: string | number): string;


/**
Returns a Promise that resolves after the requested timeout.

@param timeout - The number of milliseconds to wait before resolving the Promise.

@return A Promise.
*/
export function wait(timeout?: number): Promise<void>;


/**
Returns a Promise that never resolves.
*/
export function block(): Promise<void>;
