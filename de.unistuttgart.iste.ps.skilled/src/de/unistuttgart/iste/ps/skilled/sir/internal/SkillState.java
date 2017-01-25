/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 18.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import de.ust.skill.common.java.api.Access;
import de.ust.skill.common.java.api.FieldDeclaration;
import de.ust.skill.common.java.api.SkillException;
import de.ust.skill.common.java.internal.BasePool;
import de.ust.skill.common.java.internal.SkillObject;
import de.ust.skill.common.java.internal.StoragePool;
import de.ust.skill.common.java.internal.StringPool;
import de.ust.skill.common.java.internal.fieldTypes.Annotation;
import de.ust.skill.common.java.internal.fieldTypes.StringType;
import de.ust.skill.common.jvm.streams.FileInputStream;

import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;

/**
 * Internal implementation of SkillFile.
 *
 * @author Timm Felden
 * @note type access fields start with a capital letter to avoid collisions
 */
public final class SkillState extends de.ust.skill.common.java.internal.SkillState implements SkillFile {

    // types by skill name
    private final HashMap<String, StoragePool<?, ?>> poolByName;

    @Override
    public HashMap<String, StoragePool<?, ?>> poolByName() {
        return poolByName;
    }

    /**
     * Create a new skill file based on argument path and mode.
     *
     * @throws IOException
     *             on IO and mode related errors
     * @throws SkillException
     *             on file or specification consistency errors
     */
    public static SkillState open(String path, Mode... mode) throws IOException, SkillException {
        File f = new File(path);
        return open(f, mode);
    }

    /**
     * Create a new skill file based on argument path and mode.
     *
     * @throws IOException
     *             on IO and mode related errors
     * @throws SkillException
     *             on file or specification consistency errors
     */
    public static SkillState open(File path, Mode... mode) throws IOException, SkillException {
        for (Mode m : mode) {
            if (m == Mode.Create && !path.exists())
                path.createNewFile();
        }
        assert path.exists() : "can only open files that already exist in genarel, because of java.nio restrictions";
        return open(path.toPath(), mode);
    }

    /**
     * Create a new skill file based on argument path and mode.
     *
     * @throws IOException
     *             on IO and mode related errors
     * @throws SkillException
     *             on file or specification consistency errors
     * @note suppress unused warnings, because sometimes type declarations are
     *       created, although nobody is using them
     */
    @SuppressWarnings("unused")
    public static SkillState open(Path path, Mode... mode) throws IOException, SkillException {
        ActualMode actualMode = new ActualMode(mode);
        switch (actualMode.open) {
        case Create:
            // initialization order of type information has to match file parser
            // and can not be done in place
            StringPool strings = new StringPool(null);
            ArrayList<StoragePool<?, ?>> types = new ArrayList<>(1);
            StringType stringType = new StringType(strings);
            Annotation annotation = new Annotation(types);

            // create type information
            BuildInformationAccess BuildInformation = new BuildInformationAccess(0);
            types.add(BuildInformation);
            CommentAccess Comment = new CommentAccess(1);
            types.add(Comment);
            CommentTagAccess CommentTag = new CommentTagAccess(2);
            types.add(CommentTag);
            CustomFieldOptionAccess CustomFieldOption = new CustomFieldOptionAccess(3);
            types.add(CustomFieldOption);
            FieldLikeAccess FieldLike = new FieldLikeAccess(4);
            types.add(FieldLike);
            CustomFieldAccess CustomField = new CustomFieldAccess(5, FieldLike);
            types.add(CustomField);
            FieldAccess Field = new FieldAccess(6, FieldLike);
            types.add(Field);
            FieldViewAccess FieldView = new FieldViewAccess(7, FieldLike);
            types.add(FieldView);
            FilePathAccess FilePath = new FilePathAccess(8);
            types.add(FilePath);
            HintAccess Hint = new HintAccess(9);
            types.add(Hint);
            IdentifierAccess Identifier = new IdentifierAccess(10);
            types.add(Identifier);
            RestrictionAccess Restriction = new RestrictionAccess(11);
            types.add(Restriction);
            ToolAccess Tool = new ToolAccess(12);
            types.add(Tool);
            ToolTypeCustomizationAccess ToolTypeCustomization = new ToolTypeCustomizationAccess(13);
            types.add(ToolTypeCustomization);
            TypeAccess Type = new TypeAccess(14);
            types.add(Type);
            BuiltinTypeAccess BuiltinType = new BuiltinTypeAccess(15, Type);
            types.add(BuiltinType);
            MapTypeAccess MapType = new MapTypeAccess(16, BuiltinType);
            types.add(MapType);
            SingleBaseTypeContainerAccess SingleBaseTypeContainer = new SingleBaseTypeContainerAccess(17, BuiltinType);
            types.add(SingleBaseTypeContainer);
            ConstantLengthArrayTypeAccess ConstantLengthArrayType = new ConstantLengthArrayTypeAccess(18, SingleBaseTypeContainer);
            types.add(ConstantLengthArrayType);
            SimpleTypeAccess SimpleType = new SimpleTypeAccess(19, BuiltinType);
            types.add(SimpleType);
            ConstantIntegerAccess ConstantInteger = new ConstantIntegerAccess(20, SimpleType);
            types.add(ConstantInteger);
            UserdefinedTypeAccess UserdefinedType = new UserdefinedTypeAccess(21, Type);
            types.add(UserdefinedType);
            ClassTypeAccess ClassType = new ClassTypeAccess(22, UserdefinedType);
            types.add(ClassType);
            EnumTypeAccess EnumType = new EnumTypeAccess(23, UserdefinedType);
            types.add(EnumType);
            InterfaceTypeAccess InterfaceType = new InterfaceTypeAccess(24, UserdefinedType);
            types.add(InterfaceType);
            TypeDefinitionAccess TypeDefinition = new TypeDefinitionAccess(25, UserdefinedType);
            types.add(TypeDefinition);
            return new SkillState(strings, types, stringType, annotation, path, actualMode.close);

        case Read:
            return FileParser.read(FileInputStream.open(path, actualMode.close == Mode.ReadOnly), actualMode.close);

        default:
            throw new IllegalStateException("should never happen");
        }
    }

    public SkillState(StringPool strings, ArrayList<StoragePool<?, ?>> types, StringType stringType,
            Annotation annotationType, Path path, Mode mode) {
        super(strings, path, mode, types, stringType, annotationType);
        poolByName = new HashMap<>();
        for (StoragePool<?, ?> p : types)
            poolByName.put(p.name(), p);

        BuildInformations = (BuildInformationAccess) poolByName.get("buildinformation");

        Comments = (CommentAccess) poolByName.get("comment");

        CommentTags = (CommentTagAccess) poolByName.get("commenttag");

        CustomFieldOptions = (CustomFieldOptionAccess) poolByName.get("customfieldoption");

        FieldLikes = (FieldLikeAccess) poolByName.get("fieldlike");

        CustomFields = (CustomFieldAccess) poolByName.get("customfield");

        Fields = (FieldAccess) poolByName.get("field");

        FieldViews = (FieldViewAccess) poolByName.get("fieldview");

        FilePaths = (FilePathAccess) poolByName.get("filepath");

        Hints = (HintAccess) poolByName.get("hint");

        Identifiers = (IdentifierAccess) poolByName.get("identifier");

        Restrictions = (RestrictionAccess) poolByName.get("restriction");

        Tools = (ToolAccess) poolByName.get("tool");

        ToolTypeCustomizations = (ToolTypeCustomizationAccess) poolByName.get("tooltypecustomization");

        Types = (TypeAccess) poolByName.get("type");

        BuiltinTypes = (BuiltinTypeAccess) poolByName.get("builtintype");

        MapTypes = (MapTypeAccess) poolByName.get("maptype");

        SingleBaseTypeContainers = (SingleBaseTypeContainerAccess) poolByName.get("singlebasetypecontainer");

        ConstantLengthArrayTypes = (ConstantLengthArrayTypeAccess) poolByName.get("constantlengtharraytype");

        SimpleTypes = (SimpleTypeAccess) poolByName.get("simpletype");

        ConstantIntegers = (ConstantIntegerAccess) poolByName.get("constantinteger");

        UserdefinedTypes = (UserdefinedTypeAccess) poolByName.get("userdefinedtype");

        ClassTypes = (ClassTypeAccess) poolByName.get("classtype");

        EnumTypes = (EnumTypeAccess) poolByName.get("enumtype");

        InterfaceTypes = (InterfaceTypeAccess) poolByName.get("interfacetype");

        TypeDefinitions = (TypeDefinitionAccess) poolByName.get("typedefinition");


        finalizePools();
    }

    public SkillState(HashMap<String, StoragePool<?, ?>> poolByName, StringPool strings, StringType stringType,
            Annotation annotationType,
            ArrayList<StoragePool<?, ?>> types, Path path, Mode mode) {
        super(strings, path, mode, types, stringType, annotationType);
        this.poolByName = poolByName;

        BuildInformations = (BuildInformationAccess) poolByName.get("buildinformation");
        Comments = (CommentAccess) poolByName.get("comment");
        CommentTags = (CommentTagAccess) poolByName.get("commenttag");
        CustomFieldOptions = (CustomFieldOptionAccess) poolByName.get("customfieldoption");
        FieldLikes = (FieldLikeAccess) poolByName.get("fieldlike");
        CustomFields = (CustomFieldAccess) poolByName.get("customfield");
        Fields = (FieldAccess) poolByName.get("field");
        FieldViews = (FieldViewAccess) poolByName.get("fieldview");
        FilePaths = (FilePathAccess) poolByName.get("filepath");
        Hints = (HintAccess) poolByName.get("hint");
        Identifiers = (IdentifierAccess) poolByName.get("identifier");
        Restrictions = (RestrictionAccess) poolByName.get("restriction");
        Tools = (ToolAccess) poolByName.get("tool");
        ToolTypeCustomizations = (ToolTypeCustomizationAccess) poolByName.get("tooltypecustomization");
        Types = (TypeAccess) poolByName.get("type");
        BuiltinTypes = (BuiltinTypeAccess) poolByName.get("builtintype");
        MapTypes = (MapTypeAccess) poolByName.get("maptype");
        SingleBaseTypeContainers = (SingleBaseTypeContainerAccess) poolByName.get("singlebasetypecontainer");
        ConstantLengthArrayTypes = (ConstantLengthArrayTypeAccess) poolByName.get("constantlengtharraytype");
        SimpleTypes = (SimpleTypeAccess) poolByName.get("simpletype");
        ConstantIntegers = (ConstantIntegerAccess) poolByName.get("constantinteger");
        UserdefinedTypes = (UserdefinedTypeAccess) poolByName.get("userdefinedtype");
        ClassTypes = (ClassTypeAccess) poolByName.get("classtype");
        EnumTypes = (EnumTypeAccess) poolByName.get("enumtype");
        InterfaceTypes = (InterfaceTypeAccess) poolByName.get("interfacetype");
        TypeDefinitions = (TypeDefinitionAccess) poolByName.get("typedefinition");

        finalizePools();
    }

    private final BuildInformationAccess BuildInformations;

    @Override
    public BuildInformationAccess BuildInformations() {
        return BuildInformations;
    }

    private final CommentAccess Comments;

    @Override
    public CommentAccess Comments() {
        return Comments;
    }

    private final CommentTagAccess CommentTags;

    @Override
    public CommentTagAccess CommentTags() {
        return CommentTags;
    }

    private final CustomFieldOptionAccess CustomFieldOptions;

    @Override
    public CustomFieldOptionAccess CustomFieldOptions() {
        return CustomFieldOptions;
    }

    private final FieldLikeAccess FieldLikes;

    @Override
    public FieldLikeAccess FieldLikes() {
        return FieldLikes;
    }

    private final CustomFieldAccess CustomFields;

    @Override
    public CustomFieldAccess CustomFields() {
        return CustomFields;
    }

    private final FieldAccess Fields;

    @Override
    public FieldAccess Fields() {
        return Fields;
    }

    private final FieldViewAccess FieldViews;

    @Override
    public FieldViewAccess FieldViews() {
        return FieldViews;
    }

    private final FilePathAccess FilePaths;

    @Override
    public FilePathAccess FilePaths() {
        return FilePaths;
    }

    private final HintAccess Hints;

    @Override
    public HintAccess Hints() {
        return Hints;
    }

    private final IdentifierAccess Identifiers;

    @Override
    public IdentifierAccess Identifiers() {
        return Identifiers;
    }

    private final RestrictionAccess Restrictions;

    @Override
    public RestrictionAccess Restrictions() {
        return Restrictions;
    }

    private final ToolAccess Tools;

    @Override
    public ToolAccess Tools() {
        return Tools;
    }

    private final ToolTypeCustomizationAccess ToolTypeCustomizations;

    @Override
    public ToolTypeCustomizationAccess ToolTypeCustomizations() {
        return ToolTypeCustomizations;
    }

    private final TypeAccess Types;

    @Override
    public TypeAccess Types() {
        return Types;
    }

    private final BuiltinTypeAccess BuiltinTypes;

    @Override
    public BuiltinTypeAccess BuiltinTypes() {
        return BuiltinTypes;
    }

    private final MapTypeAccess MapTypes;

    @Override
    public MapTypeAccess MapTypes() {
        return MapTypes;
    }

    private final SingleBaseTypeContainerAccess SingleBaseTypeContainers;

    @Override
    public SingleBaseTypeContainerAccess SingleBaseTypeContainers() {
        return SingleBaseTypeContainers;
    }

    private final ConstantLengthArrayTypeAccess ConstantLengthArrayTypes;

    @Override
    public ConstantLengthArrayTypeAccess ConstantLengthArrayTypes() {
        return ConstantLengthArrayTypes;
    }

    private final SimpleTypeAccess SimpleTypes;

    @Override
    public SimpleTypeAccess SimpleTypes() {
        return SimpleTypes;
    }

    private final ConstantIntegerAccess ConstantIntegers;

    @Override
    public ConstantIntegerAccess ConstantIntegers() {
        return ConstantIntegers;
    }

    private final UserdefinedTypeAccess UserdefinedTypes;

    @Override
    public UserdefinedTypeAccess UserdefinedTypes() {
        return UserdefinedTypes;
    }

    private final ClassTypeAccess ClassTypes;

    @Override
    public ClassTypeAccess ClassTypes() {
        return ClassTypes;
    }

    private final EnumTypeAccess EnumTypes;

    @Override
    public EnumTypeAccess EnumTypes() {
        return EnumTypes;
    }

    private final InterfaceTypeAccess InterfaceTypes;

    @Override
    public InterfaceTypeAccess InterfaceTypes() {
        return InterfaceTypes;
    }

    private final TypeDefinitionAccess TypeDefinitions;

    @Override
    public TypeDefinitionAccess TypeDefinitions() {
        return TypeDefinitions;
    }

}
