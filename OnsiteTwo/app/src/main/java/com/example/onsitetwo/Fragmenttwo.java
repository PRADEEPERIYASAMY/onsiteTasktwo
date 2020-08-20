package com.example.onsitetwo;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmenttwo extends Fragment {

    public static PaintView paintView;
    public static FragmentViewModel viewModel;
    private Button button;
    private ImageView test;
    private HandlerThread thread;
    private int count = 0;

    public Fragmenttwo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView( LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate ( R.layout.fragment_fragmenttwo , container , false );
        button = view.findViewById ( R.id.but2 );
        paintView = view.findViewById ( R.id.paint2 );
        test = view.findViewById ( R.id.test );
        button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View v ) {
                viewModel.setBitmap ( PaintView.current );

                if (PaintView.listAction.size () >count){
                    count = PaintView.listAction.size ();
                    viewModel.setBitmap ( PaintView.current );
                }
            }
        } );
        paintHandler ();
        return view;
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated ( savedInstanceState );
        viewModel = ViewModelProviders.of(getActivity()).get(FragmentViewModel.class);
        viewModel.getBitmap ().observe(getViewLifecycleOwner(), new Observer<Bitmap> () {
            @Override
            public void onChanged( Bitmap bitmap ) {
                paintView.SetBitmap ( bitmap );
                test.setImageBitmap ( bitmap );

            }

        });
    }

    private void paintHandler(){

        thread = new HandlerThread ("MyHandlerThread1");
        thread.start();
        final Handler handler = new Handler ( thread.getLooper () );

        handler.post(new Runnable() {
            @Override

            public void run()
            {
                if (PaintView.listAction.size () >count){
                    count = PaintView.listAction.size ();
                    viewModel.setBitmap ( PaintView.current );
                }
                handler.postDelayed(this, 1);
            }
        });
    }
}