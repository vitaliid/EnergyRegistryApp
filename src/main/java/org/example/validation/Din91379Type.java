package org.example.validation;

/**
 * DIN 91379 character set datatypes; the name matches the root element in xsd/din91379-wrapper.xsd.
 */
public enum Din91379Type {
    DATATYPE_A("datatypeA"),
    DATATYPE_B("datatypeB"),
    DATATYPE_C("datatypeC"),
    DATATYPE_D("datatypeD"),
    DATATYPE_E("datatypeE");

    private final String element;

    Din91379Type(String element) {
        this.element = element;
    }

    public String element() {
        return element;
    }
}
