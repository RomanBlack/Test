package ru.romanblack.test.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import ru.romanblack.test.ui.activity.MainActivity;

public abstract class MainActivityFragment extends BaseFragment {

    private MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mainActivity = null;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }
}
