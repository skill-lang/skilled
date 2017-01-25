/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 18.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import de.ust.skill.common.java.api.SkillException;
import de.ust.skill.common.java.internal.*;
import de.ust.skill.common.java.internal.fieldDeclarations.AutoField;
import de.ust.skill.common.java.internal.fieldTypes.*;
import de.ust.skill.common.java.internal.parts.Block;
import de.ust.skill.common.java.restrictions.FieldRestriction;

/**
 *  All types defined by skill.
 */
public class BuiltinTypeAccess extends SubPool<de.unistuttgart.iste.ps.skilled.sir.BuiltinType, de.unistuttgart.iste.ps.skilled.sir.Type> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    BuiltinTypeAccess(int poolIndex, TypeAccess superPool) {
        super(poolIndex, "builtintype", superPool, new HashSet<String>(Arrays.asList(new String[] {  })), noAutoFields());
    }

    @Override
    public void insertInstances() {
        de.unistuttgart.iste.ps.skilled.sir.Type[] data = ((TypeAccess)basePool).data();
        final Block last = blocks().getLast();
        int i = (int) last.bpo;
        int high = (int) (last.bpo + last.count);
        while (i < high) {
            if (null != data[i])
                return;

            de.unistuttgart.iste.ps.skilled.sir.BuiltinType r = new de.unistuttgart.iste.ps.skilled.sir.BuiltinType(i + 1);
            data[i] = r;
            staticData.add(r);

            i += 1;
        }
    }


    /**
     * @return a new BuiltinType instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.BuiltinType make() {
        de.unistuttgart.iste.ps.skilled.sir.BuiltinType rval = new de.unistuttgart.iste.ps.skilled.sir.BuiltinType();
        add(rval);
        return rval;
    }

    public BuiltinTypeBuilder build() {
        return new BuiltinTypeBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.BuiltinType());
    }

    /**
     * Builder for new BuiltinType instances.
     *
     * @author Timm Felden
     */
    public static final class BuiltinTypeBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.BuiltinType> {

        protected BuiltinTypeBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.BuiltinType, ? super de.unistuttgart.iste.ps.skilled.sir.BuiltinType> pool, de.unistuttgart.iste.ps.skilled.sir.BuiltinType instance) {
            super(pool, instance);
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.BuiltinType make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.BuiltinType rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.BuiltinType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.BuiltinType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.BuiltinType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.BuiltinType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.Type[] data = ((TypeAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.BuiltinType.SubType r = new de.unistuttgart.iste.ps.skilled.sir.BuiltinType.SubType(this, i + 1);
                data[i] = r;
                staticData.add(r);

                i += 1;
            }
        }
    }

    /**
     * punch a hole into the java type system :)
     */
    @SuppressWarnings("unchecked")
    static <T, U> FieldType<T> cast(FieldType<U> f) {
        return (FieldType<T>) f;
    }
}
