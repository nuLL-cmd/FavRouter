package com.example.favrouter.backendRecycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.favrouter.R;

import java.util.List;

public class AdapterCustom extends RecyclerView.Adapter<AdapterCustom.DataHandler>{
    private List<DataProvider> dataProviderList;
    private View convertView;
    private DataHandler dataHandler;
    private DataProvider dataProvider;
    LayoutInflater layoutInflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void setOnClick(int position);
    }

    public void setOnOtemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public AdapterCustom(List<DataProvider> dataProviderList) {
        this.dataProviderList = dataProviderList;
    }

    @NonNull
    @Override
    public DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_celula,parent,false);
        dataHandler = new DataHandler(convertView,listener);
        return dataHandler;
    }

    @Override
    public void onBindViewHolder(@NonNull DataHandler holder, int position) {
        dataProvider = dataProviderList.get(position);
        holder.txt_location.setText(dataProvider.getLocation());
        holder.txt_latitude.setText(String.valueOf(dataProvider.getLatitude()));
        holder.txt_longitude.setText(String.valueOf(dataProvider.getLongitude()));
        holder.img_location_celula.setTag(position);
    }

    @Override
    public int getItemCount() {
        return dataProviderList.size();
    }

    static class DataHandler extends RecyclerView.ViewHolder {
        private TextView txt_location;
        private TextView txt_latitude;
        private TextView txt_longitude;
        private ImageButton img_location_celula;
        public DataHandler(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            txt_latitude = itemView.findViewById(R.id.txt_latitude);
            txt_longitude = itemView.findViewById(R.id.txt_longitute);
            txt_location = itemView.findViewById(R.id.txt_location_celula);
            img_location_celula = itemView.findViewById(R.id.img_location_celula);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!= null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.setOnClick(position);
                        }
                    }
                }
            });

        }
    }
}
