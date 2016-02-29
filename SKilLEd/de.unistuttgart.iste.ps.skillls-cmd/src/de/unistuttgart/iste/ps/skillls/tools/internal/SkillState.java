/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 29.02.2016                               *
 * |___/_|\_\_|_|____|    by: mpl                                             *
\*                                                                            */
package de.unistuttgart.iste.ps.skillls.tools.internal;

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

import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;

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
        assert f.exists() : "can only open files that already exist in genarel, because of java.nio restrictions";
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
            FileAccess File = new FileAccess(0);
            types.add(File);
            GeneratorAccess Generator = new GeneratorAccess(1);
            types.add(Generator);
            HintAccess Hint = new HintAccess(2);
            types.add(Hint);
            HintParentAccess HintParent = new HintParentAccess(3);
            types.add(HintParent);
            FieldAccess Field = new FieldAccess(4, HintParent);
            types.add(Field);
            TypeAccess Type = new TypeAccess(5, HintParent);
            types.add(Type);
            ToolAccess Tool = new ToolAccess(6);
            types.add(Tool);
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

        Files = (FileAccess) poolByName.get("file");

        Generators = (GeneratorAccess) poolByName.get("generator");

        Hints = (HintAccess) poolByName.get("hint");

        HintParents = (HintParentAccess) poolByName.get("hintparent");

        Fields = (FieldAccess) poolByName.get("field");

        Types = (TypeAccess) poolByName.get("type");

        Tools = (ToolAccess) poolByName.get("tool");


        finalizePools();
    }

    public SkillState(HashMap<String, StoragePool<?, ?>> poolByName, StringPool strings, StringType stringType,
            Annotation annotationType,
            ArrayList<StoragePool<?, ?>> types, Path path, Mode mode) {
        super(strings, path, mode, types, stringType, annotationType);
        this.poolByName = poolByName;

        Files = (FileAccess) poolByName.get("file");
        Generators = (GeneratorAccess) poolByName.get("generator");
        Hints = (HintAccess) poolByName.get("hint");
        HintParents = (HintParentAccess) poolByName.get("hintparent");
        Fields = (FieldAccess) poolByName.get("field");
        Types = (TypeAccess) poolByName.get("type");
        Tools = (ToolAccess) poolByName.get("tool");

        finalizePools();
    }

    private final FileAccess Files;

    @Override
    public FileAccess Files() {
        return Files;
    }

    private final GeneratorAccess Generators;

    @Override
    public GeneratorAccess Generators() {
        return Generators;
    }

    private final HintAccess Hints;

    @Override
    public HintAccess Hints() {
        return Hints;
    }

    private final HintParentAccess HintParents;

    @Override
    public HintParentAccess HintParents() {
        return HintParents;
    }

    private final FieldAccess Fields;

    @Override
    public FieldAccess Fields() {
        return Fields;
    }

    private final TypeAccess Types;

    @Override
    public TypeAccess Types() {
        return Types;
    }

    private final ToolAccess Tools;

    @Override
    public ToolAccess Tools() {
        return Tools;
    }

}
