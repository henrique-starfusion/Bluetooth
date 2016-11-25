package br.com.starfusion.adapters;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.starfusion.R;
import br.com.starfusion.ui.Activity;
import br.com.starfusion.useful.BluetoothConnector;
import br.com.starfusion.useful.Resources;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Henrique on 24/08/2016.
 */
public class BluetoothList extends RecyclerView.Adapter<BluetoothList.BluetoothViewHolder> {

    private Context mContext;
    private BluetoothAdapter mAdapter;
    private List<BluetoothDevice> mDevice;

    public BluetoothList(Context context){
        mContext = context;
        mDevice = new ArrayList<>();

        mAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mContext.registerReceiver(mReceiver, filter);
    }

    @Override
    public int getItemCount() {
        return mDevice.size();
    }

    @Override
    public BluetoothViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth, parent, false);
        BluetoothViewHolder holder = new BluetoothViewHolder(view);
        return holder;
    }

    public void onDestroy(){
        mContext.unregisterReceiver(mReceiver);
    }

    @Override
    public void onBindViewHolder(BluetoothViewHolder holder, int index) {
        holder.setItem(mDevice.get(index), mAdapter);
    }

    public void update(){
        mDevice.clear();
        mDevice.addAll(mAdapter.getBondedDevices());
        notifyDataSetChanged();
        //mAdapter.startDiscovery();
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDevice.add(device);
            }

            notifyDataSetChanged();
        }
    };

    public static class BluetoothViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private BluetoothAdapter mAdapter;
        private BluetoothDevice mDevice;

        @Bind(R.id.device_item)
        LinearLayout mDeviceItem;

        @Bind(R.id.device_name)
        TextView mDeviceName;

        @Bind(R.id.device_address)
        TextView mDeviceAddress;

        public BluetoothViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setItem(BluetoothDevice device, BluetoothAdapter adapter){
            mDevice = device;
            mAdapter = adapter;
            mDeviceName.setText(device.getName());
            mDeviceAddress.setText(device.getAddress());
            mDeviceItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                ProgressDialog progress = new ProgressDialog(view.getContext());

                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setMessage(Resources.getChar(R.string.message_wait_connect_bluetooth));
                progress.onStart();

                BluetoothConnector connector = new BluetoothConnector(mDevice, true, mAdapter, null);
                BluetoothConnector.BluetoothSocketWrapper socket = connector.connect();

                progress.dismiss();

                Activity.openMain(view.getContext(), socket);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), "", Toast.LENGTH_LONG).show();
            }
        }
    }
}
