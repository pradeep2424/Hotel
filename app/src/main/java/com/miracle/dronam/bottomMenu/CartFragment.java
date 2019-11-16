package com.miracle.dronam.bottomMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miracle.dronam.R;


public class CartFragment extends Fragment {
    View rootView;
    LinearLayout llBrowseMenu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        init();
        componentEvents();

        return rootView;
    }

    private void init()
    {
        llBrowseMenu = rootView.findViewById(R.id.ll_browseMenu);
    }

    private void componentEvents() {
        llBrowseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToHomeFragment1();
            }
        });
    }

    public void switchToHomeFragment1() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, new HomeFragment());
        transaction.commit();
    }
}
