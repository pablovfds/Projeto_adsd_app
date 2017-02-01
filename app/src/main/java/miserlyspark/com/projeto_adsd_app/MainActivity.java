package miserlyspark.com.projeto_adsd_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.test.rule.logging.LogDeviceGetPropInfoRule;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.my_recycler_view) RecyclerView mRecyclerView;

    @BindView(R.id.toolbar) Toolbar toolbar;

    private PhotoAdapter mAdapter;
    private AsyncHttpClient client;
    private ProgressDialog progressDialog;

    String url = "http://192.168.130.196:5000";
    //String url = "https://adsd.herokuapp.com";

    String tam1000, tam100000;

    List<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        arrayList = new ArrayList<>();

        progressDialog = ProgressDialog.show(this, "Aguarde", "Carregando Dados");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        setRecycleView();

        // specify an adapter (see also next example)
        mAdapter = new PhotoAdapter(arrayList);
        mRecyclerView.setAdapter(mAdapter);

        client = new AsyncHttpClient();
        tam1000 = getString(R.string.large_text_1000);
        tam100000 = getString(R.string.large_text_100000);

        updateRecyclerView();

        mAdapter.setOnItemClickedListener(new PhotoAdapter.OnItemClickedListener() {

            @Override
            public void onItemClicked(String id) {
                getItemById(id);
            }
        });
    }

    @OnClick(R.id.fab)
    public void addPhoto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tamanho das palavras")
                .setItems(R.array.testArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            postTextServer(tam1000);
                        } else {
                            postTextServer(tam100000);
                        }
                    }
                });
        builder.show();
    }

    private void setRecycleView(){
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.canScrollVertically();
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void getItemById(String id){
        RequestParams requestParams = new RequestParams();
        requestParams.put("_id", id);
        client.get(this, url +  "/image",requestParams, new JsonHttpResponseHandler() {
            long begin;
            long end;
            @Override
            public void onStart() {
                begin = SystemClock.currentThreadTimeMillis();
                Log.d("Begin", String.valueOf(begin));
            }

            @Override
            public void onFinish() {
                end = SystemClock.currentThreadTimeMillis();
                Log.d("End", String.valueOf(end));
                Log.d("Total", String.valueOf(end - begin));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                for (int i = 0; i < timeline.length(); i++) {
                    try {
                        Log.d("item", timeline.getJSONObject(i).getString("_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("error", "get JSONObject");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("error", "get JSONObject");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("error", "get JSONObject");
            }
        });
    }

    private void updateRecyclerView(){
        progressDialog.show();

        client.get(this, url +  "/imagelist", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                for (int i = 0; i < timeline.length(); i++) {
                    try {
                        arrayList.add(timeline.getJSONObject(i).getString("_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
                mAdapter.updateImageList(arrayList);
                Log.d("responde",mAdapter.getItemCount()+"");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("error", "get JSONObject");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("error", "get JSONObject");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("error", "get JSONObject");
            }
        });
    }



    public void postTextServer(String text){
        RequestParams requestParams = new RequestParams();
        requestParams.put("image", text);
        client.post(url + "/image",requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
               Log.d("foi", timeline.toString());
                try {
                    String id = timeline.getString("_id");
                    arrayList.add(id);
                    mAdapter.updateImageList(arrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                Log.d("nao foi JSONObject", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("nao foi Throwable", responseString);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("nao foi JSONArray", errorResponse.toString());
            }
        });

    }
}
