package karob.bigtrees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class KGetOptions {
    public static File optionsFile;
    public static String optionsFilename;
    public static String section;
    public static boolean sectionFlag = false;

    public KGetOptions() {
        sectionFlag = false;
    }

    public static void setFile(String s) {
        optionsFilename = s;
        optionsFile = new File(optionsFilename);
        if (!optionsFile.exists()) {
            System.out.println("BigTrees options file '" + optionsFilename + "' does not exist.");
            System.out.println("Creating '" + optionsFilename + "'.");
            if (optionsFile.getParentFile() != null) {
                optionsFile.getParentFile().mkdirs();
            }
            KGetOptions.autogenOptions(s);
        } else {
            System.out.println("Loading BigTrees options file '" + optionsFilename + "'.");
        }
    }

    public static void setSection(String s) {
        section = s;
        sectionFlag = true;
    }

    public static void clearSection(String s) {
        sectionFlag = false;
    }

    public static int getInteger(String trial, int safety) {
        boolean startSection = !sectionFlag;
        try {
            if (optionsFile == null || !optionsFile.exists()) {
                return safety;
            }
            BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
            String s;
            while ((s = reader.readLine()) != null) {
                try {
                    String[] aa = s.split(" ");
                    if (aa.length > 1 && aa[0].equals("SECTION")) {
                        startSection = aa[1].equals(section);
                    }
                    if (!startSection) continue;
                    String[] as = s.replace(" ", "").split("=");
                    if (as[0].equals(trial)) {
                        safety = Integer.parseInt(as[1].replace("%", ""));
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
            reader.close();
            return safety;
        } catch (Exception e) {
            return safety;
        }
    }

    public static int[] getIntegerArray(String trial, int[] safety) {
        boolean startSection = !sectionFlag;
        try {
            if (optionsFile == null || !optionsFile.exists()) {
                return safety;
            }
            BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
            String s;
            while ((s = reader.readLine()) != null) {
                try {
                    String[] aa = s.split(" ");
                    if (aa.length > 1 && aa[0].equals("SECTION")) {
                        startSection = aa[1].equals(section);
                    }
                    if (!startSection) continue;
                    String[] as = s.replace(" ", "").split("=");
                    if (as[0].equals(trial)) {
                        String[] bs = as[1].split(",");
                        safety = new int[bs.length];
                        for (int i = 0; i < bs.length; ++i) {
                            safety[i] = Integer.parseInt(bs[i].replace("%", "").trim());
                        }
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
            reader.close();
            return safety;
        } catch (Exception e) {
            return safety;
        }
    }

    public static float getFloat(String trial, float safety) {
        boolean startSection = !sectionFlag;
        try {
            if (optionsFile == null || !optionsFile.exists()) {
                return safety;
            }
            BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
            String s;
            while ((s = reader.readLine()) != null) {
                try {
                    String[] aa = s.split(" ");
                    if (aa.length > 1 && aa[0].equals("SECTION")) {
                        startSection = aa[1].equals(section);
                    }
                    if (!startSection) continue;
                    String[] as = s.replace(" ", "").split("=");
                    if (as[0].equals(trial)) {
                        safety = Float.parseFloat(as[1].replace("%", ""));
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
            reader.close();
            return safety;
        } catch (Exception e) {
            return safety;
        }
    }

    public static boolean getBoolean(String trial, boolean safety) {
        boolean startSection = !sectionFlag;
        try {
            if (optionsFile == null || !optionsFile.exists()) {
                return safety;
            }
            BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
            String s;
            while ((s = reader.readLine()) != null) {
                try {
                    String[] aa = s.split(" ");
                    if (aa.length > 1 && aa[0].equals("SECTION")) {
                        startSection = aa[1].equals(section);
                    }
                    if (!startSection) continue;
                    String[] as = s.replace(" ", "").split("=");
                    if (as[0].equals(trial)) {
                        safety = as[1].equalsIgnoreCase("true");
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
            reader.close();
            return safety;
        } catch (Exception e) {
            return safety;
        }
    }

    public static String getWord(String trial, String safety) {
        boolean startSection = !sectionFlag;
        try {
            if (optionsFile == null || !optionsFile.exists()) {
                return safety;
            }
            BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
            String s;
            while ((s = reader.readLine()) != null) {
                try {
                    String[] aa = s.split(" ");
                    if (aa.length > 1 && aa[0].equals("SECTION")) {
                        startSection = aa[1].equals(section);
                    }
                    if (!startSection) continue;
                    String[] as = s.split("=");
                    if (as[0].replace(" ", "").equals(trial)) {
                        safety = as[1].trim();
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
            reader.close();
            return safety;
        } catch (Exception e) {
            return safety;
        }
    }

    public static String getString(String trial, String safety) {
        boolean startSection = !sectionFlag;
        try {
            if (optionsFile == null || !optionsFile.exists()) {
                return safety;
            }
            BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
            String s;
            while ((s = reader.readLine()) != null) {
                try {
                    String[] aa = s.split(" ");
                    if (aa.length > 1 && aa[0].equals("SECTION")) {
                        startSection = aa[1].equals(section);
                    }
                    if (!startSection) continue;
                    String[] as = s.split("=");
                    if (as[0].replace(" ", "").equals(trial)) {
                        String temp = as[1].trim();
                        if (temp.startsWith("\"") && temp.endsWith("\"")) {
                            safety = temp.substring(1, temp.length() - 1);
                        } else {
                            safety = temp;
                        }
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
            reader.close();
            return safety;
        } catch (Exception e) {
            return safety;
        }
    }

    public static void autogenOptions(String s) {
        try {
            PrintWriter var1 = new PrintWriter(new FileWriter(optionsFile));
            var1.println("----------------------------------------------------");
            var1.println("Big Trees Mod Configuration File");
            var1.println("----------------------------------------------------");
            var1.println("");
            var1.println("SECTION GENERAL");
            var1.println("----------------------------------------------------");
            var1.println("  natural-generation = true");
            var1.println("  natural-generation-exceptions = none");
            var1.println("  manual-generation = true");
            var1.println("  manual-generation-exceptions = none");
            var1.println("  classic-tree-heights = false");
            var1.println("  classic-jungle = false");
            var1.println("  oak-height-minimum = 4");
            var1.println("  oak-height-maximum = 7");
            var1.println("  birch-height-minimum = 8");
            var1.println("  birch-height-maximum = 14");
            var1.println("  pine-1-height-minimum = 11");
            var1.println("  pine-1-height-maximum = 16");
            var1.println("  pine-2-height-minimum = 10");
            var1.println("  pine-2-height-maximum = 14");
            var1.println("  swamp-height-minimum = 5");
            var1.println("  swamp-height-maximum = 9");
            var1.println("  jungle-1-height-minimum = 10");
            var1.println("  jungle-1-height-maximum = 35");
            var1.println("  jungle-2-height-minimum = 4");
            var1.println("  jungle-2-height-maximum = 11");
            var1.println("  old-tree-enable = true");
            var1.println("  old-tree-percentage = 25%");
            var1.println("  tall-tree-enable = true");
            var1.println("  tall-tree-percentage = 25%");
            var1.println("  big-birch-enable = true");
            var1.println("  big-birch-percentage = 0.5%");
            var1.println("  old-tree-jungle-percentage = 25%");
            var1.println("  old-swamp-tree-enable = true");
            var1.println("  old-swamp-tree-percentage = 20%");
            var1.println("  hat-tree-enable = true");
            var1.println("  hat-tree-percentage = 20%");
            var1.println("  cyprus-tree-enable = true");
            var1.println("  cyprus-tree-percentage = 20%");
            var1.println("  dead-tree-enable = true");
            var1.println("  dead-tree-percentage = 5%");
            var1.println("  variable-pine-density = true");
            var1.println("  thick-pine-enable = true");
            var1.println("  thick-pine-percentage = 20%");
            var1.println("  mixed-pine-percentage = 20%");
            var1.println("  no-pine-percentage = 20%");
            var1.println("  roots-enable = true");
            var1.println("");
            var1.println("SECTION OLDTREE");
            var1.println("----------------------------------------------------");
            var1.println("  wood-type-1 = 17");
            var1.println("  wood-meta-1 = 0");
            var1.println("  height-minimum-1 = 3");
            var1.println("  height-maximum-1 = 7");
            var1.println("  wood-type-2 = 17");
            var1.println("  wood-meta-2 = 0");
            var1.println("  leaf-type-2 = 18");
            var1.println("  leaf-meta-2 = 0");
            var1.println("  height-minimum-2 = 12");
            var1.println("  height-maximum-2 = 18");
            var1.println("  wood-type-3 = 17");
            var1.println("  wood-meta-3 = 1");
            var1.println("  leaf-type-3 = 18");
            var1.println("  leaf-meta-3 = 1");
            var1.println("  height-minimum-3 = 18");
            var1.println("  height-maximum-3 = 22");
            var1.println("  wood-type-4 = 17");
            var1.println("  wood-meta-4 = 0");
            var1.println("  leaf-type-4 = 18");
            var1.println("  leaf-meta-4 = 0");
            var1.println("  height-minimum-4 = 28");
            var1.println("  height-maximum-4 = 32");
            var1.println("  wood-type-5 = 17");
            var1.println("  wood-meta-5 = 2");
            var1.println("  leaf-type-5 = 18");
            var1.println("  leaf-meta-5 = 2");
            var1.println("  height-minimum-5 = 19");
            var1.println("  height-maximum-5 = 24");
            var1.println("");
            var1.println("SECTION DEADTREE");
            var1.println("----------------------------------------------------");
            var1.println("  wood-type = 17");
            var1.println("  wood-meta = 0");
            var1.println("  height-minimum = 13");
            var1.println("  height-maximum = 17");
            var1.println("");
            var1.println("SECTION SWAMPTREE");
            var1.println("----------------------------------------------------");
            var1.println("  wood-type = 17");
            var1.println("  wood-meta = 0");
            var1.println("  leaf-type = 18");
            var1.println("  leaf-meta = 0");
            var1.println("  height-minimum = 28");
            var1.println("  height-maximum = 32");
            var1.println("");
            var1.println("SECTION CYPRUSTREE");
            var1.println("----------------------------------------------------");
            var1.println("  wood-type = 17");
            var1.println("  wood-meta = 1");
            var1.println("  leaf-type = 18");
            var1.println("  leaf-meta = 1");
            var1.println("  height-minimum = 28");
            var1.println("  height-maximum = 32");
            var1.println("");
            var1.println("SECTION HATTREE");
            var1.println("----------------------------------------------------");
            var1.println("  wood-type = 17");
            var1.println("  wood-meta = 0");
            var1.println("  leaf-type = 18");
            var1.println("  leaf-meta = 0");
            var1.println("  height-minimum = 28");
            var1.println("  height-maximum = 32");
            var1.println("");
            var1.println("SECTION TALLTREE");
            var1.println("----------------------------------------------------");
            var1.println("  wood-type = 17");
            var1.println("  wood-meta = 0");
            var1.println("  leaf-type = 18");
            var1.println("  leaf-meta = 0");
            var1.println("  height-minimum = 13");
            var1.println("  height-maximum = 28");
            var1.println("  trunk-minimum = 23%");
            var1.println("  trunk-maximum = 32%");
            var1.println("  stunt-minimum = 7");
            var1.println("  base-type-1 = 3");
            var1.println("  base-type-2 = 2");
            var1.println("  branch-length = 60%");
            var1.println("  branch-rotation = 0.618034");
            var1.println("  tap-root-length = 50%");
            var1.println("  branch-spacing = 60%");
            var1.println("  branch-pitch = 0.0");
            var1.println("  branch-curl = 0.08");
            var1.println("  leaf-radius = 3");
            var1.println("  sub-branch-density = 12%");
            var1.println("  sub-branch-section = 60%");
            var1.println("  size-for-sub = 4.0");
            var1.println("  sub-branch-angle = 30");
            var1.println("  sub-branch-size = 75%");
            var1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
