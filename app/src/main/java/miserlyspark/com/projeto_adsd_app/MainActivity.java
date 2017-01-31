package miserlyspark.com.projeto_adsd_app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_PERMISSION = 2;
    @BindView(R.id.my_recycler_view) RecyclerView mRecyclerView;

    @BindView(R.id.toolbar) Toolbar toolbar;

    private PhotoAdapter mAdapter;
    private AsyncHttpClient client;
    private GalleryPhoto galleryPhoto;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        List<String> imagesList = new ArrayList<>();

        progressDialog = ProgressDialog.show(this, "Aguarde", "Carregando Dados");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PhotoAdapter(imagesList, this);
        mRecyclerView.setAdapter(mAdapter);

        client = new AsyncHttpClient();

        updateRecyclerView();
    }

    @OnClick(R.id.fab)
    public void addPhoto(){


    }

    private void updateRecyclerView(){
        progressDialog.show();
        client.get(this, "https://adsd.herokuapp.com/images", new JsonHttpResponseHandler() {
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                List<String> newImagesList = new ArrayList<>();

                for (int i = 0; i < timeline.length(); i++) {
                    try {
                        newImagesList.add(timeline.getJSONObject(i).getString("title"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
                mAdapter.updateImageList(newImagesList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();

                Toast.makeText(MainActivity.this, "Erro ao carreagar lista : " + statusCode
                        + " ------" + errorResponse, Toast.LENGTH_SHORT).show();
                
            }
        });
    }
}
