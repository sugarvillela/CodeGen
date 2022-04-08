## Description
### A Java-to-Java code generator that writes nicely formatted files, storing interim work in a JSON file    
Build directly from Java API or build from a stored JSON file.     

### Caveat       
There is a significant learning curve to using this program, due to the complexity of languages.

## Requirements  
Say you have a need to generate program code from another program, as in a source-to-source compiler. You would need three things:    
1.  An API in your source language to specify language structures to be generated.
2.  A way to store your specification in a JSON file (to separate the tasks of specifying and generating)    
3.  A way to format and generate code in the destination language.    

#### You may even wish for the generator to be language agnostic, meaning you can specify it in Java and generate it in, say C++.
*  Languages have common structures.  For instance "if-else" appears in most languages, as do methods, operators etc.
*  A lot of structures are not common between languages, like classes or lambda functions. One may have it while another doesn't.
*  The difficulty of achieving language agnosticism increases with the difference between source and target language.
    *  At some point, language translation becomes untenable (at least the way I'm doing it here).

#### To achieve some generality, the program should identify common language structures and attributes.  
*  Language structures like classes, methods, statements, operators etc. are defined in the program as Java enumerations. Attributes like 'name' and 'type' are defined in the same way.
    *  This tactic leads to a large number of enumerations, hence the learning curve.
*  When you want to use an idiom not defined by an Enumeration, you have two choices:
    *  Add the code as a literal string (will lose language agnosticism because the literal code is in the target language).
    *  Add an enumaration to the program to define your idiom.

## Program Overview
#### Language structures are defined as CODE_NODE enums.
*  Self-explanatory examples: PACKAGE, FILE, IMPORT, IMPORT_ITEM...
*  Less obvious examples: CODE_BLOCK, PAR_BLOCK, BOOL_BLOCK...
    *  See source file (package codedef.enums.CODE_NODE) for comments.
*  CODE_NODE only defines the structure type.
    *  Class CodeNode implements the functionality and maintains state.
*  CODE_NODEs define the tree structure of the language
*  CODE_NODEs are nodes in the tree that contain child nodes.
    *  For example PACKAGE -> FILE -> CLASS -> METHOD
*  The program includes perfunctory validation for structure.
    *  For example, you can't put a class into a method

#### Language structures have attributes, defined as MODIFIER enums.
*  Self-explanatory examples: NAME_, TYPE_, ABSTRACT_, FINAL_...
*  Less obvious examples: TYPE_CONJ_, TYPE_COMP_, IS_NEGATE_...
    *  See source file (package codedef.enums.MODIFIER) for comments
*  MODIFIER enums end with an underscore, so you can tell them from CODE_NODE enums. 
*  MODIFIER defines the attribute type.
    *  Class AttribModifier implements the functionality and maintains state.
    *  MODIFIERs additionally define requirements such as number of input args, input arg type and possible enumerated types.
        *  See source file (package codedef.enums.MODIFIER) for comments
*  MODIFIERs define the attributes that CODE_NODEs contain, like 'name'
*  The program includes perfunctory validation for attributes.
    *  Some attributes are required, others disallowed.
    *  Attributes need specific data, so the number and type of input args are validated.

#### For CODE_NODEs and ATTRIBUTEs, specific structure and content rules are not hard-coded in the class, but are defined in an init process.
*  The CodeDef class builds a map of singleton CODE_NODEs, pre-loaded with default values (allowed children, required attributes etc).
*  PrototypeFactory keeps the map and supplies CODE_NODE objects on request.
    *  On a request for a new CODE_NODE object, CodeDef returns a clone of the singleton default object.
*  See codedef.init.CodeDef and codedef.impl.PrototypeFactory for implementation of this.

#### Building language tree from Java API using three commands:
*  PrototypeFactory.get(CODE_NODE) returns a new CodeNode object of the specified configuration.
*  CodeNode.putAttribute(MODIFIER, value) sets a new attribute for the CodeNode object.
    *  Accepts any primitive value, subject to validation for arg quantity and arg type.
    *  See codedef.impl.AttribConvertUtil for implementation of this. 
*  CodeNode.setChildren(children...) adds any number of children to the parent node.
    *  Accepts a CodeNode child, or any number of primitive values (wraps values in a new CodeNode object in the most efficient way possible).
    *  See codedef.codenode.NodeWrapUtil for implementation of this.
*  CodeNode uses a builder pattern for chaining build commands.
*  Language is a tree. Properly indented build commands follow the tree structure for readability.
    *  Though a dense chunk of builder code is intimidating, it is fairly easy to understand.
    *  See test.mock.MockSource for examples.
*  Once built, use CodeNode.exportJson() to retrieve a JSONObject representing the structures and attributes.
    *  Calling it on the root node returns the entire tree.

#### Saving and retrieving language tree in JSON file
*  Call UtilFileJson.put(JSONObject, filePath) to save JSONObject to the file.
*  Call JCodeBuilder.build(filePath) to build a JSONObject from the file.
*  You can also run these from TranslationCenter.exportJson() and TranslationCenter.importJson().
    *  Set the root first or get the root after.

#### Generating formatted code
*  TranslationCenter go() translates from a CodeNode root to a set of files.
*  Uses setters and strategies to control how things happen. Before calling go():
  *  Set the root (the CodeNode tree)
  *  Set the WriteStrategy to write to a file, list or just display output
  *  Set the external path for where to write files (also sets a root directory for the Package info included in Java source files)
*  This program only implements a Java-to-Java translator.
    *  To implement another language, implement the ITranslator interface.
    *  Assuming common language structures, the only difference is syntax.

