package IR;

/*
 * Describes the role of an IDVariable instance
 */
public enum VarRole {
    GLOBAL,
    ARG,
    LOCAL,

    // TODO rethink about the following two when implementing classes
    CFIELD_VAR,
    CFIELD_FUNC
}
