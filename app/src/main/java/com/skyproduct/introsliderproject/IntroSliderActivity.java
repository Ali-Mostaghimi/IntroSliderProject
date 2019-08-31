package com.skyproduct.introsliderproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroSliderActivity extends AppCompatActivity {

    Button btnNext, btnSkip;
    LinearLayout dotsLayout;
    ViewPager viewPager;
    SliderPagerAdapter pagerAdapter;

    SliderPrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        changeStatusBarColor();
        prefManager = new SliderPrefManager(this);
        if(! prefManager.startSlider()){
            launchMainScreen();
            return;
        }
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new SliderPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        showDots(viewPager.getCurrentItem());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //dots
                showDots(position);
                //last page
                if(position == viewPager.getAdapter().getCount() - 1){
                    btnSkip.setVisibility(View.GONE);
                    btnNext.setText(R.string.gotit);
                }else{
                    btnSkip.setVisibility(View.VISIBLE);
                    btnNext.setText(R.string.next);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastPage = viewPager.getAdapter().getCount() - 1;
                int currentPage = viewPager.getCurrentItem();
                if(currentPage == lastPage){
                    prefManager.setStartSlider(false);
                    launchMainScreen();
                }else{
                    viewPager.setCurrentItem(currentPage+1);
                }


            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainScreen();
            }
        });
    }

    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    public void launchMainScreen() {
        Intent intent  = new Intent(IntroSliderActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDots(int pageNumber){
        TextView [] dots = new TextView[viewPager.getAdapter().getCount()];
        dotsLayout.removeAllViews();
        for(int i=0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            dots[i].setTextColor(ContextCompat.getColor(this,
                    (i == pageNumber ? R.color.dot_active : R.color.dot_inactive)
            ));
            dotsLayout.addView(dots[i]);

        }
    }


    public class SliderPagerAdapter extends PagerAdapter{

        String[] slideTitles;
        String[] slideDescriptions;
        int[] imageIds = {R.drawable.ic_food, R.drawable.ic_movie,
                R.drawable.ic_discount, R.drawable.ic_travel};
        int[] bgColorIds = {R.color.slide_1_bg_color, R.color.slide_2_bg_color,
                R.color.slide_3_bg_color, R.color.slide_4_bg_color};

        public SliderPagerAdapter(){
            slideTitles = getResources().getStringArray(R.array.slide_titles);
            slideDescriptions = getResources().getStringArray(R.array.slide_descriptions);

        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(IntroSliderActivity.this)
                    .inflate(R.layout.slide, container, false);
            //Init slide fields
            view.findViewById(R.id.bglayout).setBackgroundColor(
                    ContextCompat.getColor(IntroSliderActivity.this, bgColorIds[position])
            );
            ((ImageView)view.findViewById(R.id.slide_image)).setImageResource(imageIds[position]);
            ((TextView) view.findViewById(R.id.slide_title)).setText(slideTitles[position]);
            ((TextView) view.findViewById(R.id.slide_desc)).setText(slideDescriptions[position]);

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return bgColorIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
 }
