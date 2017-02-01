package miserlyspark.com.projeto_adsd_app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import java.util.List;

class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{

    private List<String> list;

    private OnItemClickedListener mItemClickListener;

    public void setOnItemClickedListener(OnItemClickedListener l) {
        mItemClickListener = l;
    }

    PhotoAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    public void updateImageList(List<String> images){
        this.list = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView textView;

        ViewHolder(View v) {
            super(v);
            this.textView = (TextView) v.findViewById(R.id.iv_text);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        final String id = textView.getText().toString();
                        mItemClickListener.onItemClicked(id);
                    }
                }
            });
        }


    }

    public interface OnItemClickedListener {
        void onItemClicked(String id);
    }
}
