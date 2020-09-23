package shyunku.project.moneytransaction;

import android.content.Context;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class FileManager {
    private final String fileName = "transactions.json";
    public static Context fileManageContext;

    public void saveFile(TransactionEngine engine){
        Log.e("txx", "saving");

        File file = new File(fileManageContext.getFilesDir(), fileName);
        Gson gson = new GsonBuilder().setLenient().create();

        try(FileWriter writer = new FileWriter(file.getAbsolutePath())){
            gson.toJson(engine, writer);
        } catch (IOException e) {
            Log.e("txx", e.getMessage());
        }
    }

    public TransactionEngine loadFile(){
        JsonParser parser = new JsonParser();
        Log.e("txx", "loading");

        File file = new File(fileManageContext.getFilesDir(), fileName);
        TransactionEngine engine = null;
        Gson gson = new GsonBuilder().setLenient().create();

        try {
            JsonObject jsonObject = (JsonObject) parser.parse(new FileReader(file.getAbsolutePath()));
            engine = gson.fromJson(new FileReader(file.getAbsolutePath()), TransactionEngine.class);
            Log.e("txx", jsonObject.toString());
        } catch (FileNotFoundException e) {
            // file not found
        }
        return engine;
    }
}
