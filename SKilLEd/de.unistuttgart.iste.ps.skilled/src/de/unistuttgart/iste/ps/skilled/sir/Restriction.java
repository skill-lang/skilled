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


/**
 *  Base of type and field restrictions.
 */
public class Restriction extends SkillObject {
    private static final long serialVersionUID = 0x5c11L + ((long) "restriction".hashCode()) << 32;

    @Override
    public String skillName() {
        return "restriction";
    }

    /**
     * Create a new unmanaged Restriction. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public Restriction() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public Restriction(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public Restriction(long skillID, java.util.ArrayList<java.lang.String> arguments, java.lang.String name) {
        super(skillID);
        this.arguments = arguments;
        this.name = name;
    }

    protected java.util.ArrayList<java.lang.String> arguments = null;

    /**
     *  restriction arguments are stored as strings, because I do not want to introduce a million restriction types just
     *  for range restrictions.
     */
    final public java.util.ArrayList<java.lang.String> getArguments() {
        return arguments;
    }

    /**
     *  restriction arguments are stored as strings, because I do not want to introduce a million restriction types just
     *  for range restrictions.
     */
    final public void setArguments(java.util.ArrayList<java.lang.String> arguments) {
        this.arguments = arguments;
    }

    protected java.lang.String name = null;

    final public java.lang.String getName() {
        return name;
    }

    final public void setName(java.lang.String name) {
        this.name = name;
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
        case "arguments":
            return (T) arguments;
        case "name":
            return (T) name;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("Restriction(this: ").append(this);
        sb.append(", arguments: ").append(arguments);
        sb.append(", name: ").append(name);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends Restriction implements NamedType {
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
