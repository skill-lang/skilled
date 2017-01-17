/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 16.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir;

import de.ust.skill.common.java.api.FieldDeclaration;
import de.ust.skill.common.java.internal.NamedType;
import de.ust.skill.common.java.internal.SkillObject;
import de.ust.skill.common.java.internal.StoragePool;


/**
 *  a language custom field
 */
public class CustomField extends FieldLike {
    private static final long serialVersionUID = 0x5c11L + ((long) "customfield".hashCode()) << 32;

    @Override
    public String skillName() {
        return "customfield";
    }

    /**
     * Create a new unmanaged CustomField. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public CustomField() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public CustomField(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public CustomField(long skillID, java.lang.String language, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> options, java.lang.String typename, de.unistuttgart.iste.ps.skilled.sir.Comment comment, de.unistuttgart.iste.ps.skilled.sir.Identifier name, de.unistuttgart.iste.ps.skilled.sir.Type type, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        super(skillID);
        this.language = language;
        this.options = options;
        this.typename = typename;
        this.comment = comment;
        this.name = name;
        this.type = type;
        this.hints = hints;
        this.restrictions = restrictions;
    }

    protected java.lang.String language = null;

    /**
     *  the name of the language that treats this fields
     */
    final public java.lang.String getLanguage() {
        return language;
    }

    /**
     *  the name of the language that treats this fields
     */
    final public void setLanguage(java.lang.String language) {
        this.language = language;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> options = null;

    final public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> getOptions() {
        return options;
    }

    final public void setOptions(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> options) {
        this.options = options;
    }

    protected java.lang.String typename = null;

    /**
     *  the type name that will be used to create a language specific field type
     */
    final public java.lang.String getTypename() {
        return typename;
    }

    /**
     *  the type name that will be used to create a language specific field type
     */
    final public void setTypename(java.lang.String typename) {
        this.typename = typename;
    }

    /**
     * unchecked conversions are required, because the Java type system known
     * nothing of our invariants
     * 
     * @note to self: Boxing bei primitiven beachten!
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(FieldDeclaration<T> field) {
        switch (field.name()) {
        case "language":
            return (T) language;
        case "options":
            return (T) options;
        case "typename":
            return (T) typename;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("CustomField(this: ").append(this);
        sb.append(", language: ").append(language);
        sb.append(", options: ").append(options);
        sb.append(", typename: ").append(typename);
        sb.append(", comment: ").append(comment);
        sb.append(", name: ").append(name);
        sb.append(", type: ").append(type);
        sb.append(", hints: ").append(hints);
        sb.append(", restrictions: ").append(restrictions);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends CustomField implements NamedType {
        private final StoragePool<?, ?> τPool;

        /** internal use only!!! */
        public SubType(StoragePool<?, ?> τPool, long skillID) {
            super(skillID);
            this.τPool = τPool;
        }

        @Override
        public StoragePool<?, ?> τPool() {
          return τPool;
        }

        @Override
        public String skillName() {
            return τPool.name();
        }

        @Override
        public String toString() {
            return skillName() + "#" + skillID;
        }
    }
}
