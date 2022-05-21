package com.example.intellijtestapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WordData {
    public static ArrayList<String> words = new ArrayList<String>();


    public static void makeWords(){
        File myfile = new File("/data/data/com.example.intellijtestapp/words.txt");
        try {
            Scanner scanner = new Scanner(myfile);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                words.add(data);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
