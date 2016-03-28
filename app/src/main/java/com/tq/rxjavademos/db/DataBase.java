package com.tq.rxjavademos.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tq.rxjavademos.App;
import com.tq.rxjavademos.model.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * Created by boobooL on 2016/3/28 0028
 * Created 邮箱 ：boobooMX@163.com
 */
public class DataBase {
    private static String DATA_FILE_NAME="data.db";
    private static DataBase INSTANCE;
    File dataFile=new File(App.getInstance().getFilesDir(),DATA_FILE_NAME);
    Gson gson=new Gson();
    public static DataBase getInstance(){
        if(INSTANCE==null){
            INSTANCE=new DataBase();
        }
         return INSTANCE;
    }

    public List<Item>readItems(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Reader reader=new FileReader(dataFile);
            return gson.fromJson(reader,new TypeToken<List<Item>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void writeItems(List<Item>items){
        String json=gson.toJson(items);


            try {
                if(!dataFile.exists()) {
                    dataFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            Writer writer=new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void delete(){
        dataFile.delete();
    }

}
