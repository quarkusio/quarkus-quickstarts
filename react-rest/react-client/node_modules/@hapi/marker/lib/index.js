'use strict';

const internals = {
    symbols: new Map()
};


module.exports = exports = function (subject) {

    let symbol = internals.symbols.get(subject);
    if (symbol) {
        return symbol;
    }

    symbol = Symbol(subject);
    internals.symbols.set(subject, symbol);

    return symbol;
};
