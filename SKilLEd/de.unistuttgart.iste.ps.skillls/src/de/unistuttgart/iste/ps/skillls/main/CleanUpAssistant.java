package de.unistuttgart.iste.ps.skillls.main;

import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 18.02.16.
 *
 * @author Armin Hüneburg
 */
public class CleanUpAssistant {
    private static CleanUpAssistant instance = null;

    /**
     * Creates or returns an instance. If the skillFile is changed, you have to call {@link CleanUpAssistant#renewInstance()} first
     * @param skillFile the skillfile an instance should use
     * @return returns an instance.
     */
    public static CleanUpAssistant getInstance(SkillFile skillFile) {
        if (instance == null) {
            instance = new CleanUpAssistant(skillFile);
        }
        return instance;
    }

    /**
     * deletes the current instance
     */
    public static void renewInstance() {
        instance = null;
    }

    private final HashMap<Type, ArrayList<Type>> typeMap = new HashMap<>();
    private SkillFile skillFile;
    private final List<Type> typesToDelete;
    private boolean broken = false;

    /**
     * Constructor. sets the skillfile
     * @param skillFile skillfile the assistant should use.
     */
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

    /**
     * Sets a type as found
     * @param oldType the old equivalent type
     * @param newType the new type
     */
    public void foundType(Type oldType, Type newType) {
        typesToDelete.remove(oldType);
        if (oldType == newType || oldType == null) {
            return;
        }
        renewType(oldType, newType);
    }

    /**
     * replaces the oldtype with the new type
     * @param oldType the type that should be deleted
     * @param newType the type that should replace the old one
     */
    private void renewType(Type oldType, Type newType) {
        if (newType == null) {
            return;
        }
        for (Tool tool : skillFile.Tools()) {
            for (int i = 0; i < tool.getTypes().size(); i++) {
                Type type = tool.getTypes().get(i);
                if (type.getOrig() != oldType) {
                    continue;
                }
                ArrayList<String> restrictions = new ArrayList<>();
                ArrayList<Hint> hints = new ArrayList<>();
                ArrayList<Field> fields = new ArrayList<>();
                ArrayList<String> extensions = new ArrayList<>();
                Type toolType = skillFile.Types().make(newType.getComment(), extensions, fields, newType.getFile(),
                        newType.getName(), newType, restrictions, hints);

                restrictions.addAll(type.getRestrictions().stream().filter(res -> newType.getRestrictions().contains(res))
                        .collect(Collectors.toList()));

                for (Hint hint : type.getHints()) {
                    for (Hint newHint : newType.getHints()) {
                        if (newHint.getName().toLowerCase().equals(hint.getName().toLowerCase())) {
                            toolType.getHints().add(skillFile.Hints().make(newHint.getName(), toolType));
                            break;
                        }
                    }
                }

                for (Field field : type.getFields()) {
                    for (Field newField : newType.getFields()) {
                        if (newField.getName().toLowerCase().equals(field.getName().toLowerCase())) {
                            Field toolField = copyField(field, newField, toolType);
                            toolType.getFields().add(toolField);
                            break;
                        }
                    }
                }

                for (String ex : type.getExtends()) {
                    for (String newEx : newType.getExtends()) {
                        if (ex.toLowerCase().equals(newEx.toLowerCase())) {
                            toolType.getExtends().add(ex);
                            break;
                        }
                    }
                }

                tool.getTypes().remove(type);
                deleteType(type);
                tool.getTypes().add(toolType);
            }
        }
        deleteType(oldType);
    }

    private void deleteType(Type type) {
        for (Field field : type.getFields()) {
            for (Hint hint : field.getHints()) {
                skillFile.delete(hint);
            }
            skillFile.delete(field);
        }
        for (Hint hint : type.getHints()) {
            skillFile.delete(hint);
        }
        skillFile.delete(type);
    }

    private Field copyField(Field toolField, Field newField, Type parent) {
        ArrayList<String> restrictions = new ArrayList<>();
        ArrayList<Hint> hints = new ArrayList<>();

        Field field = skillFile.Fields().make(newField.getComment(), newField.getName(), newField, restrictions, parent, hints);

        for (String res : toolField.getRestrictions()) {
            for (String newRes : newField.getRestrictions()) {
                if (res.toLowerCase().equals(newRes.toLowerCase())) {
                    field.getRestrictions().add(res);
                    break;
                }
            }
        }

        for (Hint hint : toolField.getHints()) {
            for (Hint newHint : newField.getHints()) {
                if (hint.getName().toLowerCase().equals(newHint.getName().toLowerCase())) {
                    Hint fieldHint = skillFile.Hints().make(newHint.getName(), field);
                    field.getHints().add(fieldHint);
                    break;
                }
            }
        }

        return field;
    }

    /**
     *
     * @param name the name of the type that should be found
     * @return returns null or the type.
     */
    public Type findType(String name) {
        for (Type type : typeMap.keySet()) {
            boolean nameMatch = type.getName().matches("^([^ ]+ )?" + name + "( .*)?$");
            if (type.getOrig() == null && nameMatch) {
                return type;
            }
        }
        return null;
    }

    /**
     * deletes all unneeded types in the skillfile.
     */
    public void cleanUp(Indexing indexing) {
        if (indexing == Indexing.NO_INDEXING) {
            return;
        }
        for (Type type : typesToDelete) {
            int counter = 0;
            for (Type fileType : skillFile.Types()) {
                counter++;
                if (fileType == type || fileType.getOrig() == type) {
                    deleteType(fileType);
                    for (Tool tool : skillFile.Tools()) {
                        if (tool.getTypes().contains(fileType)) {
                            tool.getTypes().remove(fileType);
                            break;
                        }
                    }
                }
            }
            broken |= counter >= 2;
            deleteType(type);
        }
    }

    /**
     * Tests if at least one tool is broken and throws a BreakageException if it is.
     */
    public void breakageAnalysis() {
        if (broken) {
            BreakageException e = new BreakageException();
            HashSet<Tool> tools = new HashSet<>();
            for (Type origType : typesToDelete) {
                for (Tool tool : skillFile.Tools()) {
                    if (tool.getFiles().stream().anyMatch(f -> f.getPath().equals(origType.getFile().getPath()))) {
                        tools.add(tool);
                    }
                }
            }
            tools.forEach(e::addTool);
            throw e;
        }
    }
}
