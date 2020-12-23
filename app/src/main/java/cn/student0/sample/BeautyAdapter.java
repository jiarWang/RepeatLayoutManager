package cn.student0.sample;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : wangjian
 * @Data : 2020-12-20 14:26
 * @Describe :
 */
public class BeautyAdapter extends RecyclerView.Adapter<BeautyAdapter.VHolder> {

    private List<String> mData = new ArrayList<>();

    {
        for (int i = 0; i < 20; i++) {
            mData.add("" + i);
        }
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new VHolder(inflater.inflate(R.layout.item_demo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        String text = mData.get(position % mData.size());
        holder.tvDesc.setText(text);
        holder.data = text;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class VHolder extends RecyclerView.ViewHolder {
        private String data;

        private TextView tvDesc;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.tv_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data == null) return;
                    Toast.makeText(v.getContext(), data, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
