package codedef.enums;

/** The basic building block of formatted code.
 *  For all CODE_NODE enum there is one shared CodeNode implementation.
 *    Customized rules are defined on init in CodeDef.
 *    New instances come from PrototypeFactory: copies of initialized object with customized rules set
 *    Each node has an instance of Translator (provided by OutLangManager) to define custom translator behavior.
 *  For each CODE_NODE enum there is a Translator implementation.
 *
 *  Each node has required and optional attributes, like 'name' etc.
 *  Each node accesses a list of nested children for maintaining the code tree structure.
 *    Each node's translator further divides the list of nested children into head and body, for nodes that require it
 *
 *  Validation is cursory to catch large errors. Use a target-language linter on the result for fine errors.
 *    Required and allowed attributes are validated when CodeNode objects are created
 *    Required and allowed children are validated when tree is built
 *  Enums ending in _GRP are aliases to group types together, to satisfy validator.
 *    Rather than list every allowed child type, use the group/alias to allow all nodes in that group (in CodeDef)
 *
 *  A node is either large- or small-scope
 *  Large-scope nodes (package, class, method etc) have nesting rules
 *    and do their own formatting for the most part.
 *  Small-scope nodes (expressions and their parts) can nest any other small-scope nodes
 *    and depend on parent nodes for formatting.
 *  The line between large- and small-scope is blurry, based on general structures of target languages.
 */
public enum CODE_NODE {
    GLOB,           // non-code-generating tree root
    PACKAGE,        // directory
    FILE,
    IMPORT,         // wrapper for import items
    IMPORT_ITEM,    // import, include etc.
    CLASS,
    METHOD,         // function
    METHOD_ARGS,    // wrapper for the list of method args

    CODE_BLOCK,     // { code }
    PAR_BLOCK,      // ( a, b, c) // par stands for 'parentheses', used for if statements
    BOOL_BLOCK,     // !( a b c)
    STATEMENT,      // expr expr ;
    EXPR_GRP,       // Group/alias for any small-scope struct

    IF_ELSE,        // wraps true case + optional else case
    CONDITIONAL,    // wrapper for comparison, conjunction etc
    CONJUNCTION,    // && ||
    COMPARISON,     // == < <= etc
    ELSE,

    WHILE,          // loop with conditional at top
    DO_WHILE,       // loop with conditional at bottom

    FOR,            // for(type i; conditional; i = i + FOR_STEP)
    FOR_INIT,       // wraps a statement: varName = initial value
    FOR_INC,        // wraps an expression: i++
    FOR_IN,         // for(type name in EXPR_GRP)

    VAR_DEF_GRP,    // Group/Alias for all var def
    VAR_DEF_SCALAR, // type name
    VAR_DEF_LIST,   // List<type> name
    VAR_DEF_MAP,    // Map<type1, type2> name
    VAR_DEF_SET,    // Set<type> name
    VAR_DEF_OBJECT, // LiteralObjectType objectName
    VAR_DEF_ARRAY,  // type[] name

    FUN_CALL,       // myFun(myOtherFun())

    NEW_DEF_GRP,    // Group/Alias for all new object
    NEW_LIST,       // new List<>(args)
    NEW_MAP,        // new Map<>(args)
    NEW_SET,        // new Set<>(args)
    NEW_OBJECT,     // new type(args)
    NEW_ARRAY,      // new type[size?]{args}

    SWITCH,         // switch(name){}
    SWITCH_CASE,    // case :
    SWITCH_DEFAULT, // final case in switch

    COMMENT,        // text
    COMMENT_LONG,   // /* text */

    LIT,            // text like +-= or an idiom not in CodeGen's vocabulary (loss of language agnosticism)
    ASSIGN,         // =
    RETURN,         // literal
    BREAK           // literal
    ;

    CODE_NODE() {
    }

}
