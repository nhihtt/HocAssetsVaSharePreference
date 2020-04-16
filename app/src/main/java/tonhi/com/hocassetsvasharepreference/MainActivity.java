package tonhi.com.hocassetsvasharepreference;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;

import tonhi.com.hocassetsvasharepreference.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayAdapter<String>fontAdapter;
    int lastSelected=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadFontsFromAssets();
        addEvents();
    }

    private void addEvents() {
        binding.lvFont.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeFont(position);
                lastSelected=position;
                xuLyNgheNhac();
            }
        });

    }

    private void xuLyNgheNhac() {
        try{
            AssetFileDescriptor afd=getAssets().openFd("musics/y2mate.com - [Vietsub + Lyrics] Pretty's On The Inside - Chloe Adams_SeQ1OBwwWK4.mp3");
            MediaPlayer player= new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            afd.close();
            player.prepare();
            player.start();
        }
        catch (Exception ex){

            Log.e("Loi",ex.toString());
        }
    }

    private void changeFont(int position) {
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/"
        +fontAdapter.getItem(position));
        binding.textView2.setTypeface(typeface);
    }

    private void loadFontsFromAssets() {
        try {
            AssetManager assetManager=getAssets();
            String []fonts= assetManager.list("fonts");
            fontAdapter =new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_list_item_1,fonts);
            binding.lvFont.setAdapter(fontAdapter);
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences= getSharedPreferences("MY_SHARE",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("VITRI_CHON_CUOICUNG",lastSelected);
        editor.commit();//xác nhận lưu dữ liệu trạng thái người dùng
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences= getSharedPreferences("MY_SHARE",MODE_PRIVATE);
        lastSelected=preferences.getInt("VITRI_CHON_CUOICUNG",-1);
        if(lastSelected==-1)
            binding.lvFont.setSelection(lastSelected);
    }

}
