/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 16.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir;

import de.ust.skill.common.java.api.FieldDeclaration;
import de.ust.skill.common.java.internal.NamedType;
import de.ust.skill.common.java.internal.SkillObject;
import de.ust.skill.common.java.internal.StoragePool;


public class Tool extends SkillObject {
    private static final long serialVersionUID = 0x5c11L + ((long) "tool".hashCode()) << 32;

    @Override
    public String skillName() {
        return "tool";
    }

    /**
     * Create a new unmanaged Tool. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public Tool() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public Tool(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public Tool(long skillID, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.BuildInformation> buildTargets, java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> customFieldAnnotations, java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> customTypeAnnotations, java.lang.String description, java.lang.String name, java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, java.util.HashMap<java.lang.String, de.unistuttgart.iste.ps.skilled.sir.FieldLike>> selectedFields, java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType> selectedUserTypes) {
        super(skillID);
        this.buildTargets = buildTargets;
        this.customFieldAnnotations = customFieldAnnotations;
        this.customTypeAnnotations = customTypeAnnotations;
        this.description = description;
        this.name = name;
        this.selectedFields = selectedFields;
        this.selectedUserTypes = selectedUserTypes;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.BuildInformation> buildTargets = null;

    /**
     *  build targets associated with this tool
     */
    final public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.BuildInformation> getBuildTargets() {
        return buildTargets;
    }

    /**
     *  build targets associated with this tool
     */
    final public void setBuildTargets(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.BuildInformation> buildTargets) {
        this.buildTargets = buildTargets;
    }

    protected java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> customFieldAnnotations = null;

    /**
     *  overrides existing annotations
     */
    final public java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> getCustomFieldAnnotations() {
        return customFieldAnnotations;
    }

    /**
     *  overrides existing annotations
     */
    final public void setCustomFieldAnnotations(java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> customFieldAnnotations) {
        this.customFieldAnnotations = customFieldAnnotations;
    }

    protected java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> customTypeAnnotations = null;

    /**
     *  overrides existing annotations
     */
    final public java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> getCustomTypeAnnotations() {
        return customTypeAnnotations;
    }

    /**
     *  overrides existing annotations
     */
    final public void setCustomTypeAnnotations(java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> customTypeAnnotations) {
        this.customTypeAnnotations = customTypeAnnotations;
    }

    protected java.lang.String description = null;

    final public java.lang.String getDescription() {
        return description;
    }

    final public void setDescription(java.lang.String description) {
        this.description = description;
    }

    protected java.lang.String name = null;

    final public java.lang.String getName() {
        return name;
    }

    final public void setName(java.lang.String name) {
        this.name = name;
    }

    protected java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, java.util.HashMap<java.lang.String, de.unistuttgart.iste.ps.skilled.sir.FieldLike>> selectedFields = null;

    /**
     *  The set of fields selected by this tool. The string argument is used to ensure, that selected fields have unique
     *  names.
     */
    final public java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, java.util.HashMap<java.lang.String, de.unistuttgart.iste.ps.skilled.sir.FieldLike>> getSelectedFields() {
        return selectedFields;
    }

    /**
     *  The set of fields selected by this tool. The string argument is used to ensure, that selected fields have unique
     *  names.
     */
    final public void setSelectedFields(java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType, java.util.HashMap<java.lang.String, de.unistuttgart.iste.ps.skilled.sir.FieldLike>> selectedFields) {
        this.selectedFields = selectedFields;
    }

    protected java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType> selectedUserTypes = null;

    /**
     *  the set of user types selected by this tool
     */
    final public java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType> getSelectedUserTypes() {
        return selectedUserTypes;
    }

    /**
     *  the set of user types selected by this tool
     */
    final public void setSelectedUserTypes(java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.UserdefinedType> selectedUserTypes) {
        this.selectedUserTypes = selectedUserTypes;
    }

    /**
     * unchecked conversions are required, because the Java type system known
     * nothing of our invariants
     * 
     * @note to self: Boxing bei primitiven beachten!
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(FieldDeclaration<T> field) {
        switch (field.name()) {
        case "buildtargets":
            return (T) buildTargets;
        case "customfieldannotations":
            return (T) customFieldAnnotations;
        case "customtypeannotations":
            return (T) customTypeAnnotations;
        case "description":
            return (T) description;
        case "name":
            return (T) name;
        case "selectedfields":
            return (T) selectedFields;
        case "selectedusertypes":
            return (T) selectedUserTypes;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("Tool(this: ").append(this);
        sb.append(", buildTargets: ").append(buildTargets);
        sb.append(", customFieldAnnotations: ").append(customFieldAnnotations);
        sb.append(", customTypeAnnotations: ").append(customTypeAnnotations);
        sb.append(", description: ").append(description);
        sb.append(", name: ").append(name);
        sb.append(", selectedFields: ").append(selectedFields);
        sb.append(", selectedUserTypes: ").append(selectedUserTypes);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends Tool implements NamedType {
        private final StoragePool<?, ?> τPool;

        /** internal use only!!! */
        public SubType(StoragePool<?, ?> τPool, long skillID) {
            super(skillID);
            this.τPool = τPool;
        }

        @Override
        public StoragePool<?, ?> τPool() {
          return τPool;
        }

        @Override
        public String skillName() {
            return τPool.name();
        }

        @Override
        public String toString() {
            return skillName() + "#" + skillID;
        }
    }
}
