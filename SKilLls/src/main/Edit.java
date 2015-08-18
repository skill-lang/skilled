package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Armin Hüneburg
 * @since 25.08.15.
 */
class Edit {
    private final Scanner scanner = new Scanner(System.in);
    private File project;
    private tools.internal.ToolAccess ta;
    private tools.internal.FileAccess fa;
    private tools.api.SkillFile sk;

    public Edit() {

    }

    public void setProject(File project) {
        this.project = project;
    }

    public void setToolAccess(tools.internal.ToolAccess ta) {
        this.ta = ta;
    }

    public void setFileAccess(tools.internal.FileAccess fa) {
        this.fa = fa;
    }

    public void setSkillFile(tools.api.SkillFile sk) {
        this.sk = sk;
    }

    public void start() {
        System.out.println("Welcome to the Tool editor.\nWhat is the name of the Tool you want to add?");
        boolean found = false;
        String toolname = null;
        while (!found) {
            toolname = scanner.nextLine();
            if (toolname.startsWith("&")) {
                System.out.println("Please don't use & as first character.");
            } else {
                found = true;
            }
        }
        tools.Tool t = ta.make();
        t.setName(toolname);
        System.out.println("Please insert a file name to add this file. &X to exit.");
        String line;
        t.setFiles(new ArrayList<tools.File>());
        t.setTypes(new ArrayList<tools.Type>());
        while (!(line = scanner.nextLine()).equals("&X")) {
            if (line.isEmpty() || line.startsWith("&")) {
                System.out.println("Please enter a file name that does not start with &");
            } else {
                t.getFiles().add(fa.make("", new File(line).getAbsolutePath(), ""));
            }
        }
        sk.close();
    }
}
