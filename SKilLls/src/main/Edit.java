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
    private tools.internal.ToolAccess ta;
    private tools.internal.FileAccess fa;
    private tools.api.SkillFile sk;

    public Edit() {

    }

    public void setProject(File project) {
        File project1 = project;
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
        String toolName;
        toolName = getTool();
        tools.Tool t;
        if (toolName.startsWith("&") && Character.toLowerCase(toolName.charAt(1)) == 'n') {
            toolName = newTool();
            t = ta.make();
            t.setFiles(new ArrayList<>());
            t.setTypes(new ArrayList<>());
            t.setName(toolName);
        }
        switch (chooseAction()) {
            case 1:

                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;
        }
    }

    private int chooseAction() {
        while (true) {
            System.out.println("1 - Add Type    2 - Remove Type\n3 - Edit Type    4 - Delete Tool");
            String line = scanner.nextLine();
            try {
                int n = Integer.parseInt(line);
                if (1 <= n && n <= 4) {
                    return n;
                } else {
                    System.out.println("Invalid action");
                }
            } catch (NumberFormatException e) {
                if (exit(line)) {
                    System.exit(0);
                }
            }
        }
    }

    private String newTool() {
        System.out.println("Please insert a name for the new tool.");
        String line = scanner.nextLine();
        while (line.startsWith("&")) {
            if (exit(line)) {
                System.exit(0);
            }
            System.out.println("Please don't use & as the first character in a tool.");
            line = scanner.nextLine();
        }
        return line;
    }

    private String getTool() {
        System.out.println("Please insert a tool for editing or insert &N for adding a new one.");
        String line = scanner.nextLine();
        while (line.startsWith("&")) {
            if (exit(line)) {
                System.exit(0);
            }
            if (line.length() > 1 && Character.toLowerCase(line.charAt(1)) == 'n') {
                return line;
            }
            System.out.println("Please don't use & as the first character in a tool.");
            line = scanner.nextLine();
        }
        return line;
    }

    private boolean exit(String line) {
        if (line.startsWith("&")) {
            switch (line.charAt(1)) {
                case 'Q':
                case 'q':
                    return true;

                case 'X':
                case 'x':
                    save();
                    return true;
            }
        }
        return false;
    }

    private void save() {
        sk.close();
    }
}
