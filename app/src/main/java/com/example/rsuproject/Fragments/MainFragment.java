package com.example.rsuproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rsuproject.R;

public class MainFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Proyecto RSU");
        Toast.makeText(getContext(), "Fragment principal", Toast.LENGTH_SHORT).show();

        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}