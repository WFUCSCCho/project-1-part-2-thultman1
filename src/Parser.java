/****************************************************
 * @file: Parser.java
 * @description: Loads all movies from movie_data.csv into a BST<Movie>,
 *               then processes input commands (search/remove/print) and
 *               writes results to result.txt. Quote-aware CSV parsing
 *               handles commas inside quoted fields (like description).
 * @author: Tim Hultman
 * @date: 9/22/25
 ****************************************************/
import java.io.*;
import java.util.Scanner;

public class Parser {
    private BST<Movie> mybst = new BST<>();

    /** Constructor: clears result.txt, loads dataset, processes commands */
    public Parser(String filename) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("./result.txt");
        writer.print("");
        writer.close();

        loadCSV("./movie_data.csv");  // preload dataset into BST
        process(new File(filename));  // run commands
    }

    /** Load all movies from CSV into BST */
    private void loadCSV(String csvPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (first) { // skip header
                    first = false;
                    if (line.toLowerCase().startsWith("name,")) continue;
                }

                String[] parts = splitCSV(line, 8);
                if (parts == null || parts.length != 8) continue;

                Movie m = toMovie(parts);
                mybst.insert(m);
            }
        } catch (IOException e) {
            System.out.println("Error reading dataset: " + csvPath);
        }
    }

    /** Process commands from input file */
    public void process(File input) throws FileNotFoundException {
        Scanner scan = new Scanner(input);
        while (scan.hasNextLine()) {
            String line = scan.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] cmd = line.split(" +", 2);
            operate_BST(cmd);
        }
        scan.close();
    }

    /** Execute BST operations */
    public void operate_BST(String[] command) {
        if (command.length == 0) {
            writeToFile("Invalid Command", "./result.txt");
            return;
        }

        switch (command[0].toLowerCase()) {
            case "search" -> {
                if (command.length < 2) {
                    writeToFile("Invalid Command", "./result.txt");
                } else {
                    Movie target = new Movie(command[1].trim(), 0, "", "", 0, "", "", "");
                    Node<Movie> found = mybst.search(target);
                    if (found != null) {
                        writeToFile("found " + found.getValue().toString(), "./result.txt");
                    } else {
                        writeToFile("search failed", "./result.txt");
                    }
                }
            }
            case "remove" -> {
                if (command.length < 2) {
                    writeToFile("Invalid Command", "./result.txt");
                } else {
                    Movie target = new Movie(command[1].trim(), 0, "", "", 0, "", "", "");
                    Node<Movie> removed = mybst.remove(target);
                    if (removed != null) {
                        writeToFile("removed " + command[1], "./result.txt");
                    } else {
                        writeToFile("remove failed", "./result.txt");
                    }
                }
            }
            case "print" -> {
                StringBuilder build = new StringBuilder();
                for (Movie m : mybst) {
                    build.append(m.toString()).append("\n");
                }
                writeToFile(build.toString().trim(), "./result.txt");
            }
            default -> writeToFile("Invalid Command", "./result.txt");
        }
    }

    /** Convert array of fields into a Movie */
    private Movie toMovie(String[] f) {
        String name = unquote(f[0]);
        int year = safeInt(unquote(f[1]));
        String duration = unquote(f[2]);
        String genre = unquote(f[3]);
        double rating = safeDouble(unquote(f[4]));
        String description = unquote(f[5]);
        String director = unquote(f[6]);
        String stars = unquote(f[7]);
        return new Movie(name, year, duration, genre, rating, description, director, stars);
    }

    /** Quote-aware CSV splitting */
    private String[] splitCSV(String line, int expected) {
        String[] out = new String[expected];
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        int idx = 0;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    if (idx >= expected) return null;
                    out[idx++] = cur.toString();
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
        }
        if (idx < expected) out[idx++] = cur.toString();
        return (idx == expected) ? out : null;
    }

    private String unquote(String s) {
        if (s == null) return "";
        String t = s.trim();
        if (t.length() >= 2 && t.startsWith("\"") && t.endsWith("\"")) {
            t = t.substring(1, t.length() - 1);
        }
        return t.trim();
    }

    private int safeInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private double safeDouble(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0.0; }
    }

    /** Append one line to result.txt */
    public void writeToFile(String content, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(content + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filePath);
        }
    }
}
