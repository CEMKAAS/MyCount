package com.zaroslikov.mycountjava2.db;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.zaroslikov.mycountjava2.R;

import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.MyViewHolder> {

    private Listener listener;

    private List<CountPerson> countPerson;

    public static interface Listener {
        public void onClick(int position, CountPerson countPerson);
    }

    public AdapterList(List<CountPerson> countPersonList) {
        this.countPerson = countPersonList;
    }

    public void setListener(AdapterList.Listener listener) {
        this.listener = listener;
    }

    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.nameTxt.setText(String.valueOf(countPerson.get(position).getName()));
        holder.countTxt.setText(String.valueOf(countPerson.get(position).getCount()));
        holder.timeTxt.setText("Последние изм.: "+ countPerson.get(position).getTime());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position, countPerson.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return countPerson.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, timeTxt, countTxt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.name_list);
            timeTxt = itemView.findViewById(R.id.time_list);
            countTxt = itemView.findViewById(R.id.count_list);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }


}
