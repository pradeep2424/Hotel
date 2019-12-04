package com.miracle.dronam.bottomMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.miracle.dronam.R;
import com.miracle.dronam.activities.ManageAddressesActivity;
import com.miracle.dronam.activities.PaymentMethodsActivity;
import com.miracle.dronam.adapter.RecycleAdapterProfile;
import com.miracle.dronam.listeners.OnRecyclerViewClickListener;
import com.miracle.dronam.main.MainActivity;
import com.miracle.dronam.model.ProfileObject;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements OnRecyclerViewClickListener {
    View rootView;
    MainActivity mainActivity;

    private Toolbar toolbar;
    private RecyclerView rvProfile;
    private LinearLayout llManageAddresses;

    private RecycleAdapterProfile adapterProfile;
    private ArrayList<ProfileObject> listProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        initComponents();
        componentEvents();
        setupToolbar();
        setupRecyclerViewProfile();

        return rootView;
    }

    private void initComponents() {
        rvProfile = rootView.findViewById(R.id.rv_profile);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        llManageAddresses = rootView.findViewById(R.id.ll_manageAddresses);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
            }
        };
        handler.postDelayed(runnable, 400);

    }

    private void componentEvents() {
        llManageAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManageAddressesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupToolbar() {
//        toolbar.setTitle("Your Fragment Title");
        toolbar.setTitle("");
        mainActivity.setSupportActionBar(toolbar);

    }

    private void setupRecyclerViewProfile() {
        getProfileData();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvProfile.setLayoutManager(layoutManager);
        rvProfile.setItemAnimator(new DefaultItemAnimator());


        adapterProfile = new RecycleAdapterProfile(getActivity(), listProfile);
        rvProfile.setAdapter(adapterProfile);
        adapterProfile.setClickListener(this);
    }

    private void getProfileData() {
        String[] title = {getString(R.string.profile_payment_methods),
                getString(R.string.profile_reward_credits),
                getString(R.string.profile_settings),
                getString(R.string.profile_invite_friends)};

        Integer[] icon = {R.mipmap.profile_payment_method, R.mipmap.profile_reward_credits,
                R.mipmap.profile_settings, R.mipmap.profile_invite_friends};

        listProfile = new ArrayList<>();
        for (int i = 0; i < icon.length; i++) {
            ProfileObject profileObject = new ProfileObject(title[i], icon[i]);
            listProfile.add(profileObject);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onClick(View view, int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(getActivity(), PaymentMethodsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
