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
 *  A regular type definition
 */
public class ClassTypeAccess extends SubPool<de.unistuttgart.iste.ps.skilled.sir.ClassType, de.unistuttgart.iste.ps.skilled.sir.Type> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    ClassTypeAccess(int poolIndex, UserdefinedTypeAccess superPool) {
        super(poolIndex, "classtype", superPool, new HashSet<String>(Arrays.asList(new String[] { "fields", "interfaces", "super" })), noAutoFields());
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

            de.unistuttgart.iste.ps.skilled.sir.ClassType r = new de.unistuttgart.iste.ps.skilled.sir.ClassType(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.ClassType> f;
        switch (name) {
        case "fields":
            f = new KnownField_ClassType_fields(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.FieldLike>)(owner().poolByName().get("fieldlike"))), 1 + dataFields.size(), this);
            break;

        case "interfaces":
            f = new KnownField_ClassType_interfaces(new SetType<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.InterfaceType>)(owner().poolByName().get("interfacetype"))), 1 + dataFields.size(), this);
            break;

        case "super":
            f = new KnownField_ClassType_Zsuper((FieldType<de.unistuttgart.iste.ps.skilled.sir.ClassType>)(owner().poolByName().get("classtype")), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ClassType> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ClassType> f;
        switch (name) {
        case "fields":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ClassType>) new KnownField_ClassType_fields((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike>>) type, ID, this);
            break;

        case "interfaces":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ClassType>) new KnownField_ClassType_interfaces((FieldType<java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType>>) type, ID, this);
            break;

        case "super":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ClassType>) new KnownField_ClassType_Zsuper((FieldType<de.unistuttgart.iste.ps.skilled.sir.ClassType>) type, ID, this);
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
     * @return a new ClassType instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.ClassType make() {
        de.unistuttgart.iste.ps.skilled.sir.ClassType rval = new de.unistuttgart.iste.ps.skilled.sir.ClassType();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.ClassType make(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields, java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> interfaces, de.unistuttgart.iste.ps.skilled.sir.ClassType Zsuper, de.unistuttgart.iste.ps.skilled.sir.Comment comment, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions, de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        de.unistuttgart.iste.ps.skilled.sir.ClassType rval = new de.unistuttgart.iste.ps.skilled.sir.ClassType(-1, fields, interfaces, Zsuper, comment, hints, restrictions, name);
        add(rval);
        return rval;
    }

    public ClassTypeBuilder build() {
        return new ClassTypeBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.ClassType());
    }

    /**
     * Builder for new ClassType instances.
     *
     * @author Timm Felden
     */
    public static final class ClassTypeBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.ClassType> {

        protected ClassTypeBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.ClassType, ? super de.unistuttgart.iste.ps.skilled.sir.ClassType> pool, de.unistuttgart.iste.ps.skilled.sir.ClassType instance) {
            super(pool, instance);
        }

        public ClassTypeBuilder fields(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields) {
            instance.setFields(fields);
            return this;
        }

        public ClassTypeBuilder interfaces(java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> interfaces) {
            instance.setInterfaces(interfaces);
            return this;
        }

        public ClassTypeBuilder Zsuper(de.unistuttgart.iste.ps.skilled.sir.ClassType Zsuper) {
            instance.setSuper(Zsuper);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.ClassType make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.ClassType rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.ClassType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.ClassType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.ClassType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.ClassType.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
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
                de.unistuttgart.iste.ps.skilled.sir.ClassType.SubType r = new de.unistuttgart.iste.ps.skilled.sir.ClassType.SubType(this, i + 1);
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
