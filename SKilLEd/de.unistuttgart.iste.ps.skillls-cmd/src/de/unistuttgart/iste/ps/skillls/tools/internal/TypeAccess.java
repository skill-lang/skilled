/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 29.02.2016                               *
 * |___/_|\_\_|_|____|    by: mpl                                             *
\*                                                                            */
package de.unistuttgart.iste.ps.skillls.tools.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import de.ust.skill.common.java.api.SkillException;
import de.ust.skill.common.java.internal.*;
import de.ust.skill.common.java.internal.fieldDeclarations.AutoField;
import de.ust.skill.common.java.internal.fieldTypes.*;
import de.ust.skill.common.java.internal.parts.Block;
import de.ust.skill.common.java.restrictions.FieldRestriction;

public class TypeAccess extends SubPool<de.unistuttgart.iste.ps.skillls.tools.Type, de.unistuttgart.iste.ps.skillls.tools.HintParent> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    TypeAccess(int poolIndex, HintParentAccess superPool) {
        super(poolIndex, "type", superPool, new HashSet<String>(Arrays.asList(new String[] { "comment", "extends", "fields", "file", "name", "orig", "restrictions" })), noAutoFields());
    }

    @Override
    public void insertInstances() {
        de.unistuttgart.iste.ps.skillls.tools.HintParent[] data = ((HintParentAccess)basePool).data();
        final Block last = blocks().getLast();
        int i = (int) last.bpo;
        int high = (int) (last.bpo + last.count);
        while (i < high) {
            if (null != data[i])
                return;

            de.unistuttgart.iste.ps.skillls.tools.Type r = new de.unistuttgart.iste.ps.skillls.tools.Type(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skillls.tools.Type> f;
        switch (name) {
        case "comment":
            f = new KnownField_Type_comment(string, 1 + dataFields.size(), this);
            break;

        case "extends":
            f = new KnownField_Type_extends_(new VariableLengthArray<>(string), 1 + dataFields.size(), this);
            break;

        case "fields":
            f = new KnownField_Type_fields(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skillls.tools.Field>)(owner().poolByName().get("field"))), 1 + dataFields.size(), this);
            break;

        case "file":
            f = new KnownField_Type_file((FieldType<de.unistuttgart.iste.ps.skillls.tools.File>)(owner().poolByName().get("file")), 1 + dataFields.size(), this);
            break;

        case "name":
            f = new KnownField_Type_name(string, 1 + dataFields.size(), this);
            break;

        case "orig":
            f = new KnownField_Type_orig((FieldType<de.unistuttgart.iste.ps.skillls.tools.Type>)(owner().poolByName().get("type")), 1 + dataFields.size(), this);
            break;

        case "restrictions":
            f = new KnownField_Type_restrictions(new VariableLengthArray<>(string), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Type> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Type> f;
        switch (name) {
        case "comment":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Type>) new KnownField_Type_comment((FieldType<java.lang.String>) type, ID, this);
            break;

        case "extends":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Type>) new KnownField_Type_extends_((FieldType<java.util.ArrayList<java.lang.String>>) type, ID, this);
            break;

        case "fields":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Type>) new KnownField_Type_fields((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Field>>) type, ID, this);
            break;

        case "file":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Type>) new KnownField_Type_file((FieldType<de.unistuttgart.iste.ps.skillls.tools.File>) type, ID, this);
            break;

        case "name":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Type>) new KnownField_Type_name((FieldType<java.lang.String>) type, ID, this);
            break;

        case "orig":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Type>) new KnownField_Type_orig((FieldType<de.unistuttgart.iste.ps.skillls.tools.Type>) type, ID, this);
            break;

        case "restrictions":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Type>) new KnownField_Type_restrictions((FieldType<java.util.ArrayList<java.lang.String>>) type, ID, this);
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
     * @return a new Type instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skillls.tools.Type make() {
        de.unistuttgart.iste.ps.skillls.tools.Type rval = new de.unistuttgart.iste.ps.skillls.tools.Type();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skillls.tools.Type make(java.lang.String comment, java.util.ArrayList<java.lang.String> extends_, java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Field> fields, de.unistuttgart.iste.ps.skillls.tools.File file, java.lang.String name, de.unistuttgart.iste.ps.skillls.tools.Type orig, java.util.ArrayList<java.lang.String> restrictions, java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> hints) {
        de.unistuttgart.iste.ps.skillls.tools.Type rval = new de.unistuttgart.iste.ps.skillls.tools.Type(-1, comment, extends_, fields, file, name, orig, restrictions, hints);
        add(rval);
        return rval;
    }

    public TypeBuilder build() {
        return new TypeBuilder(this, new de.unistuttgart.iste.ps.skillls.tools.Type());
    }

    /**
     * Builder for new Type instances.
     *
     * @author Timm Felden
     */
    public static final class TypeBuilder extends Builder<de.unistuttgart.iste.ps.skillls.tools.Type> {

        protected TypeBuilder(StoragePool<de.unistuttgart.iste.ps.skillls.tools.Type, ? super de.unistuttgart.iste.ps.skillls.tools.Type> pool, de.unistuttgart.iste.ps.skillls.tools.Type instance) {
            super(pool, instance);
        }

        public TypeBuilder comment(java.lang.String comment) {
            instance.setComment(comment);
            return this;
        }

        public TypeBuilder extends_(java.util.ArrayList<java.lang.String> extends_) {
            instance.setExtends(extends_);
            return this;
        }

        public TypeBuilder fields(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Field> fields) {
            instance.setFields(fields);
            return this;
        }

        public TypeBuilder file(de.unistuttgart.iste.ps.skillls.tools.File file) {
            instance.setFile(file);
            return this;
        }

        public TypeBuilder name(java.lang.String name) {
            instance.setName(name);
            return this;
        }

        public TypeBuilder orig(de.unistuttgart.iste.ps.skillls.tools.Type orig) {
            instance.setOrig(orig);
            return this;
        }

        public TypeBuilder restrictions(java.util.ArrayList<java.lang.String> restrictions) {
            instance.setRestrictions(restrictions);
            return this;
        }

        public TypeBuilder hints(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> hints) {
            instance.setHints(hints);
            return this;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.Type, de.unistuttgart.iste.ps.skillls.tools.HintParent> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skillls.tools.Type.SubType, de.unistuttgart.iste.ps.skillls.tools.HintParent> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skillls.tools.Type.SubType, de.unistuttgart.iste.ps.skillls.tools.HintParent> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.Type.SubType, de.unistuttgart.iste.ps.skillls.tools.HintParent> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skillls.tools.HintParent[] data = ((HintParentAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skillls.tools.Type.SubType r = new de.unistuttgart.iste.ps.skillls.tools.Type.SubType(this, i + 1);
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
