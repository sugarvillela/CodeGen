package codedef.translatortests;

import codedef.impl.PrototypeFactory;
import org.junit.jupiter.api.Test;
import runstate.Glob;

import static codedef.enums.CODE_NODE.*;
import static codedef.enums.ENU_LANGUAGE.JAVA_;
import static codedef.enums.MODIFIER.*;

public class TranslatorTestValidation {
    protected static final PrototypeFactory f = Glob.PROTOTYPE_FACTORY;

    @Test
    void givenBadArgQuantityOfTwo_shouldAbort() {
        f.get(GLOB).putAttrib(LANGUAGE_, "java", "c++");    // expecting one arg
    }
    @Test
    void givenBadArgQuantityOfZero_shouldAbort() {
        f.get(GLOB).putAttrib(LANGUAGE_);    // expecting one arg
    }
    @Test
    void givenBadIntValue_shouldAbort() {
        f.get(GLOB)
            .putAttrib(LANGUAGE_, JAVA_.toString())
            .putAttrib(INDENT_, "BOLLOCKS");   // expecting a number
    }

    @Test
    void givenNonEnumeratedValue_shouldAbortIfEnumerationRequired() {
        f.get(GLOB).putAttrib(LANGUAGE_, "my cool language");    // expecting java
    }
    @Test
    void givenDisallowedAttrib_shouldAbort() {
        f.get(GLOB).putAttrib(IMPLEMENTS_, "some interface");   // IMPLEMENTS_ is not allowed
    }
    @Test
    void givenMissingAttrib_shouldAbort() {
        f.get(GLOB).setChildren(
                f.get(PACKAGE)  // expecting putAttrib(NAME_, packageName)
        );
    }
    @Test
    void givenBadNesting_shouldAbort() {
        f.get(GLOB).setChildren(
            f.get(PACKAGE).putAttrib(NAME_, "some_package").setChildren(
                f.get(GLOB)  // expecting a package or class
            )
        );
    }
}
