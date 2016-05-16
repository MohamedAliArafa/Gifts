package com.zeowls.gifts;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

/**
 * Created by nezar on 5/16/16.
 */
public class CustomDialogFragment extends DialogFragment {
    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */

    Picasso picasso;
    String ImageUrl;


    ImageView Im_Full;
    RelativeLayout Item_Full_image_linear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.item_full_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        picasso = Picasso.with(getActivity());
        Bundle bundle = getArguments();
        String ImageName="";

        if (bundle != null) {
            ImageName = bundle.getString("IMAGE_NAME");

        }


        Im_Full = (ImageView) view.findViewById(R.id.Im_Full);
        //  Im_Full.setBackground(itemPic.getDrawable());
        picasso.load(ImageName).into(Im_Full);
        Item_Full_image_linear = (RelativeLayout) view.findViewById(R.id.Item_Full_image_linear);
        Item_Full_image_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //  picasso.load("http://bubble.zeowls.com/uploads/" + item_image).fit().centerCrop().into(Im_Full);


    }

    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
