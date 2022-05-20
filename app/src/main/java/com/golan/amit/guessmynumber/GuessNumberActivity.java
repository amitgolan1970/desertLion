package com.golan.amit.guessmynumber;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class GuessNumberActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivLion;
    EditText etGuess;
    Button btnGuess;
    TextView tvInfo;
    GussNumberHelper gnh;
    Animation animRotateRight, animRotateLeft;
    Animation animScale;
    SoundPool sp;
    int winSound, looseSound;
    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_number);

        init();
    }

    private void init() {
        ivLion = (ImageView)findViewById(R.id.ivDesertLion);
        etGuess = (EditText)findViewById(R.id.etGuessId);
        etGuess.requestFocus();
        btnGuess = (Button)findViewById(R.id.btnGuessId);
        btnGuess.setOnClickListener(this);
        tvInfo = (TextView)findViewById(R.id.tvInformation);
        gnh = new GussNumberHelper();
        animRotateRight = AnimationUtils.loadAnimation(this, R.anim.anim_rotete_right);
        animRotateLeft = AnimationUtils.loadAnimation(this, R.anim.anim_rotete_left);
        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME). build();
            sp = new SoundPool.Builder()
                    .setMaxStreams(10).setAudioAttributes(aa).build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }
        winSound = sp.load(this, R.raw.cheering, 1);
        looseSound = sp.load(this, R.raw.failtrombone, 1);

        sw = (Switch)findViewById(R.id.swId);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    gnh.gen();
                    Toast.makeText(GuessNumberActivity.this, "changed to advance mode ("
                            + GussNumberHelper.LOW + "-" + GussNumberHelper.HIGH + ")", Toast.LENGTH_SHORT).show();
                    if(MainActivity.DEBUG) {
                        Log.d(MainActivity.DEBUGTAG, "changed to advance. now numer is: " + gnh.get_number());
                    }
                } else {
                    gnh.genBeginner();
                    Toast.makeText(GuessNumberActivity.this, "changed to beginner mode ("
                            + GussNumberHelper.LOWBEGINNER + "-" + GussNumberHelper.HIGHBEGINNER + ")", Toast.LENGTH_SHORT).show();
                    if(MainActivity.DEBUG) {
                        Log.d(MainActivity.DEBUGTAG, "changed to beginner. now numer is: " + gnh.get_number());
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == btnGuess) {
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "button clicked");
            }
            String guessStr = null;
            try {
                if(etGuess.getText() == null || etGuess.getText().toString().length() == 0) {
                    Toast.makeText(this, "empty number is not allowed", Toast.LENGTH_SHORT).show();
                } else {
                    guessStr = etGuess.getText().toString();
                    int tmpNumInt = Integer.parseInt(guessStr);
                    etGuess.setText("");
                    if(tmpNumInt == gnh.get_number()) {
                        Toast.makeText(this, "WON: " + tmpNumInt + " = " + gnh.get_number(), Toast.LENGTH_SHORT).show();
                        tvInfo.setText("Won in " + gnh.get_attempts() + " attempt" + (gnh.get_attempts() == 1 ? "" : "s"));
                        gnh.gen();
                        gnh.resetAttempts();
                        ivLion.startAnimation(animRotateRight);
                        sp.play(winSound, 1, 1, 0, 0, 1);
                        v.setAlpha(1);
                        sw.setChecked(true);
                    }
                    else {
                        if (gnh.get_number() < tmpNumInt) {
                            Toast.makeText(this, "Lower", Toast.LENGTH_SHORT).show();
                            if(MainActivity.DEBUG) {
                                Log.d(MainActivity.DEBUGTAG, "Lower: " + gnh.get_number() + "<" + tmpNumInt);
                            }
                            tvInfo.setText("Lower. " + gnh.get_attempts() + " attempt" + (gnh.get_attempts() == 1 ? "" : "s"));
                        } else {
                            Toast.makeText(this, "Higher", Toast.LENGTH_SHORT).show();
                            if(MainActivity.DEBUG) {
                                Log.d(MainActivity.DEBUGTAG, "Higher: " + gnh.get_number() + ">" + tmpNumInt);
                            }
                            tvInfo.setText("Higher. " + gnh.get_attempts() + " attempt" + (gnh.get_attempts() == 1 ? "" : "s"));
                        }
                        if(gnh.get_attempts() == GussNumberHelper.ALLOWEDATTEMPTS) {
                            sp.play(looseSound, 1, 1, 0, 0, 1);
                            v.setAlpha((float)0.8);
                            Toast.makeText(this, "LOST. no more attempts. Number was: " + gnh.get_number(), Toast.LENGTH_SHORT).show();
                            if(MainActivity.DEBUG) {
                                Log.d(MainActivity.DEBUGTAG, "Lost. attempts are over");
                            }
                            tvInfo.setText("Lost. All attempts are over. number was: " + gnh.get_number());
                            gnh.gen();
                            gnh.resetAttempts();
                            ivLion.startAnimation(animScale);
                            sw.setChecked(true);
                        } else {
                            gnh.increaseAttempts();
                        }
                    }
                    if(MainActivity.DEBUG) {
                        Log.d(MainActivity.DEBUGTAG, "input: " + tmpNumInt + ", number is: " + gnh.get_number());
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "illegal input, enter number", Toast.LENGTH_SHORT).show();
                Log.e(MainActivity.DEBUGTAG, "illegal input, enter number");
            }
        }
    }

}
