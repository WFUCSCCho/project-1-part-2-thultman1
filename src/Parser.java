/****************************************************
 * @file: Parser.java
 * @description: Loads all movies from movie_data.csv into a BST<Movie>, then processes input commands (search/remove/print) and writes results to result.txt. Quote-aware CSV parsing handles commas inside quoted fields (like description).
 * @author: Tim Hultman
 * @date: 9/23/25
 ****************************************************/
import java.io.*;
import java.util.Scanner;

public class Parser {
    //BST movie construction
    private BST<Movie> BST = new BST<>();

    /**
     * Constructor, clears result.txt, loads dataset, processes commands
     * Parameter String filename, name of input
     */
    public Parser(String filename) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("./result.txt");
        writer.print("");
        writer.close();

        loadCSV("./movie_data.csv");
        process(new File(filename));
    }

    /**
     * Load all movies from CSV into BST, skipping header row
     * Parameter String csvPath, name of csv
     * Return void
     */
    public void loadCSV(String CSV) {
        try (Scanner scan = new Scanner(new File(CSV))) {
            scan.nextLine(); // pass over header as it is just variables

            while (scan.hasNextLine()) {
                String[] parts = splitCSV(scan.nextLine(), 8);
                BST.insert(toMovie(parts));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Bad dataset: " + CSV);
        }
    }


    /**
     * Process input file commands
     * Parameter String input, name of input file
     * Return void
     */
    public void process(File inputFile) throws FileNotFoundException {
        try (Scanner scan = new Scanner(inputFile)) {
            while (scan.hasNextLine()) {
                String[] cmd = scan.nextLine().split(" +", 2);
                operate_BST(cmd);
            }
        }
    }


    /**
     * Operates BST via cases
     * Parameter String[] command, 1D array of commands
     * Return void
     */
    public void operate_BST(String[] command) {
        String cmd = command[0].toLowerCase();
        switch (cmd) {
            case "search":
                Movie target = new Movie(command[1], 0, "", "", 0, "", "", "");
                Node<Movie> found = BST.search(target);
                if (found != null) {
                    writeToFile("found " + found.getValue().toString(), "./result.txt");
                }
                else {
                    writeToFile("search error", "./result.txt");
                }
                break;

            case "remove":
                Movie film = new Movie(command[1], 0, "", "", 0, "", "", "");
                Node<Movie> removed = BST.remove(film);
                if (removed != null) {
                    writeToFile("removed " + command[1], "./result.txt");
                }
                else {
                    writeToFile("remove error", "./result.txt");
                }
                break;

            case "print":
                StringBuilder strB = new StringBuilder();
                for (Movie m : BST) {
                    strB.append(m.toString()).append("\n");
                }
                writeToFile(strB.toString().trim(), "./result.txt");
                break;
        }
    }


    /**
     * Convert array of fields into a Movie
     * Parameter String[] f, 1D array of film
     * Return Movie, film with filled values
     */
    private Movie toMovie(String[] f) {
        String name = unquote(f[0]);
        int year = Integer.parseInt(unquote(f[1]));
        String duration = unquote(f[2]);
        String genre = unquote(f[3]);
        double rating = Double.parseDouble(unquote(f[4]));
        String description = unquote(f[5]);
        String director = unquote(f[6]);
        String stars = unquote(f[7]);
        return new Movie(name, year, duration, genre, rating, description, director, stars);
    }

    /**
     * Splits CSV with every 8 commas being a new movie, quote aware
     * Parameter String ln an int expected, the line scan and expected int value
     * Return String[], array
     */
    private String[] splitCSV(String ln, int expected) {
        String[] out = new String[expected];
        StringBuilder strB = new StringBuilder();
        boolean inQuotes = false;
        int index = 0;

        for (int i = 0; i < ln.length(); i++) {
            char c = ln.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < ln.length() && ln.charAt(i + 1) == '"') {
                        strB.append('"');
                        i++;
                    }
                    else {
                        inQuotes = false;
                    }
                }
                else {
                    strB.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                }
                else if (c == ',') {
                    out[index++] = strB.toString();
                    strB.setLength(0);
                }
                else {
                    strB.append(c);
                }
            }
        }
        out[index] = strB.toString();
        return out;
    }

    /**
     * Unqoutes a string
     * Parameter String str, input string w/ ""
     * Return String, out string w/o ""
     */
    private String unquote(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }


    /**
     * Writes one line to result.txt until done
     * Parameter String content and filePath, content to write and the result.txt
     * Return void
     */
    public void writeToFile(String content, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(content + "\n");
        }
        catch (IOException e) {
            System.out.println("Error writing to file: " + filePath);
        }
    }
}
