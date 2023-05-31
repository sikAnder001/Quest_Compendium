package com.questcompendium.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.questcompendium.R;
import com.questcompendium.adapter.SearchListAdapter;
import com.questcompendium.model.Search;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListScreenActivity extends AppCompatActivity {
    @BindView(R.id.rlSearchList)
    RecyclerView rlSearchList;
    ArrayList<Search> searchList = new ArrayList<>();
    String backgimg, fsHotelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);

        Intent intent = getIntent();
        if (intent != null) {
            //search = intent.getStringExtra("search");
            searchList = this.getIntent().getExtras().getParcelableArrayList("search");
            backgimg = this.getIntent().getExtras().getString("backgimg");
            fsHotelId = this.getIntent().getExtras().getString("fsHotelId");
            if (searchList != null) {
                initialize();
            }
        }
    }

    @OnClick(R.id.llBack)
    public void changeActivity() {
        onBackPressed();
    }

    private void initialize() {
        ButterKnife.bind(this);
        SearchListAdapter searchListAdapter = new SearchListAdapter(ListScreenActivity.this, searchList, backgimg, fsHotelId);
        rlSearchList.setAdapter(searchListAdapter);
    }
}