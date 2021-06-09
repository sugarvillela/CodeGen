package codedef.modifier;

/** The basic building block of formatted code.
 *  For all CODE_NODE enum there is a shared CodeNode implementation.
 *    Customized rules are defined on init in CodeDef.
 *    Each node has an instance of Translator (provided by OutLangManager) to define custom translator behavior.
 *  For each CODE_NODE enum there is a Translator implementation.
 *
 *  Each node has required and optional attributes, like 'name', 'value' etc.
 *  Each node accesses a list of nested children for maintaining the code tree structure.
 *    Tree structure is maintained by a special implementation of IGTree<ICodeNode>
 *    Each node's translator further divides the list of nested children into head and body, for nodes that require it
 *
 *  Validation is cursory to catch large errors. Use a target-language linter on the result for fine errors.
 *
 *  A node is either large- or small-scope, denoted by a boolean parameter in the enum.
 *  Large-scope nodes (package, class, method etc) have strict nesting rules
 *    and do their own formatting for the most part.
 *  Small-scope nodes (expressions and their parts) can nest any other small-scope nodes
 *    and depend on parent nodes for formatting.
 *  The line between large- and small-scope is blurry, based on general structures of target languages.
 */
public enum CODE_NODE {
    GLOB,
    PACKAGE,
    FILE,
    IMPORT,
    IMPORT_ITEM,
    CLASS,
    CLASS_FIELD,
    METHOD,
    CONSTRUCTOR,
    METHOD_ARGS,
    METHOD_ARG,
    IF_ELSE,
    CONDITIONAL,
    ELSE,

    CODE_BLOCK,
    PAR_BLOCK,
    BOOL_BLOCK,
    STATEMENT,
    EXPR,

    LIT,
    CONJUNCTION,
    COMPARISON,
    VAR_DEF,
    RETURN,
    ASSIGN,
    COMMENT,
    COMMENT_LONG,

    SWITCH,
    SWITCH_CASE,
    SWITCH_DEFAULT,
    BREAK
    ;
    

    CODE_NODE() {
    }

}
