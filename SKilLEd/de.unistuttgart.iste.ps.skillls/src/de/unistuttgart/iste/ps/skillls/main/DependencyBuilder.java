package de.unistuttgart.iste.ps.skillls.main;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;


/**
 * Created on 25.01.16.
 *
 * Class for adding the dependencies of a type to a tool
 *
 * @author Armin Hüneburg
 */
public class DependencyBuilder implements Runnable {
    private final Tool tool;
    private final SkillFile skillFile;

    /**
     * Constructor, setting object fields.
     * 
     * @param tool
     *            tool containing types, that may have dependencies that are not met
     * @param skillFile
     *            skillfile containing the tool and types.
     */
    public DependencyBuilder(Tool tool, SkillFile skillFile) {
        this.tool = tool;
        this.skillFile = skillFile;
    }

    /**
     * Method for searching and adding missing dependencies.
     */
    @Override
    public void run() {
        for (de.unistuttgart.iste.ps.skillls.tools.File file : tool.getFiles()) {
            ArrayList<Type> types = tool.getTypes().stream().filter(type -> type != null && type.getFile().getPath().equals(file.getPath()))
                    .collect(Collectors.toCollection(ArrayList::new));
            for (Type type : types) {
                ArrayList<String> deps = new ArrayList<>();
                for (String extension : type.getExtends()) {
                    tool.getTypes().stream().filter(t -> t.getName().equals(extension))
                            .filter(t -> !t.getFile().getPath().equals(type.getFile().getPath())
                                    && !deps.contains(t.getFile().getPath()))
                            .forEach(t -> deps.add(t.getFile().getPath()));
                }
                for (Field field : type.getFields()) {
                    for (Type t : skillFile.Types()) {
                        String name = field.getName();
                        if (name.startsWith("auto")) {
                            name = name.substring(5);
                        }
                        int index = name.indexOf(' ');
                        if (index == -1) {
                            index = name.length();
                        }
                        if (t.getName().startsWith(name.substring(0, index))) {
                            if (!t.getFile().getPath().equals(file.getPath()) && !deps.contains(t.getFile().getPath())) {
                                deps.add(t.getFile().getPath());
                            }
                        }
                    }
                }
                deps.stream().filter(dep -> !file.getDependencies().contains(dep))
                        .forEach(dep -> file.getDependencies().add(dep));
            }
        }
    }
}
