package shyunku.project.moneytransaction;

import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class FileManager {
    public void saveFile(TransactionEngine engine){
        Log.e("try", "saving");

        Gson gson = new Gson();
        File saveFile  = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MoneyTransaction");
        Log.e("r", "GET : "+Environment.getExternalStorageDirectory().getAbsolutePath()+"/MoneyTransaction");
        if(!saveFile.exists())
            saveFile.mkdir();
        try(FileWriter writer = new FileWriter(saveFile+"/savedData.json")){
            gson.toJson(engine, writer);
            Log.e("r", gson.toJson(engine));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TransactionEngine loadFile(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MoneyTransaction";
        Log.e("try", "loading");
        TransactionEngine engine = null;
        Gson gson = new Gson();
        File saveFile = null;
        saveFile = new File(path);

        if(saveFile==null) {
            saveFile.mkdir();
        }
        try {
            JsonReader reader = new JsonReader(new FileReader(path+"/savedData.json"));
            Log.e("a", reader.toString());
            engine = gson.fromJson(new FileReader(path+"/savedData.json"), TransactionEngine.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return engine;
    }
}
