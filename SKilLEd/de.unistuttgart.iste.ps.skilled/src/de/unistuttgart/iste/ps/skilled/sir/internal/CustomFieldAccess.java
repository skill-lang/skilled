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
 *  a language custom field
 */
public class CustomFieldAccess extends SubPool<de.unistuttgart.iste.ps.skilled.sir.CustomField, de.unistuttgart.iste.ps.skilled.sir.FieldLike> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    CustomFieldAccess(int poolIndex, FieldLikeAccess superPool) {
        super(poolIndex, "customfield", superPool, new HashSet<String>(Arrays.asList(new String[] { "language", "options", "typename" })), noAutoFields());
    }

    @Override
    public void insertInstances() {
        de.unistuttgart.iste.ps.skilled.sir.FieldLike[] data = ((FieldLikeAccess)basePool).data();
        final Block last = blocks().getLast();
        int i = (int) last.bpo;
        int high = (int) (last.bpo + last.count);
        while (i < high) {
            if (null != data[i])
                return;

            de.unistuttgart.iste.ps.skilled.sir.CustomField r = new de.unistuttgart.iste.ps.skilled.sir.CustomField(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.CustomField> f;
        switch (name) {
        case "language":
            f = new KnownField_CustomField_language(string, 1 + dataFields.size(), this);
            break;

        case "options":
            f = new KnownField_CustomField_options(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption>)(owner().poolByName().get("customfieldoption"))), 1 + dataFields.size(), this);
            break;

        case "typename":
            f = new KnownField_CustomField_typename(string, 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.CustomField> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.CustomField> f;
        switch (name) {
        case "language":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.CustomField>) new KnownField_CustomField_language((FieldType<java.lang.String>) type, ID, this);
            break;

        case "options":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.CustomField>) new KnownField_CustomField_options((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption>>) type, ID, this);
            break;

        case "typename":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.CustomField>) new KnownField_CustomField_typename((FieldType<java.lang.String>) type, ID, this);
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
     * @return a new CustomField instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.CustomField make() {
        de.unistuttgart.iste.ps.skilled.sir.CustomField rval = new de.unistuttgart.iste.ps.skilled.sir.CustomField();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.CustomField make(java.lang.String language, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> options, java.lang.String typename, de.unistuttgart.iste.ps.skilled.sir.Comment comment, de.unistuttgart.iste.ps.skilled.sir.Identifier name, de.unistuttgart.iste.ps.skilled.sir.Type type, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        de.unistuttgart.iste.ps.skilled.sir.CustomField rval = new de.unistuttgart.iste.ps.skilled.sir.CustomField(-1, language, options, typename, comment, name, type, hints, restrictions);
        add(rval);
        return rval;
    }

    public CustomFieldBuilder build() {
        return new CustomFieldBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.CustomField());
    }

    /**
     * Builder for new CustomField instances.
     *
     * @author Timm Felden
     */
    public static final class CustomFieldBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.CustomField> {

        protected CustomFieldBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.CustomField, ? super de.unistuttgart.iste.ps.skilled.sir.CustomField> pool, de.unistuttgart.iste.ps.skilled.sir.CustomField instance) {
            super(pool, instance);
        }

        public CustomFieldBuilder language(java.lang.String language) {
            instance.setLanguage(language);
            return this;
        }

        public CustomFieldBuilder options(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> options) {
            instance.setOptions(options);
            return this;
        }

        public CustomFieldBuilder typename(java.lang.String typename) {
            instance.setTypename(typename);
            return this;
        }

        public CustomFieldBuilder comment(de.unistuttgart.iste.ps.skilled.sir.Comment comment) {
            instance.setComment(comment);
            return this;
        }

        public CustomFieldBuilder name(de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
            instance.setName(name);
            return this;
        }

        public CustomFieldBuilder type(de.unistuttgart.iste.ps.skilled.sir.Type type) {
            instance.setType(type);
            return this;
        }

        public CustomFieldBuilder hints(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints) {
            instance.setHints(hints);
            return this;
        }

        public CustomFieldBuilder restrictions(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
            instance.setRestrictions(restrictions);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.CustomField make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.CustomField rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.CustomField, de.unistuttgart.iste.ps.skilled.sir.FieldLike> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.CustomField.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.CustomField.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.CustomField.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.FieldLike[] data = ((FieldLikeAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.CustomField.SubType r = new de.unistuttgart.iste.ps.skilled.sir.CustomField.SubType(this, i + 1);
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
