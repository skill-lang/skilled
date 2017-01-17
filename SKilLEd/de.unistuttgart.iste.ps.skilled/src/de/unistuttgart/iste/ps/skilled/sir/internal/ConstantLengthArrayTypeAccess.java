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
 * 
 *  @note  kind is always "array"
 */
public class ConstantLengthArrayTypeAccess extends SubPool<de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType, de.unistuttgart.iste.ps.skilled.sir.Type> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    ConstantLengthArrayTypeAccess(int poolIndex, SingleBaseTypeContainerAccess superPool) {
        super(poolIndex, "constantlengtharraytype", superPool, new HashSet<String>(Arrays.asList(new String[] { "length" })), noAutoFields());
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

            de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType r = new de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType> f;
        switch (name) {
        case "length":
            f = new KnownField_ConstantLengthArrayType_length(V64.get(), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType> f;
        switch (name) {
        case "length":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType>) new KnownField_ConstantLengthArrayType_length((FieldType<java.lang.Long>) type, ID, this);
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
     * @return a new ConstantLengthArrayType instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType make() {
        de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType rval = new de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType make(long length, de.unistuttgart.iste.ps.skilled.sir.GroundType base, java.lang.String kind) {
        de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType rval = new de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType(-1, length, base, kind);
        add(rval);
        return rval;
    }

    public ConstantLengthArrayTypeBuilder build() {
        return new ConstantLengthArrayTypeBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType());
    }

    /**
     * Builder for new ConstantLengthArrayType instances.
     *
     * @author Timm Felden
     */
    public static final class ConstantLengthArrayTypeBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType> {

        protected ConstantLengthArrayTypeBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType, ? super de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType> pool, de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType instance) {
            super(pool, instance);
        }

        public ConstantLengthArrayTypeBuilder length(long length) {
            instance.setLength(length);
            return this;
        }

        public ConstantLengthArrayTypeBuilder base(de.unistuttgart.iste.ps.skilled.sir.GroundType base) {
            instance.setBase(base);
            return this;
        }

        public ConstantLengthArrayTypeBuilder kind(java.lang.String kind) {
            instance.setKind(kind);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
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
                de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType.SubType r = new de.unistuttgart.iste.ps.skilled.sir.ConstantLengthArrayType.SubType(this, i + 1);
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
