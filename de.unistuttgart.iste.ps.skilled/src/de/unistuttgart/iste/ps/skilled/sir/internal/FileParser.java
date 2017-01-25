/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 18.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir.internal;

import java.util.Collections;
import java.util.HashSet;

import de.ust.skill.common.java.api.SkillException;
import de.ust.skill.common.java.api.SkillFile.Mode;
import de.ust.skill.common.java.internal.BasePool;
import de.ust.skill.common.java.internal.ParseException;
import de.ust.skill.common.java.internal.SkillObject;
import de.ust.skill.common.java.internal.StoragePool;
import de.ust.skill.common.java.restrictions.TypeRestriction;
import de.ust.skill.common.jvm.streams.FileInputStream;

final public class FileParser extends de.ust.skill.common.java.internal.FileParser<SkillState> {

    public final SkillState state;

    /**
     * Constructs a parser that parses the file from in and constructs the
     * state. State is valid immediately after construction.
     */
    private FileParser(FileInputStream in, Mode writeMode) throws ParseException {
        super(in);

        // parse blocks
        while (!in.eof()) {
            stringBlock();
            typeBlock();
        }

        this.state = makeState(writeMode);
    }

    /**
     * turns a file into a state.
     *
     * @note this method is abstract, because some methods, including state
     *       allocation depend on the specification
     */
    public static SkillState read(FileInputStream in, Mode writeMode) throws ParseException {
        FileParser p = new FileParser(in, writeMode);
        return p.state;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends B, B extends SkillObject> StoragePool<T, B> newPool(String name,
            StoragePool<? super T, B> superPool, HashSet<TypeRestriction> restrictions) {
        final StoragePool<T, B> p;
        // allocate correct pool type
        switch (name) {
        case "buildinformation":
            p = (StoragePool<T, B>) new BuildInformationAccess(types.size());
            break;


        case "comment":
            p = (StoragePool<T, B>) new CommentAccess(types.size());
            break;


        case "commenttag":
            p = (StoragePool<T, B>) new CommentTagAccess(types.size());
            break;


        case "customfieldoption":
            p = (StoragePool<T, B>) new CustomFieldOptionAccess(types.size());
            break;


        case "fieldlike":
            p = (StoragePool<T, B>) new FieldLikeAccess(types.size());
            break;


        case "customfield": {
            FieldLikeAccess parent = (FieldLikeAccess)(poolByName.get("fieldlike"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type CustomField");
            p = (StoragePool<T, B>) new CustomFieldAccess(types.size(), parent);
            break;
        }


        case "field": {
            FieldLikeAccess parent = (FieldLikeAccess)(poolByName.get("fieldlike"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type Field");
            p = (StoragePool<T, B>) new FieldAccess(types.size(), parent);
            break;
        }


        case "fieldview": {
            FieldLikeAccess parent = (FieldLikeAccess)(poolByName.get("fieldlike"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type FieldView");
            p = (StoragePool<T, B>) new FieldViewAccess(types.size(), parent);
            break;
        }


        case "filepath":
            p = (StoragePool<T, B>) new FilePathAccess(types.size());
            break;


        case "hint":
            p = (StoragePool<T, B>) new HintAccess(types.size());
            break;


        case "identifier":
            p = (StoragePool<T, B>) new IdentifierAccess(types.size());
            break;


        case "restriction":
            p = (StoragePool<T, B>) new RestrictionAccess(types.size());
            break;


        case "tool":
            p = (StoragePool<T, B>) new ToolAccess(types.size());
            break;


        case "tooltypecustomization":
            p = (StoragePool<T, B>) new ToolTypeCustomizationAccess(types.size());
            break;


        case "type":
            p = (StoragePool<T, B>) new TypeAccess(types.size());
            break;


        case "builtintype": {
            TypeAccess parent = (TypeAccess)(poolByName.get("type"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type BuiltinType");
            p = (StoragePool<T, B>) new BuiltinTypeAccess(types.size(), parent);
            break;
        }


        case "maptype": {
            BuiltinTypeAccess parent = (BuiltinTypeAccess)(poolByName.get("builtintype"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type MapType");
            p = (StoragePool<T, B>) new MapTypeAccess(types.size(), parent);
            break;
        }


        case "singlebasetypecontainer": {
            BuiltinTypeAccess parent = (BuiltinTypeAccess)(poolByName.get("builtintype"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type SingleBaseTypeContainer");
            p = (StoragePool<T, B>) new SingleBaseTypeContainerAccess(types.size(), parent);
            break;
        }


        case "constantlengtharraytype": {
            SingleBaseTypeContainerAccess parent = (SingleBaseTypeContainerAccess)(poolByName.get("singlebasetypecontainer"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type ConstantLengthArrayType");
            p = (StoragePool<T, B>) new ConstantLengthArrayTypeAccess(types.size(), parent);
            break;
        }


        case "simpletype": {
            BuiltinTypeAccess parent = (BuiltinTypeAccess)(poolByName.get("builtintype"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type SimpleType");
            p = (StoragePool<T, B>) new SimpleTypeAccess(types.size(), parent);
            break;
        }


        case "constantinteger": {
            SimpleTypeAccess parent = (SimpleTypeAccess)(poolByName.get("simpletype"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type ConstantInteger");
            p = (StoragePool<T, B>) new ConstantIntegerAccess(types.size(), parent);
            break;
        }


        case "userdefinedtype": {
            TypeAccess parent = (TypeAccess)(poolByName.get("type"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type UserdefinedType");
            p = (StoragePool<T, B>) new UserdefinedTypeAccess(types.size(), parent);
            break;
        }


        case "classtype": {
            UserdefinedTypeAccess parent = (UserdefinedTypeAccess)(poolByName.get("userdefinedtype"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type ClassType");
            p = (StoragePool<T, B>) new ClassTypeAccess(types.size(), parent);
            break;
        }


        case "enumtype": {
            UserdefinedTypeAccess parent = (UserdefinedTypeAccess)(poolByName.get("userdefinedtype"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type EnumType");
            p = (StoragePool<T, B>) new EnumTypeAccess(types.size(), parent);
            break;
        }


        case "interfacetype": {
            UserdefinedTypeAccess parent = (UserdefinedTypeAccess)(poolByName.get("userdefinedtype"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type InterfaceType");
            p = (StoragePool<T, B>) new InterfaceTypeAccess(types.size(), parent);
            break;
        }


        case "typedefinition": {
            UserdefinedTypeAccess parent = (UserdefinedTypeAccess)(poolByName.get("userdefinedtype"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type TypeDefinition");
            p = (StoragePool<T, B>) new TypeDefinitionAccess(types.size(), parent);
            break;
        }

        default:
            if (null == superPool)
                p = (StoragePool<T, B>) new BasePool<T>(types.size(), name, Collections.EMPTY_SET, noAutoFields());
            else
                p = (StoragePool<T, B>) superPool.makeSubPool(types.size(), name);
            break;
        }

        // check super type expectations
        if (p.superPool() != superPool)
            throw new ParseException(
                    in,
                    blockCounter,
                    null,
                    "The super type of %s stored in the file does not match the specification!\nexpected %s, but was %s",
                    name, null == p.superPool() ? "<none>" : p.superPool().name(), null == superPool ? "<none>"
                            : superPool.name());

        types.add(p);
        poolByName.put(name, p);

        return p;
    }

    private SkillState makeState(Mode mode) {

        // create missing type information
        BuildInformationAccess BuildInformation;
        if (poolByName.containsKey("buildinformation"))
            BuildInformation = (BuildInformationAccess) poolByName.get("buildinformation");
        else {
            BuildInformation = new BuildInformationAccess(types.size());
            types.add(BuildInformation);
            poolByName.put("buildinformation", BuildInformation);
        }

        CommentAccess Comment;
        if (poolByName.containsKey("comment"))
            Comment = (CommentAccess) poolByName.get("comment");
        else {
            Comment = new CommentAccess(types.size());
            types.add(Comment);
            poolByName.put("comment", Comment);
        }

        CommentTagAccess CommentTag;
        if (poolByName.containsKey("commenttag"))
            CommentTag = (CommentTagAccess) poolByName.get("commenttag");
        else {
            CommentTag = new CommentTagAccess(types.size());
            types.add(CommentTag);
            poolByName.put("commenttag", CommentTag);
        }

        CustomFieldOptionAccess CustomFieldOption;
        if (poolByName.containsKey("customfieldoption"))
            CustomFieldOption = (CustomFieldOptionAccess) poolByName.get("customfieldoption");
        else {
            CustomFieldOption = new CustomFieldOptionAccess(types.size());
            types.add(CustomFieldOption);
            poolByName.put("customfieldoption", CustomFieldOption);
        }

        FieldLikeAccess FieldLike;
        if (poolByName.containsKey("fieldlike"))
            FieldLike = (FieldLikeAccess) poolByName.get("fieldlike");
        else {
            FieldLike = new FieldLikeAccess(types.size());
            types.add(FieldLike);
            poolByName.put("fieldlike", FieldLike);
        }

        CustomFieldAccess CustomField;
        if (poolByName.containsKey("customfield"))
            CustomField = (CustomFieldAccess) poolByName.get("customfield");
        else {
            CustomField = new CustomFieldAccess(types.size(), FieldLike);
            types.add(CustomField);
            poolByName.put("customfield", CustomField);
        }

        FieldAccess Field;
        if (poolByName.containsKey("field"))
            Field = (FieldAccess) poolByName.get("field");
        else {
            Field = new FieldAccess(types.size(), FieldLike);
            types.add(Field);
            poolByName.put("field", Field);
        }

        FieldViewAccess FieldView;
        if (poolByName.containsKey("fieldview"))
            FieldView = (FieldViewAccess) poolByName.get("fieldview");
        else {
            FieldView = new FieldViewAccess(types.size(), FieldLike);
            types.add(FieldView);
            poolByName.put("fieldview", FieldView);
        }

        FilePathAccess FilePath;
        if (poolByName.containsKey("filepath"))
            FilePath = (FilePathAccess) poolByName.get("filepath");
        else {
            FilePath = new FilePathAccess(types.size());
            types.add(FilePath);
            poolByName.put("filepath", FilePath);
        }

        HintAccess Hint;
        if (poolByName.containsKey("hint"))
            Hint = (HintAccess) poolByName.get("hint");
        else {
            Hint = new HintAccess(types.size());
            types.add(Hint);
            poolByName.put("hint", Hint);
        }

        IdentifierAccess Identifier;
        if (poolByName.containsKey("identifier"))
            Identifier = (IdentifierAccess) poolByName.get("identifier");
        else {
            Identifier = new IdentifierAccess(types.size());
            types.add(Identifier);
            poolByName.put("identifier", Identifier);
        }

        RestrictionAccess Restriction;
        if (poolByName.containsKey("restriction"))
            Restriction = (RestrictionAccess) poolByName.get("restriction");
        else {
            Restriction = new RestrictionAccess(types.size());
            types.add(Restriction);
            poolByName.put("restriction", Restriction);
        }

        ToolAccess Tool;
        if (poolByName.containsKey("tool"))
            Tool = (ToolAccess) poolByName.get("tool");
        else {
            Tool = new ToolAccess(types.size());
            types.add(Tool);
            poolByName.put("tool", Tool);
        }

        ToolTypeCustomizationAccess ToolTypeCustomization;
        if (poolByName.containsKey("tooltypecustomization"))
            ToolTypeCustomization = (ToolTypeCustomizationAccess) poolByName.get("tooltypecustomization");
        else {
            ToolTypeCustomization = new ToolTypeCustomizationAccess(types.size());
            types.add(ToolTypeCustomization);
            poolByName.put("tooltypecustomization", ToolTypeCustomization);
        }

        TypeAccess Type;
        if (poolByName.containsKey("type"))
            Type = (TypeAccess) poolByName.get("type");
        else {
            Type = new TypeAccess(types.size());
            types.add(Type);
            poolByName.put("type", Type);
        }

        BuiltinTypeAccess BuiltinType;
        if (poolByName.containsKey("builtintype"))
            BuiltinType = (BuiltinTypeAccess) poolByName.get("builtintype");
        else {
            BuiltinType = new BuiltinTypeAccess(types.size(), Type);
            types.add(BuiltinType);
            poolByName.put("builtintype", BuiltinType);
        }

        MapTypeAccess MapType;
        if (poolByName.containsKey("maptype"))
            MapType = (MapTypeAccess) poolByName.get("maptype");
        else {
            MapType = new MapTypeAccess(types.size(), BuiltinType);
            types.add(MapType);
            poolByName.put("maptype", MapType);
        }

        SingleBaseTypeContainerAccess SingleBaseTypeContainer;
        if (poolByName.containsKey("singlebasetypecontainer"))
            SingleBaseTypeContainer = (SingleBaseTypeContainerAccess) poolByName.get("singlebasetypecontainer");
        else {
            SingleBaseTypeContainer = new SingleBaseTypeContainerAccess(types.size(), BuiltinType);
            types.add(SingleBaseTypeContainer);
            poolByName.put("singlebasetypecontainer", SingleBaseTypeContainer);
        }

        ConstantLengthArrayTypeAccess ConstantLengthArrayType;
        if (poolByName.containsKey("constantlengtharraytype"))
            ConstantLengthArrayType = (ConstantLengthArrayTypeAccess) poolByName.get("constantlengtharraytype");
        else {
            ConstantLengthArrayType = new ConstantLengthArrayTypeAccess(types.size(), SingleBaseTypeContainer);
            types.add(ConstantLengthArrayType);
            poolByName.put("constantlengtharraytype", ConstantLengthArrayType);
        }

        SimpleTypeAccess SimpleType;
        if (poolByName.containsKey("simpletype"))
            SimpleType = (SimpleTypeAccess) poolByName.get("simpletype");
        else {
            SimpleType = new SimpleTypeAccess(types.size(), BuiltinType);
            types.add(SimpleType);
            poolByName.put("simpletype", SimpleType);
        }

        ConstantIntegerAccess ConstantInteger;
        if (poolByName.containsKey("constantinteger"))
            ConstantInteger = (ConstantIntegerAccess) poolByName.get("constantinteger");
        else {
            ConstantInteger = new ConstantIntegerAccess(types.size(), SimpleType);
            types.add(ConstantInteger);
            poolByName.put("constantinteger", ConstantInteger);
        }

        UserdefinedTypeAccess UserdefinedType;
        if (poolByName.containsKey("userdefinedtype"))
            UserdefinedType = (UserdefinedTypeAccess) poolByName.get("userdefinedtype");
        else {
            UserdefinedType = new UserdefinedTypeAccess(types.size(), Type);
            types.add(UserdefinedType);
            poolByName.put("userdefinedtype", UserdefinedType);
        }

        ClassTypeAccess ClassType;
        if (poolByName.containsKey("classtype"))
            ClassType = (ClassTypeAccess) poolByName.get("classtype");
        else {
            ClassType = new ClassTypeAccess(types.size(), UserdefinedType);
            types.add(ClassType);
            poolByName.put("classtype", ClassType);
        }

        EnumTypeAccess EnumType;
        if (poolByName.containsKey("enumtype"))
            EnumType = (EnumTypeAccess) poolByName.get("enumtype");
        else {
            EnumType = new EnumTypeAccess(types.size(), UserdefinedType);
            types.add(EnumType);
            poolByName.put("enumtype", EnumType);
        }

        InterfaceTypeAccess InterfaceType;
        if (poolByName.containsKey("interfacetype"))
            InterfaceType = (InterfaceTypeAccess) poolByName.get("interfacetype");
        else {
            InterfaceType = new InterfaceTypeAccess(types.size(), UserdefinedType);
            types.add(InterfaceType);
            poolByName.put("interfacetype", InterfaceType);
        }

        TypeDefinitionAccess TypeDefinition;
        if (poolByName.containsKey("typedefinition"))
            TypeDefinition = (TypeDefinitionAccess) poolByName.get("typedefinition");
        else {
            TypeDefinition = new TypeDefinitionAccess(types.size(), UserdefinedType);
            types.add(TypeDefinition);
            poolByName.put("typedefinition", TypeDefinition);
        }

        // make state
        SkillState r = new SkillState(poolByName, Strings, StringType, Annotation, types, in.path(), mode);
        try {
            r.check();
        } catch (SkillException e) {
            throw new ParseException(in, blockCounter, e, "Post serialization check failed!");
        }
        return r;
    }
}
