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

public class InterfaceTypeAccess extends SubPool<de.unistuttgart.iste.ps.skilled.sir.InterfaceType, de.unistuttgart.iste.ps.skilled.sir.Type> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    InterfaceTypeAccess(int poolIndex, UserdefinedTypeAccess superPool) {
        super(poolIndex, "interfacetype", superPool, new HashSet<String>(Arrays.asList(new String[] { "fields", "interfaces", "super" })), noAutoFields());
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

            de.unistuttgart.iste.ps.skilled.sir.InterfaceType r = new de.unistuttgart.iste.ps.skilled.sir.InterfaceType(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.InterfaceType> f;
        switch (name) {
        case "fields":
            f = new KnownField_InterfaceType_fields(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.FieldLike>)(owner().poolByName().get("fieldlike"))), 1 + dataFields.size(), this);
            break;

        case "interfaces":
            f = new KnownField_InterfaceType_interfaces(new SetType<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.InterfaceType>)(owner().poolByName().get("interfacetype"))), 1 + dataFields.size(), this);
            break;

        case "super":
            f = new KnownField_InterfaceType_Zsuper((FieldType<de.unistuttgart.iste.ps.skilled.sir.ClassType>)(owner().poolByName().get("classtype")), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.InterfaceType> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.InterfaceType> f;
        switch (name) {
        case "fields":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.InterfaceType>) new KnownField_InterfaceType_fields((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike>>) type, ID, this);
            break;

        case "interfaces":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.InterfaceType>) new KnownField_InterfaceType_interfaces((FieldType<java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType>>) type, ID, this);
            break;

        case "super":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.InterfaceType>) new KnownField_InterfaceType_Zsuper((FieldType<de.unistuttgart.iste.ps.skilled.sir.ClassType>) type, ID, this);
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
     * @return a new InterfaceType instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.InterfaceType make() {
        de.unistuttgart.iste.ps.skilled.sir.InterfaceType rval = new de.unistuttgart.iste.ps.skilled.sir.InterfaceType();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.InterfaceType make(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields, java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> interfaces, de.unistuttgart.iste.ps.skilled.sir.ClassType Zsuper, de.unistuttgart.iste.ps.skilled.sir.Comment comment, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions, de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        de.unistuttgart.iste.ps.skilled.sir.InterfaceType rval = new de.unistuttgart.iste.ps.skilled.sir.InterfaceType(-1, fields, interfaces, Zsuper, comment, hints, restrictions, name);
        add(rval);
        return rval;
    }

    public InterfaceTypeBuilder build() {
        return new InterfaceTypeBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.InterfaceType());
    }

    /**
     * Builder for new InterfaceType instances.
     *
     * @author Timm Felden
     */
    public static final class InterfaceTypeBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> {

        protected InterfaceTypeBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.InterfaceType, ? super de.unistuttgart.iste.ps.skilled.sir.InterfaceType> pool, de.unistuttgart.iste.ps.skilled.sir.InterfaceType instance) {
            super(pool, instance);
        }

        public InterfaceTypeBuilder fields(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields) {
            instance.setFields(fields);
            return this;
        }

        public InterfaceTypeBuilder interfaces(java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> interfaces) {
            instance.setInterfaces(interfaces);
            return this;
        }

        public InterfaceTypeBuilder Zsuper(de.unistuttgart.iste.ps.skilled.sir.ClassType Zsuper) {
            instance.setSuper(Zsuper);
            return this;
        }

        public InterfaceTypeBuilder comment(de.unistuttgart.iste.ps.skilled.sir.Comment comment) {
            instance.setComment(comment);
            return this;
        }

        public InterfaceTypeBuilder hints(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints) {
            instance.setHints(hints);
            return this;
        }

        public InterfaceTypeBuilder restrictions(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
            instance.setRestrictions(restrictions);
            return this;
        }

        public InterfaceTypeBuilder name(de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
            instance.setName(name);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.InterfaceType make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.InterfaceType rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.InterfaceType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.InterfaceType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.InterfaceType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.InterfaceType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
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
                de.unistuttgart.iste.ps.skilled.sir.InterfaceType.SubType r = new de.unistuttgart.iste.ps.skilled.sir.InterfaceType.SubType(this, i + 1);
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
