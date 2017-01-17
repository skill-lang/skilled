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

public class EnumTypeAccess extends SubPool<de.unistuttgart.iste.ps.skilled.sir.EnumType, de.unistuttgart.iste.ps.skilled.sir.Type> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    EnumTypeAccess(int poolIndex, UserdefinedTypeAccess superPool) {
        super(poolIndex, "enumtype", superPool, new HashSet<String>(Arrays.asList(new String[] { "fields", "instances" })), noAutoFields());
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

            de.unistuttgart.iste.ps.skilled.sir.EnumType r = new de.unistuttgart.iste.ps.skilled.sir.EnumType(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.EnumType> f;
        switch (name) {
        case "fields":
            f = new KnownField_EnumType_fields(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.FieldLike>)(owner().poolByName().get("fieldlike"))), 1 + dataFields.size(), this);
            break;

        case "instances":
            f = new KnownField_EnumType_instances(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.Identifier>)(owner().poolByName().get("identifier"))), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.EnumType> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.EnumType> f;
        switch (name) {
        case "fields":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.EnumType>) new KnownField_EnumType_fields((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike>>) type, ID, this);
            break;

        case "instances":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.EnumType>) new KnownField_EnumType_instances((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Identifier>>) type, ID, this);
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
     * @return a new EnumType instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.EnumType make() {
        de.unistuttgart.iste.ps.skilled.sir.EnumType rval = new de.unistuttgart.iste.ps.skilled.sir.EnumType();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.EnumType make(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Identifier> instances, de.unistuttgart.iste.ps.skilled.sir.Comment comment, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions, de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        de.unistuttgart.iste.ps.skilled.sir.EnumType rval = new de.unistuttgart.iste.ps.skilled.sir.EnumType(-1, fields, instances, comment, hints, restrictions, name);
        add(rval);
        return rval;
    }

    public EnumTypeBuilder build() {
        return new EnumTypeBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.EnumType());
    }

    /**
     * Builder for new EnumType instances.
     *
     * @author Timm Felden
     */
    public static final class EnumTypeBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.EnumType> {

        protected EnumTypeBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.EnumType, ? super de.unistuttgart.iste.ps.skilled.sir.EnumType> pool, de.unistuttgart.iste.ps.skilled.sir.EnumType instance) {
            super(pool, instance);
        }

        public EnumTypeBuilder fields(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields) {
            instance.setFields(fields);
            return this;
        }

        public EnumTypeBuilder instances(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Identifier> instances) {
            instance.setInstances(instances);
            return this;
        }

        public EnumTypeBuilder comment(de.unistuttgart.iste.ps.skilled.sir.Comment comment) {
            instance.setComment(comment);
            return this;
        }

        public EnumTypeBuilder hints(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints) {
            instance.setHints(hints);
            return this;
        }

        public EnumTypeBuilder restrictions(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
            instance.setRestrictions(restrictions);
            return this;
        }

        public EnumTypeBuilder name(de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
            instance.setName(name);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.EnumType make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.EnumType rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.EnumType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.EnumType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.EnumType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.EnumType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
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
                de.unistuttgart.iste.ps.skilled.sir.EnumType.SubType r = new de.unistuttgart.iste.ps.skilled.sir.EnumType.SubType(this, i + 1);
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
