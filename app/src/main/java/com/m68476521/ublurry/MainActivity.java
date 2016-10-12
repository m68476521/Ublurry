package com.m68476521.ublurry;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.m68476521.ublurry.databinding.ActivityMainBinding;
import com.m68476521.ublurry.utils.ImagePicker;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_ID = 8;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);

        imageView = binding.imageView;

        binding.fab.setOnClickListener(v ->
                startActivityForResult(ImagePicker.getPickImageIntent(MainActivity.this), PICK_IMAGE_ID));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Uri uri = ImagePicker.getUriFromResult(this, resultCode, data);

                if (uri != null) {
                    Picasso.with(this).load(uri).into(imageView);
                } else {
                    Toast.makeText(this, R.string.err_select_img, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
