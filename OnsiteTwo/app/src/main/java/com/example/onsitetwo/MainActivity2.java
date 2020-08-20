package com.example.onsitetwo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private MediaPlayer mediaPlayer1,mediaPlayer2,mediaPlayer3;
    private Button color,brush,eraser;
    private static int brushColor;
    private static int brushSize = 12,eraserSize = 12;
    private static Bitmap bitmap;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView ( R.layout.activity_main2 );

        mediaPlayer1 = new MediaPlayer ().create(getApplicationContext (),R.raw.negative);
        mediaPlayer2 = new MediaPlayer ().create(getApplicationContext (),R.raw.rubberone);
        mediaPlayer3 = new MediaPlayer ().create(getApplicationContext (),R.raw.tick);

        eraser = findViewById ( R.id.eraser );
        color = findViewById ( R.id.color );
        brush = findViewById ( R.id.brush );

        getSupportFragmentManager().beginTransaction()
                .replace (R.id.fragment_one, new fragmentone ())
                .replace (R.id.fragment_two, new Fragmenttwo ())
                .commit();

        eraser.setOnClickListener ( new View.OnClickListener () {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick( View v ) {
                mediaPlayer1.start ();
                Fragmenttwo.paintView.enableEraser ();
                fragmentone.paintView.enableEraser ();
                AlertDialog.Builder builder = new AlertDialog.Builder ( MainActivity2.this,R.style.CustomDialog);
                View view = LayoutInflater.from (MainActivity2.this).inflate ( R.layout.brush_dialog,null,false );
                TextView toolsSelected = view.findViewById ( R.id.status_tools_selected );
                Button button = view.findViewById ( R.id.yes );
                final TextView statusSize = view.findViewById ( R.id.status_size );
                ImageView ivTools = view.findViewById ( R.id.iv_tools );
                SeekBar seekBar = view.findViewById ( R.id.seekbar_size );
                seekBar.setMax ( 99 );
                toolsSelected.setText ( "Eraser Size" );
                ivTools.setImageResource ( R.drawable.wrong );
                statusSize.setText ( "Selected Size :"+String.valueOf ( eraserSize ) );
                seekBar.setProgress ( eraserSize,true );

                final AlertDialog alertDialog = builder.create ();
                alertDialog.setView ( view );

                seekBar.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener () {
                    @Override
                    public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser ) {
                        eraserSize = progress+1;
                        statusSize.setText ( "Selected Size :"+String.valueOf ( eraserSize ) );
                        Fragmenttwo.paintView.setSizeEraser ( eraserSize );
                        fragmentone.paintView.setSizeEraser ( eraserSize );
                    }

                    @Override
                    public void onStartTrackingTouch( SeekBar seekBar ) {

                    }

                    @Override
                    public void onStopTrackingTouch( SeekBar seekBar ) {

                    }
                } );

                button.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick( View v ) {
                        alertDialog.cancel ();
                        full ();
                        mediaPlayer2.start ();
                    }
                } );

                alertDialog.setCanceledOnTouchOutside ( false );
                alertDialog.show ();
            }
        } );

        color.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View v ) {
                Fragmenttwo.paintView.disableEraser ();
                fragmentone.paintView.disableEraser ();
                mediaPlayer1.start ();
                int color = brushColor;
                ColorPickerDialogBuilder
                        .with ( MainActivity2.this )
                        .setTitle ( "Choose Color" )
                        .initialColor ( color )
                        .wheelType ( ColorPickerView.WHEEL_TYPE.FLOWER )
                        .density ( 12 )
                        .setPositiveButton ( "Ok", new ColorPickerClickListener () {
                            @Override
                            public void onClick( DialogInterface d, int lastSelectedColor, Integer[] allColors ) {
                                brushColor = lastSelectedColor;
                                Fragmenttwo.paintView.setBrushColor ( brushColor );
                                fragmentone.paintView.setBrushColor ( brushColor );
                                full ();
                                mediaPlayer2.start ();
                            }
                        } ).setNegativeButton ( "Cancel", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        mediaPlayer2.start ();
                        full ();
                    }
                } ).build ().show ();
            }
        } );

        brush.setOnClickListener ( new View.OnClickListener () {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick( View v ) {
                Fragmenttwo.paintView.disableEraser ();
                fragmentone.paintView.disableEraser ();
                mediaPlayer1.start ();
                AlertDialog.Builder builder = new AlertDialog.Builder ( MainActivity2.this , R.style.CustomDialog );
                View view = LayoutInflater.from ( MainActivity2.this ).inflate ( R.layout.brush_dialog , null , false );
                TextView toolsSelected = view.findViewById ( R.id.status_tools_selected );
                Button button = view.findViewById ( R.id.yes );
                final TextView statusSize = view.findViewById ( R.id.status_size );
                ImageView ivTools = view.findViewById ( R.id.iv_tools );
                SeekBar seekBar = view.findViewById ( R.id.seekbar_size );
                seekBar.setMax ( 99 );

                toolsSelected.setText ( "Brush Size" );
                ivTools.setImageResource ( R.drawable.paintbrush );
                statusSize.setText ( "Selected Size :" + String.valueOf ( brushSize ) );
                seekBar.setProgress ( brushSize , true );

                final AlertDialog alertDialog = builder.create ();
                alertDialog.setView ( view );

                seekBar.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener () {
                    @Override
                    public void onProgressChanged( SeekBar seekBar , int progress , boolean fromUser ) {
                        brushSize = progress + 1;
                        statusSize.setText ( "Selected Size :" + String.valueOf ( brushSize ) );
                        Fragmenttwo.paintView.setBrushSize ( brushSize );
                        fragmentone.paintView.setBrushSize ( brushSize );
                    }

                    @Override
                    public void onStartTrackingTouch( SeekBar seekBar ) {

                    }

                    @Override
                    public void onStopTrackingTouch( SeekBar seekBar ) {

                    }
                } );

                button.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick( View v ) {
                        alertDialog.cancel ();
                        full ();
                        mediaPlayer2.start ();
                    }
                } );

                alertDialog.setCanceledOnTouchOutside ( false );
                alertDialog.show ();
            }
        } );
    }


    @Override
    public void onBackPressed() {
        exit ();
    }

    private void full(){
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void exit(){
        mediaPlayer1.start ();
        final AlertDialog.Builder builder = new AlertDialog.Builder ( MainActivity2.this,R.style.CustomDialog );
        View view = LayoutInflater.from ( MainActivity2.this ).inflate ( R.layout.dialog,null,false );
        Button yes = view.findViewById ( R.id.yes );
        Button no = view.findViewById ( R.id.no );
        TextView firstText = view.findViewById ( R.id.firstText );
        TextView secondText = view.findViewById ( R.id.secontText );
        ImageView dialogImage = view.findViewById ( R.id.dialog_image );

        firstText.setText ( "Oh noooo !" );
        secondText.setText ( "Wanna move out?" );
        dialogImage.setImageResource ( R.drawable.oh );
        final AlertDialog alertDialog = builder.create ();
        alertDialog.setView ( view );

        no.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View v ) {
                full ();
                alertDialog.cancel ();
                mediaPlayer2.start ();
            }
        } );

        yes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View v ) {
                finish ();
                mediaPlayer2.start ();
            }
        } );
        alertDialog.setCanceledOnTouchOutside ( false );
        alertDialog.show ();
    }

}