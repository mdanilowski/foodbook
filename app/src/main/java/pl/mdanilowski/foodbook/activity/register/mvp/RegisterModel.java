package pl.mdanilowski.foodbook.activity.register.mvp;


import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.activity.register.RegisterActivity;
import pl.mdanilowski.foodbook.model.User;

public class RegisterModel {

    private RegisterActivity activity;

    public RegisterModel(RegisterActivity activity) {
        this.activity = activity;
    }

    public RegisterActivity getActivity() {
        return activity;
    }

    void startDashboardAfterRegister(User user){
        DashboardActivity.start(activity, true, user);
    }
}
