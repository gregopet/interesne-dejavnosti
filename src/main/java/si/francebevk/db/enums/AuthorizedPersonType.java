/*
 * This file is generated by jOOQ.
*/
package si.francebevk.db.enums;


import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.EnumType;
import org.jooq.Schema;

import si.francebevk.db.Public;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public enum AuthorizedPersonType implements EnumType {

    sibling("sibling"),

    grandparent("grandparent"),

    aunt_uncle("aunt_uncle"),

    other("other");

    private final String literal;

    private AuthorizedPersonType(String literal) {
        this.literal = literal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return getSchema() == null ? null : getSchema().getCatalog();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "authorized_person_type";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLiteral() {
        return literal;
    }
}