package pl.mdanilowski.foodbook.service;


import pl.mdanilowski.foodbook.model.Recipe;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FoodBookApi {

    @POST("/recipeLiked")
    Call<String> recipeLiked(@Query("uid") String uid, @Query("oid") String oid, @Body Recipe recipe);

}
