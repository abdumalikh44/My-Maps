package com.example.googlemaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.googlemaps.databinding.ActivityHomeBinding;
import com.example.googlemaps.fragment.HomeFragment;
import com.example.googlemaps.fragment.MapsFragment;
import com.example.googlemaps.fragment.NotificationFragment;
import com.example.googlemaps.fragment.ProfileFragment;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ReplaceFragment(new HomeFragment());


        binding.navBottomBar.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        ReplaceFragment(new HomeFragment());
                        break;

                    case R.id.notification:
                        ReplaceFragment(new NotificationFragment());
                        break;

                    case R.id.person:
                        ReplaceFragment(new ProfileFragment());
                        break;

                }
            }
        });
    }

    private void ReplaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }


}