package com.example.onsitetwo;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FragmentViewModel extends ViewModel {

    public MutableLiveData<Bitmap> bitmap = new MutableLiveData<> ();

    public MutableLiveData<Bitmap> getBitmap() {
        return bitmap;
    }

    public void setBitmap( Bitmap bit ) {
        bitmap.postValue ( bit );
    }
}
