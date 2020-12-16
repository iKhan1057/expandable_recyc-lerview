package com.e.expandablerecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecycAdapter.ItemCallback {

    private RecyclerView rvList;
    private RecycAdapter mRecycAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvList = findViewById(R.id.list);
        mRecycAdapter = new RecycAdapter(this, this);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.setAdapter(mRecycAdapter);
        mRecycAdapter.setData(parseJson());
        parseJson();
    }

    private List<DataModel> getDataList(JSONArray jsonArray) {
        List<DataModel> dataModels = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            DataModel dataModel = new DataModel();
            dataModel.setId("p" + String.valueOf(i));
            dataModel.setName(jsonObject.optString("name"));
            dataModel.setLevel(1);
            dataModel.setShowradio(false);
            dataModel.setState(DataModel.STATE.CLOSED);
            dataModel.setModels(getChildDataList(dataModel.getId(), jsonObject));
            dataModels.add(dataModel);
        }
        return dataModels;
    }

    private ArrayList<DataModel> getChildDataList(String id, JSONObject jsonObject) {
        ArrayList<DataModel> dataModels = new ArrayList<>();
        JSONArray jsonArray = jsonObject.optJSONArray("appointment");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 =jsonArray.optJSONObject(i);
            DataModel dataModel = new DataModel();
            dataModel.setId(id + "c" + String.valueOf(i));
            dataModel.setName(jsonObject1.optString("days"));
            dataModel.setLevel(2);
            dataModel.setShowradio(true);
            dataModel.setState(DataModel.STATE.CLOSED);
            dataModels.add(dataModel);
        }
        return dataModels;
    }

    @Override
    public void clck(String id, DataModel dataModel) {
        Toast.makeText(MainActivity.this, dataModel.getName(), Toast.LENGTH_LONG).show();
    }

    public String loadJSONFromAssets() {
        String json = "";
        try {
            InputStream inputStream = getAssets().open("list.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private List<DataModel> parseJson() {
        List<DataModel> dataModels = new ArrayList<>();
        String data = loadJSONFromAssets();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.optJSONArray("doctors");
            dataModels = getDataList(jsonArray);
        } catch (Exception e) {
        }
        return dataModels;
    }
}