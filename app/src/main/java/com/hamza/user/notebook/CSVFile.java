package com.hamza.user.notebook;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

class CSVFile {
    private File file;
    private DBHelper mydb;

     CSVFile(File inputStream){
        this.file = inputStream;

    }

     boolean export(Context cont) throws IOException {
        mydb = new DBHelper(cont);
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return false;
        } else {
            File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file;
            PrintWriter printWriter = null;
            try {
                file = new File(exportDir, "notebookexport.csv");
                printWriter = new PrintWriter(new FileWriter(file));
                printWriter.println("ENGLISH WORD,FRENCH WORD");
                ArrayList<String> words = mydb.getAllWords("word");
                ArrayList<String> traductions = mydb.getAllWords("traduction");
                int i = 0;
                for (String b : words) {
                    String data[] = {b, traductions.get(i)};
                    printWriter.println(data[0]+","+data[1]);
                    i++;
                }

            } catch (Exception exc) {
                debug(exc.toString());
                return false;
            } finally {
                if (printWriter != null){
                    printWriter.close();
                }
            }
            return true;
        }

    }
     void debug(Object obj){
        Log.d("DEBUG", obj.toString());
    }
     void read(Context b) throws FileNotFoundException {
        mydb = new DBHelper(b);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String csvLine;

            while ((csvLine = reader.readLine()) != null) {
                csvLine = csvLine.replace("\"", "");
                String[] row = csvLine.split(",");
                mydb.insertWord(row[0], row[1], 0);
                builder.append(csvLine);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }finally {
            try {
                reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}