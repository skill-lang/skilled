/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 18.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir;

import de.ust.skill.common.java.api.FieldDeclaration;
import de.ust.skill.common.java.internal.NamedType;
import de.ust.skill.common.java.internal.SkillObject;
import de.ust.skill.common.java.internal.StoragePool;


public class EnumType extends UserdefinedType {
    private static final long serialVersionUID = 0x5c11L + ((long) "enumtype".hashCode()) << 32;

    @Override
    public String skillName() {
        return "enumtype";
    }

    /**
     * Create a new unmanaged EnumType. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public EnumType() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public EnumType(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public EnumType(long skillID, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Identifier> instances, de.unistuttgart.iste.ps.skilled.sir.Comment comment, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions, de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        super(skillID);
        this.fields = fields;
        this.instances = instances;
        this.comment = comment;
        this.hints = hints;
        this.restrictions = restrictions;
        this.name = name;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields = null;

    /**
     *  there might be multiple fields under the same name in this type, as the class can be a) part of configuration b)
     *  contain custom fields
     */
    final public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> getFields() {
        return fields;
    }

    /**
     *  there might be multiple fields under the same name in this type, as the class can be a) part of configuration b)
     *  contain custom fields
     */
    final public void setFields(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields) {
        this.fields = fields;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Identifier> instances = null;

    final public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Identifier> getInstances() {
        return instances;
    }

    final public void setInstances(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Identifier> instances) {
        this.instances = instances;
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
        case "fields":
            return (T) fields;
        case "instances":
            return (T) instances;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("EnumType(this: ").append(this);
        sb.append(", fields: ").append(fields);
        sb.append(", instances: ").append(instances);
        sb.append(", comment: ").append(comment);
        sb.append(", hints: ").append(hints);
        sb.append(", restrictions: ").append(restrictions);
        sb.append(", name: ").append(name);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends EnumType implements NamedType {
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
