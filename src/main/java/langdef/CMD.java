package langdef;

public enum CMD {
    // summary commands
    ST_SWAP,    // Stack swap (push or pop)
    AV_SWAP,    // Array value swap
    KV_SWAP,    // Object key value swap
    // sub-commands
    PUSH,
    POP,
    // non-stack commands
    ADD_DATA
}
