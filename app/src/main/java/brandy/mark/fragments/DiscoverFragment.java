package brandy.mark.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.view.annotation.ContentView;

import brandy.mark.R;

@ContentView(R.layout.fragment_discover)
public class DiscoverFragment extends BaseFragment {
    public static final String TAG = DiscoverFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
