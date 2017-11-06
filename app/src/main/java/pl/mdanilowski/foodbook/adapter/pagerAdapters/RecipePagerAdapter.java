package pl.mdanilowski.foodbook.adapter.pagerAdapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pl.mdanilowski.foodbook.R;

public class RecipePagerAdapter extends PagerAdapter {

    List<String> imagesUrls = new ArrayList();
    LayoutInflater inflater;
    Context context;


    public RecipePagerAdapter(Context context, List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.view_slider_image, container, false);
        assert view != null;
        ImageView image = view.findViewById(R.id.sliderImage);
        Glide.with(container).load(imagesUrls.get(position)).into(image);
        container.addView(view, 0);
        return view;
    }

    @Override
    public int getCount() {
        return imagesUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
