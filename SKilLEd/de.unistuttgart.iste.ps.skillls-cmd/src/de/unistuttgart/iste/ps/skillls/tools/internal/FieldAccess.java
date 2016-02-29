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

public class FieldAccess extends SubPool<de.unistuttgart.iste.ps.skillls.tools.Field, de.unistuttgart.iste.ps.skillls.tools.HintParent> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    FieldAccess(int poolIndex, HintParentAccess superPool) {
        super(poolIndex, "field", superPool, new HashSet<String>(Arrays.asList(new String[] { "comment", "name", "orig", "restrictions", "type" })), noAutoFields());
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

            de.unistuttgart.iste.ps.skillls.tools.Field r = new de.unistuttgart.iste.ps.skillls.tools.Field(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skillls.tools.Field> f;
        switch (name) {
        case "comment":
            f = new KnownField_Field_comment(string, 1 + dataFields.size(), this);
            break;

        case "name":
            f = new KnownField_Field_name(string, 1 + dataFields.size(), this);
            break;

        case "orig":
            f = new KnownField_Field_orig((FieldType<de.unistuttgart.iste.ps.skillls.tools.Field>)(owner().poolByName().get("field")), 1 + dataFields.size(), this);
            break;

        case "restrictions":
            f = new KnownField_Field_restrictions(new VariableLengthArray<>(string), 1 + dataFields.size(), this);
            break;

        case "type":
            f = new KnownField_Field_type((FieldType<de.unistuttgart.iste.ps.skillls.tools.Type>)(owner().poolByName().get("type")), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Field> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Field> f;
        switch (name) {
        case "comment":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Field>) new KnownField_Field_comment((FieldType<java.lang.String>) type, ID, this);
            break;

        case "name":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Field>) new KnownField_Field_name((FieldType<java.lang.String>) type, ID, this);
            break;

        case "orig":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Field>) new KnownField_Field_orig((FieldType<de.unistuttgart.iste.ps.skillls.tools.Field>) type, ID, this);
            break;

        case "restrictions":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Field>) new KnownField_Field_restrictions((FieldType<java.util.ArrayList<java.lang.String>>) type, ID, this);
            break;

        case "type":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Field>) new KnownField_Field_type((FieldType<de.unistuttgart.iste.ps.skillls.tools.Type>) type, ID, this);
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
     * @return a new Field instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skillls.tools.Field make() {
        de.unistuttgart.iste.ps.skillls.tools.Field rval = new de.unistuttgart.iste.ps.skillls.tools.Field();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skillls.tools.Field make(java.lang.String comment, java.lang.String name, de.unistuttgart.iste.ps.skillls.tools.Field orig, java.util.ArrayList<java.lang.String> restrictions, de.unistuttgart.iste.ps.skillls.tools.Type type, java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> hints) {
        de.unistuttgart.iste.ps.skillls.tools.Field rval = new de.unistuttgart.iste.ps.skillls.tools.Field(-1, comment, name, orig, restrictions, type, hints);
        add(rval);
        return rval;
    }

    public FieldBuilder build() {
        return new FieldBuilder(this, new de.unistuttgart.iste.ps.skillls.tools.Field());
    }

    /**
     * Builder for new Field instances.
     *
     * @author Timm Felden
     */
    public static final class FieldBuilder extends Builder<de.unistuttgart.iste.ps.skillls.tools.Field> {

        protected FieldBuilder(StoragePool<de.unistuttgart.iste.ps.skillls.tools.Field, ? super de.unistuttgart.iste.ps.skillls.tools.Field> pool, de.unistuttgart.iste.ps.skillls.tools.Field instance) {
            super(pool, instance);
        }

        public FieldBuilder comment(java.lang.String comment) {
            instance.setComment(comment);
            return this;
        }

        public FieldBuilder name(java.lang.String name) {
            instance.setName(name);
            return this;
        }

        public FieldBuilder orig(de.unistuttgart.iste.ps.skillls.tools.Field orig) {
            instance.setOrig(orig);
            return this;
        }

        public FieldBuilder restrictions(java.util.ArrayList<java.lang.String> restrictions) {
            instance.setRestrictions(restrictions);
            return this;
        }

        public FieldBuilder type(de.unistuttgart.iste.ps.skillls.tools.Type type) {
            instance.setType(type);
            return this;
        }

        public FieldBuilder hints(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> hints) {
            instance.setHints(hints);
            return this;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.Field, de.unistuttgart.iste.ps.skillls.tools.HintParent> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skillls.tools.Field.SubType, de.unistuttgart.iste.ps.skillls.tools.HintParent> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skillls.tools.Field.SubType, de.unistuttgart.iste.ps.skillls.tools.HintParent> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.Field.SubType, de.unistuttgart.iste.ps.skillls.tools.HintParent> makeSubPool(int index, String name) {
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
                de.unistuttgart.iste.ps.skillls.tools.Field.SubType r = new de.unistuttgart.iste.ps.skillls.tools.Field.SubType(this, i + 1);
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
