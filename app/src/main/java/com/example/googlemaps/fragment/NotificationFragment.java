package com.example.googlemaps.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.googlemaps.R;
import com.example.googlemaps.databinding.FragmentNotificationBinding;
import com.example.googlemaps.databinding.FragmentProfileBinding;

public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}