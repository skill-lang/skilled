/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 29.02.2016                               *
 * |___/_|\_\_|_|____|    by: mpl                                             *
\*                                                                            */
package de.unistuttgart.iste.ps.skillls.tools.internal;

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
        case "file":
            p = (StoragePool<T, B>) new FileAccess(types.size());
            break;


        case "generator":
            p = (StoragePool<T, B>) new GeneratorAccess(types.size());
            break;


        case "hint":
            p = (StoragePool<T, B>) new HintAccess(types.size());
            break;


        case "hintparent":
            p = (StoragePool<T, B>) new HintParentAccess(types.size());
            break;


        case "field": {
            HintParentAccess parent = (HintParentAccess)(poolByName.get("hintparent"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type Field");
            p = (StoragePool<T, B>) new FieldAccess(types.size(), parent);
            break;
        }


        case "type": {
            HintParentAccess parent = (HintParentAccess)(poolByName.get("hintparent"));
            if (null == parent)
                throw new ParseException(in, blockCounter, null, "file lacks expected super type Type");
            p = (StoragePool<T, B>) new TypeAccess(types.size(), parent);
            break;
        }


        case "tool":
            p = (StoragePool<T, B>) new ToolAccess(types.size());
            break;

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
        FileAccess File;
        if (poolByName.containsKey("file"))
            File = (FileAccess) poolByName.get("file");
        else {
            File = new FileAccess(types.size());
            types.add(File);
            poolByName.put("file", File);
        }

        GeneratorAccess Generator;
        if (poolByName.containsKey("generator"))
            Generator = (GeneratorAccess) poolByName.get("generator");
        else {
            Generator = new GeneratorAccess(types.size());
            types.add(Generator);
            poolByName.put("generator", Generator);
        }

        HintAccess Hint;
        if (poolByName.containsKey("hint"))
            Hint = (HintAccess) poolByName.get("hint");
        else {
            Hint = new HintAccess(types.size());
            types.add(Hint);
            poolByName.put("hint", Hint);
        }

        HintParentAccess HintParent;
        if (poolByName.containsKey("hintparent"))
            HintParent = (HintParentAccess) poolByName.get("hintparent");
        else {
            HintParent = new HintParentAccess(types.size());
            types.add(HintParent);
            poolByName.put("hintparent", HintParent);
        }

        FieldAccess Field;
        if (poolByName.containsKey("field"))
            Field = (FieldAccess) poolByName.get("field");
        else {
            Field = new FieldAccess(types.size(), HintParent);
            types.add(Field);
            poolByName.put("field", Field);
        }

        TypeAccess Type;
        if (poolByName.containsKey("type"))
            Type = (TypeAccess) poolByName.get("type");
        else {
            Type = new TypeAccess(types.size(), HintParent);
            types.add(Type);
            poolByName.put("type", Type);
        }

        ToolAccess Tool;
        if (poolByName.containsKey("tool"))
            Tool = (ToolAccess) poolByName.get("tool");
        else {
            Tool = new ToolAccess(types.size());
            types.add(Tool);
            poolByName.put("tool", Tool);
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
