package com.iused.main;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.iused.R;
import com.iused.fragments.Fragment_ProductDetails_Negotiable;
import com.iused.fragments.PageFragment;
import com.iused.utils.DynamicPagerAdapter_SwipeProducts;
import com.iused.utils.ParallaxPageTransformer_SwipeProducts;

import java.util.ArrayList;
import java.util.List;

import static com.iused.utils.ParallaxPageTransformer_SwipeProducts.ParallaxTransformInformation.PARALLAX_EFFECT_DEFAULT;

public class ProductDetailsActivity_Negotiable_Viewpager extends AppCompatActivity {

    List<String> list_tab_names =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product_details__negotiable__viewpager);

        list_tab_names = new ArrayList<String>();
        list_tab_names= PageFragment.arrayList_unique_negotiable_product_id;

        List<Class> fragmentTypes = new ArrayList<Class>() {{
            add(Fragment_ProductDetails_Negotiable.class);

        }};
//int num=5;
        FragmentManager fm = getSupportFragmentManager();
        FragmentPagerAdapter adapter = new DynamicPagerAdapter_SwipeProducts(fm, list_tab_names,PageFragment.arrayList_unique_negotiable_product_name,PageFragment.arrayList_unique_negotiable_used_for,PageFragment.arrayList_unique_negotiable_description,PageFragment.arrayList_unique_negotiable_price,PageFragment.arrayList_unique_negotiable_condition,PageFragment.arrayList_unique_negotiable_distance,PageFragment.arrayList_unique_negotiable_posted_image, fragmentTypes);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);


        ParallaxPageTransformer_SwipeProducts pageTransformer = new ParallaxPageTransformer_SwipeProducts()
                .addViewToParallax(new ParallaxPageTransformer_SwipeProducts.ParallaxTransformInformation(R.id.view_pager, 2, 2))
                .addViewToParallax(new ParallaxPageTransformer_SwipeProducts.ParallaxTransformInformation(R.id.txt_product_name, 2, 2))
                .addViewToParallax(new ParallaxPageTransformer_SwipeProducts.ParallaxTransformInformation(R.id.mainLayout, 0,
                        PARALLAX_EFFECT_DEFAULT));
        viewPager.setPageTransformer(true, pageTransformer);

    }
}
