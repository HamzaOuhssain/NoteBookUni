package com.hamza.user.notebook;

import android.content.Context;

import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {
    File inputStream;
    public DBHelper mydb;

    public CSVFile(File inputStream){
        this.inputStream = inputStream;

    }

    public void export() throws IOException {
        /*
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "AnalysisData.csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath );
        FileWriter mFileWriter;
        CSVWriter writer;
// File exist
        if(f.exists() && !f.isDirectory()){
            mFileWriter = new FileWriter(filePath , true);
            writer = new CSVWriter(mFileWriter);
        }
        else {
            writer = new CSVWriter(new FileWriter(filePath));
        }
        ArrayList<String> words =  mydb.getAllWords("word");
        ArrayList<String> traductions =  mydb.getAllWords("traduction");
        String[] data = new String[traductions.size()];
        int i=0;
        for(String b: words){
            data[i] = b+","+traductions.get(i);
            i++;
        }


        writer.writeNext(data);

        writer.close();
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/");
        dir.mkdirs();
        File file = new File(dir, "filename.txt");


        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(data); //your string which you want to store
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        ArrayList<String> words =  mydb.getAllWords("word");
        ArrayList<String> traductions =  mydb.getAllWords("traduction");
        String[] data = new String[traductions.size()];
        int i=0;
        for(String b: words){
            data[i] = b+","+traductions.get(i);
            i++;
        }
        try
        {
            FileWriter fw = new FileWriter("test.txt");
            BufferedWriter out = new BufferedWriter(fw);
            out.write("aString");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void read(Context b) throws FileNotFoundException {
        mydb = new DBHelper(b);
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new FileReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                mydb.insertWord(row[0], row[1]);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }

    }
}