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
 *  All types defined by skill.
 */
public class BuiltinType extends Type {
    private static final long serialVersionUID = 0x5c11L + ((long) "builtintype".hashCode()) << 32;

    @Override
    public String skillName() {
        return "builtintype";
    }

    /**
     * Create a new unmanaged BuiltinType. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public BuiltinType() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public BuiltinType(long skillID) {
        super(skillID);
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("BuiltinType(this: ").append(this);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends BuiltinType implements NamedType {
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
