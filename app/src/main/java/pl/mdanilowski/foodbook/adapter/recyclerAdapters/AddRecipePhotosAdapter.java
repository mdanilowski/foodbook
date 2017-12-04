package pl.mdanilowski.foodbook.adapter.recyclerAdapters;


import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;

public class AddRecipePhotosAdapter extends RecyclerView.Adapter<AddRecipePhotosAdapter.PhotoHolder> {

    private ArrayList<Bitmap> takenImages = new ArrayList<>();

    public void setTakenImages(ArrayList<Bitmap> takenImages) {
        this.takenImages = takenImages;
        notifyDataSetChanged();
    }

    public void addImage(Bitmap image) {
        takenImages.add(image);
        notifyItemInserted(takenImages.size() - 1);
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_photo_card, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        holder.ivSmallFoodImage.setImageBitmap(takenImages.get(position));
    }

    @Override
    public int getItemCount() {
        return takenImages.size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivSmallFoodImage)
        ImageView ivSmallFoodImage;

        public PhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
