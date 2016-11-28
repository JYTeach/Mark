package brandy.mark.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import brandy.mark.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMovieFragment extends BaseFragment {
    public static final String TAG = MyMovieFragment.class.getSimpleName();

    public MyMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_movie, container, false);
    }

}