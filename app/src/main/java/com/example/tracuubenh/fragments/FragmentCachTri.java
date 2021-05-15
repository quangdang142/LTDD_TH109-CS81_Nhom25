package com.example.tracuubenh.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tracuubenh.DinhNghiaBenh;
import com.example.tracuubenh.R;

public class FragmentCachTri extends Fragment {
    public FragmentCachTri(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_definition,container, false); //inflate layout

        Context context = getActivity();
        TextView text = (TextView) view.findViewById(R.id.textViewD);
        String cachTri = ((DinhNghiaBenh)context).cachTri;
        text.setText(cachTri);
        if(cachTri==null)
        {
            text.setText("không tìm thấy");
        }
        return view;
    }
}
