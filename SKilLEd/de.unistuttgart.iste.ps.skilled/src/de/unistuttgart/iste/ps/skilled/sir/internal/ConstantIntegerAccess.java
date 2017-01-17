/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 16.01.2017                               *
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
 *  representation of constant integers
 */
public class ConstantIntegerAccess extends SubPool<de.unistuttgart.iste.ps.skilled.sir.ConstantInteger, de.unistuttgart.iste.ps.skilled.sir.Type> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    ConstantIntegerAccess(int poolIndex, SimpleTypeAccess superPool) {
        super(poolIndex, "constantinteger", superPool, new HashSet<String>(Arrays.asList(new String[] { "value" })), noAutoFields());
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

            de.unistuttgart.iste.ps.skilled.sir.ConstantInteger r = new de.unistuttgart.iste.ps.skilled.sir.ConstantInteger(i + 1);
            data[i] = r;
            staticData.add(r);

            i += 1;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addKnownField(
        String name,
        de.ust.skill.common.java.internal.fieldTypes.StringType string,
        de.ust.skill.common.java.internal.fieldTypes.Annotation annotation) {

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.ConstantInteger> f;
        switch (name) {
        case "value":
            f = new KnownField_ConstantInteger_value(V64.get(), 1 + dataFields.size(), this);
            break;

        default:
            super.addKnownField(name, string, annotation);
            return;
        }
        if (!(f instanceof AutoField))
            dataFields.add(f);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ConstantInteger> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ConstantInteger> f;
        switch (name) {
        case "value":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ConstantInteger>) new KnownField_ConstantInteger_value((FieldType<java.lang.Long>) type, ID, this);
            break;

        default:
            return super.addField(ID, type, name, restrictions);
        }

        for (FieldRestriction<?> r : restrictions)
            f.addRestriction(r);
        dataFields.add(f);
        return f;
    }

    /**
     * @return a new ConstantInteger instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.ConstantInteger make() {
        de.unistuttgart.iste.ps.skilled.sir.ConstantInteger rval = new de.unistuttgart.iste.ps.skilled.sir.ConstantInteger();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.ConstantInteger make(long value, de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        de.unistuttgart.iste.ps.skilled.sir.ConstantInteger rval = new de.unistuttgart.iste.ps.skilled.sir.ConstantInteger(-1, value, name);
        add(rval);
        return rval;
    }

    public ConstantIntegerBuilder build() {
        return new ConstantIntegerBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.ConstantInteger());
    }

    /**
     * Builder for new ConstantInteger instances.
     *
     * @author Timm Felden
     */
    public static final class ConstantIntegerBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.ConstantInteger> {

        protected ConstantIntegerBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.ConstantInteger, ? super de.unistuttgart.iste.ps.skilled.sir.ConstantInteger> pool, de.unistuttgart.iste.ps.skilled.sir.ConstantInteger instance) {
            super(pool, instance);
        }

        public ConstantIntegerBuilder value(long value) {
            instance.setValue(value);
            return this;
        }

        public ConstantIntegerBuilder name(de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
            instance.setName(name);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.ConstantInteger make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.ConstantInteger rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.ConstantInteger, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.ConstantInteger.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.ConstantInteger.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.ConstantInteger.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
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
                de.unistuttgart.iste.ps.skilled.sir.ConstantInteger.SubType r = new de.unistuttgart.iste.ps.skilled.sir.ConstantInteger.SubType(this, i + 1);
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
