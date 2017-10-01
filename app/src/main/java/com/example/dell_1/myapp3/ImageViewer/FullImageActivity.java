package com.example.dell_1.myapp3.ImageViewer;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dell_1.myapp3.R;
import com.github.chrisbanes.photoview.PhotoView;

import static com.example.dell_1.myapp3.ImageViewer.ImageGallery.al_images;

public class FullImageActivity extends AppCompatActivity {
    ImageView images;
    int position;
    int folderPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        Intent i = getIntent();
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setVisibility(View.GONE);

        // Selected image id
        position = i.getExtras().getInt("id");
        folderPosition = i.getExtras().getInt("folderPosition");
        Bundle extras = getIntent().getExtras();
        String value = extras.getString("abc");

        Glide.with(FullImageActivity.this)
                .load(value)
                .skipMemoryCache(false)
                .into(images);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new TouchImageAdapter(this,al_images, folderPosition));
        mViewPager.setCurrentItem(position);
    }
}
