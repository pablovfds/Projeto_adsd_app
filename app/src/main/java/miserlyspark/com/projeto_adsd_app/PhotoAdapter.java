package miserlyspark.com.projeto_adsd_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{

    private final Context context;
    private List<String> mImagesList;

    PhotoAdapter(List<String> images, Context context) {
        this.context = context;
        this.mImagesList = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide
                .with(context)
                .load(decodeBase64(mImagesList.get(position)))
                .asBitmap()
                .into(holder.imageView);
    }

    public void updateImageList(List<String> images){
        this.mImagesList = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    private byte[] decodeBase64(String imageBase64){
        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView imageView;

        ViewHolder(View v) {
            super(v);
            this.imageView = (ImageView) v.findViewById(R.id.iv_photo);
        }
    }
}
