package pl.mdanilowski.foodbook.activity.login.mvp;


import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.activity.login.LoginActivity;

public class LoginModel {

    private LoginActivity activity;

    public LoginModel(LoginActivity activity) {
        this.activity = activity;
    }

    public LoginActivity getActivity() {
        return activity;
    }

    public void startDashboardActivity(){
        DashboardActivity.start(activity);
    }
}
