package pl.mdanilowski.foodbook.activity.recipeDetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
//
//        List<String> list = Arrays.asList("https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png",
//                "https://www.w3schools.com/howto/img_fjords.jpg",
//                "https://www.w3schools.com/w3css/img_lights.jpg",
//                "http://www.menucool.com/slider/prod/image-slider-2.jpg",
//                "http://images.freeimages.com/images/small-previews/2fe/butterfly-1390152.jpg",
//                "http://www.dam7.com/Images/Puppy/images/myspace-puppy-images-0005.jpg");
//        recipeImages.setAdapter(new RecipePagerAdapter(this, list));
//        tabDots.setupWithViewPager(recipeImages);

    }
}
