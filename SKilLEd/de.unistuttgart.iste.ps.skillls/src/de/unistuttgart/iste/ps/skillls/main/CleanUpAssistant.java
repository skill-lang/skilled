package de.unistuttgart.iste.ps.skillls.main;

import de.unistuttgart.iste.ps.skillls.tools.*;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import de.ust.skill.common.java.api.SkillException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 18.02.16.
 *
 * @author Armin Hüneburg
 */
public class CleanUpAssistant {
    private static CleanUpAssistant instance = null;
    public static CleanUpAssistant getInstance(SkillFile skillFile) {
        if (instance == null) {
            instance = new CleanUpAssistant(skillFile);
        }
        return instance;
    }

    public static void renewInstance() {
        instance = null;
    }

    private final HashMap<Type, ArrayList<Type>> typeMap = new HashMap<>();
    private SkillFile skillFile;
    private final List<Type> typesToDelete;

    private CleanUpAssistant(SkillFile skillFile) {
        this.skillFile = skillFile;
        for (Type type : skillFile.Types()) {
            if (type.getOrig() == null && !typeMap.containsKey(type)) {
                typeMap.put(type, new ArrayList<>());
            } else if (type.getOrig() != null && typeMap.containsKey(type.getOrig())) {
                typeMap.get(type.getOrig()).add(type);
            } else if (type.getOrig() != null) {
                typeMap.put(type.getOrig(), new ArrayList<>());
                typeMap.get(type.getOrig()).add(type);
            }
        }
        typesToDelete = skillFile.Types().stream().filter(t -> t.getOrig() == null).collect(Collectors.toList());
    }

    public void foundType(Type oldType, Type newType) {
        typesToDelete.remove(oldType);
        if (oldType == newType || oldType == null) {
            return;
        }
        renewType(oldType, newType);
    }

    private void renewType(Type oldType, Type newType) {
        delete(oldType);
        List<Tool> tools = skillFile.Tools().stream().filter(t -> t.getTypes().stream().anyMatch(ty -> ty != null && ty.getOrig() == oldType)).collect(Collectors.toList());
        for (Tool tool : tools) {
            for (int i = 0; i < tool.getTypes().size(); i++) {
                Type type = tool.getTypes().get(i);
                if (type != null && type.getOrig() == oldType) {
                    ArrayList<Hint> hints = getHints(type, newType);
                    ArrayList<Field> fields = getFields(type, newType);

                    Type nt = skillFile.Types().make(newType.getComment(), newType.getExtends(), fields, newType.getFile(),
                            newType.getName(), newType, newType.getRestrictions(), hints);

                    for (Field field : fields) {
                        field.setType(nt);
                    }
                    for (Hint hint : hints) {
                        hint.setParent(nt);
                    }
                    tool.getTypes().add(nt);
                    tool.getTypes().remove(type);
                }
            }
        }
    }

    private ArrayList<Field> getFields(Type oldType, Type newType) {
        ArrayList<Field> fields = new ArrayList<>();
        HashMap<String, Field> fieldNames = new HashMap<>();
        newType.getFields().forEach(f -> fieldNames.put(f.getName().toLowerCase(), f));
        for (Field field : oldType.getFields()) {
            if (fieldNames.containsKey(field.getName().toLowerCase())) {
                Field f = fieldNames.get(field.getName().toLowerCase());
                ArrayList<Hint> hints = getHints(field, f);
                Field nf = skillFile.Fields().make(f.getComment(), f.getName(), f, f.getRestrictions(), null, hints);
                fields.add(nf);
                for (Hint hint : hints) {
                    hint.setParent(nf);
                }
            }
        }
        return fields;
    }

    private ArrayList<Hint> getHints(HintParent parent, HintParent newParent) {
        ArrayList<Hint> hints = new ArrayList<>();
        HashMap<String, Hint> hintNames = new HashMap<>();
        newParent.getHints().forEach(h -> hintNames.put(h.getName().toLowerCase(), h));
        for (Hint hint : parent.getHints()) {
            if (hintNames.containsKey(hint.getName().toLowerCase())) {
                hints.add(skillFile.Hints().make(hint.getName(), null));
            }
        }
        return hints;
    }

    private void delete(Type oldType) {
        skillFile.delete(oldType);
        for (Field field : oldType.getFields()) {
            skillFile.delete(field);
            field.getHints().forEach(skillFile::delete);
        }
        oldType.getHints().forEach(skillFile::delete);
        for (Type type : typeMap.get(oldType)) {
            skillFile.delete(type);
            for (Field field : type.getFields()) {
                skillFile.delete(field);
                field.getHints().forEach(skillFile::delete);
            }
            type.getHints().forEach(skillFile::delete);
        }
    }

    public Type findType(String name) {
        for (Type type : typeMap.keySet()) {
            boolean nameMatch = type.getName().matches("^([^ ]+ )?" + name + "( .*)?$");
            if (type.getOrig() == null && nameMatch) {
                return type;
            }
        }
        return null;
    }

    public void cleanUp() {
        typesToDelete.forEach(this::delete);
        try {
            skillFile.close();
        } catch (SkillException ignored) {
            // opened in read only mode
        }
        for (Tool tool : skillFile.Tools()) {
            for (int i = 0; i < tool.getTypes().size(); i++) {
                if (tool.getTypes().get(i) == null) {
                    tool.getTypes().remove(i);
                    i--;
                }
            }
        }
        skillFile = MainClass.openSkillFile(skillFile.currentPath());
    }
}
