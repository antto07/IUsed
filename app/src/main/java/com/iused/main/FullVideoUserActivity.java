package com.iused.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.iused.R;

import java.io.IOException;

/**
 * Created by Antto on 16/12/2016.
 */

public class FullVideoUserActivity extends AppCompatActivity {

    private FullscreenVideoLayout videoLayout;
    public String str_video=null;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_full_video);

        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        intent=getIntent();

        videoLayout.setActivity(this);
        videoLayout.setShouldAutoplay(true);
        videoLayout.setFullscreen(true);
        loadVideo();

    }

    public void loadVideo() {
//        Uri videoUri = Uri.parse("http://techslides.com/demos/sample-videos/small.mp4");
        try {
            videoLayout.setVideoURI(Uri.parse(intent.getStringExtra("video_url")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset(View v) {
        if (this.videoLayout != null) {
            this.videoLayout.reset();
            loadVideo();
        }
    }

}
