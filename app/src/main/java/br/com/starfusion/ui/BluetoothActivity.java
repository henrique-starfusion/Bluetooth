package br.com.starfusion.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.starfusion.R;
import br.com.starfusion.adapters.BluetoothList;
import butterknife.Bind;
import butterknife.ButterKnife;

public class BluetoothActivity extends AppCompatActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, Runnable {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @Bind(R.id.recicle_view)
    RecyclerView mRecicleView;

    BluetoothList mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        ButterKnife.bind(this);
    }

    @Override
    public void onResume(){
        super.onResume();

        /* Inicio da Configuração da Toolbar */
        mToolbar.setTitle(R.string.bluetooth_name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(this);
        /* Fim da Configuração da Toolbar */

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mAdapter = new BluetoothList(this);

        mRecicleView.setHasFixedSize(true);
        mRecicleView.setLayoutManager(manager);
        mRecicleView.setAdapter(mAdapter);

        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.post(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar:
            default:
                this.onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onRefresh() {
        try {
            mAdapter.update();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void run() {
        mSwipeRefresh.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onDestroy(){
        mAdapter.onDestroy();
        super.onDestroy();
    }
}
