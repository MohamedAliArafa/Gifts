package com.zeowls.gifts.ImageSlider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeowls.gifts.R;

/**
 * Created by Nezar Saleh on 3/29/2016.
 */
public class ScreenSlidePageFragment extends Fragment {


    TextView textView;

    String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        textView = (TextView) view.findViewById(R.id.SlideText);
        textView.setText(name);
    }


    public void setName(String name){
        this.name = name;
    }




}
