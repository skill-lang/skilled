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

public class ToolAccess extends BasePool<de.unistuttgart.iste.ps.skilled.sir.Tool> {

    @Override
    final protected de.unistuttgart.iste.ps.skilled.sir.Tool[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skilled.sir.Tool[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    ToolAccess(int poolIndex) {
        super(poolIndex, "tool", new HashSet<String>(Arrays.asList(new String[] { "buildtargets", "customfieldannotations", "customtypeannotations", "description", "name", "selectedfields", "selectedusertypes" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skilled.sir.Tool[] data() {
        return data;
    }

    @Override
    public void insertInstances() {
        final Block last = blocks().getLast();
        int i = (int) last.bpo;
        int high = (int) (last.bpo + last.count);
        while (i < high) {
            if (null != data[i])
                return;

            de.unistuttgart.iste.ps.skilled.sir.Tool r = new de.unistuttgart.iste.ps.skilled.sir.Tool(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.Tool> f;
        switch (name) {
        case "buildtargets":
            f = new KnownField_Tool_buildTargets(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.BuildInformation>)(owner().poolByName().get("buildinformation"))), 1 + dataFields.size(), this);
            break;

        case "customfieldannotations":
            f = new KnownField_Tool_customFieldAnnotations(new MapType<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.FieldLike>)(owner().poolByName().get("fieldlike")), (FieldType<de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization>)(owner().poolByName().get("tooltypecustomization"))), 1 + dataFields.size(), this);
            break;

        case "customtypeannotations":
            f = new KnownField_Tool_customTypeAnnotations(new MapType<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType>)(owner().poolByName().get("userdefinedtype")), (FieldType<de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization>)(owner().poolByName().get("tooltypecustomization"))), 1 + dataFields.size(), this);
            break;

        case "description":
            f = new KnownField_Tool_description(string, 1 + dataFields.size(), this);
            break;

        case "name":
            f = new KnownField_Tool_name(string, 1 + dataFields.size(), this);
            break;

        case "selectedfields":
            f = new KnownField_Tool_selectedFields(new MapType<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType>)(owner().poolByName().get("userdefinedtype")), new MapType<>(string, (FieldType<de.unistuttgart.iste.ps.skilled.sir.FieldLike>)(owner().poolByName().get("fieldlike")))), 1 + dataFields.size(), this);
            break;

        case "selectedusertypes":
            f = new KnownField_Tool_selectedUserTypes(new SetType<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType>)(owner().poolByName().get("userdefinedtype"))), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Tool> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Tool> f;
        switch (name) {
        case "buildtargets":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Tool>) new KnownField_Tool_buildTargets((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.BuildInformation>>) type, ID, this);
            break;

        case "customfieldannotations":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Tool>) new KnownField_Tool_customFieldAnnotations((FieldType<java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization>>) type, ID, this);
            break;

        case "customtypeannotations":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Tool>) new KnownField_Tool_customTypeAnnotations((FieldType<java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization>>) type, ID, this);
            break;

        case "description":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Tool>) new KnownField_Tool_description((FieldType<java.lang.String>) type, ID, this);
            break;

        case "name":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Tool>) new KnownField_Tool_name((FieldType<java.lang.String>) type, ID, this);
            break;

        case "selectedfields":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Tool>) new KnownField_Tool_selectedFields((FieldType<java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, java.util.HashMap<java.lang.String, de.unistuttgart.iste.ps.skilled.sir.FieldLike>>>) type, ID, this);
            break;

        case "selectedusertypes":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Tool>) new KnownField_Tool_selectedUserTypes((FieldType<java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType>>) type, ID, this);
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
     * @return a new Tool instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.Tool make() {
        de.unistuttgart.iste.ps.skilled.sir.Tool rval = new de.unistuttgart.iste.ps.skilled.sir.Tool();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.Tool make(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.BuildInformation> buildTargets, java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> customFieldAnnotations, java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> customTypeAnnotations, java.lang.String description, java.lang.String name, java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, java.util.HashMap<java.lang.String, de.unistuttgart.iste.ps.skilled.sir.FieldLike>> selectedFields, java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType> selectedUserTypes) {
        de.unistuttgart.iste.ps.skilled.sir.Tool rval = new de.unistuttgart.iste.ps.skilled.sir.Tool(-1, buildTargets, customFieldAnnotations, customTypeAnnotations, description, name, selectedFields, selectedUserTypes);
        add(rval);
        return rval;
    }

    public ToolBuilder build() {
        return new ToolBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.Tool());
    }

    /**
     * Builder for new Tool instances.
     *
     * @author Timm Felden
     */
    public static final class ToolBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.Tool> {

        protected ToolBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.Tool, ? super de.unistuttgart.iste.ps.skilled.sir.Tool> pool, de.unistuttgart.iste.ps.skilled.sir.Tool instance) {
            super(pool, instance);
        }

        public ToolBuilder buildTargets(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.BuildInformation> buildTargets) {
            instance.setBuildTargets(buildTargets);
            return this;
        }

        public ToolBuilder customFieldAnnotations(java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> customFieldAnnotations) {
            instance.setCustomFieldAnnotations(customFieldAnnotations);
            return this;
        }

        public ToolBuilder customTypeAnnotations(java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> customTypeAnnotations) {
            instance.setCustomTypeAnnotations(customTypeAnnotations);
            return this;
        }

        public ToolBuilder description(java.lang.String description) {
            instance.setDescription(description);
            return this;
        }

        public ToolBuilder name(java.lang.String name) {
            instance.setName(name);
            return this;
        }

        public ToolBuilder selectedFields(java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, java.util.HashMap<java.lang.String, de.unistuttgart.iste.ps.skilled.sir.FieldLike>> selectedFields) {
            instance.setSelectedFields(selectedFields);
            return this;
        }

        public ToolBuilder selectedUserTypes(java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType> selectedUserTypes) {
            instance.setSelectedUserTypes(selectedUserTypes);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.Tool make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.Tool rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.Tool, de.unistuttgart.iste.ps.skilled.sir.Tool> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.Tool.SubType, de.unistuttgart.iste.ps.skilled.sir.Tool> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.Tool.SubType, de.unistuttgart.iste.ps.skilled.sir.Tool> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.Tool.SubType, de.unistuttgart.iste.ps.skilled.sir.Tool> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.Tool[] data = ((ToolAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.Tool.SubType r = new de.unistuttgart.iste.ps.skilled.sir.Tool.SubType(this, i + 1);
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
