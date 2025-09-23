/****************************************************
 * @file: Proj1.java
 * @description: Main entry point for Project 1. Runs the Parser with a given input file and produces result.txt as output.
 * @author: Tim Hultman
 * @date: 9/14/25
 ****************************************************/

import java.io.FileNotFoundException;
/**
 * Main method for Proj1
 * Parameter String[] args, command line arguments
 */
public class Proj1 {
    public static void main(String[] args) throws FileNotFoundException{
        if(args.length != 1){
            System.err.println("Argument count is invalid: " + args.length);
            System.exit(0);
        }
        new Parser(args[0]);
    }
}