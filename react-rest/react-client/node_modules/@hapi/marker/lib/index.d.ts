declare const marker: {
    /**
    Generates a shared symbol.

    @param subject - The symbol subject.

    @return A symbol matching the subject.
    */
    (subject: string): symbol;

    default: typeof marker;
};

export = marker;
