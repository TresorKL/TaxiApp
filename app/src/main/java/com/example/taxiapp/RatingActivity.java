package com.example.taxiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class RatingActivity extends AppCompatActivity {


    ImageButton star1, star2, star3, star4, star5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);

        ImageButton[] stars = {star1, star2, star3, star4, star5};




            star1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Drawable emptyStar = getResources().getDrawable(R.drawable.empty_star_icon);
                    Drawable filledStar = getResources().getDrawable(R.drawable.filled_start_icon);

                    for (int k = 0; k < 5; k++) {

                        stars[k].setBackground(emptyStar);

                    }
                    for (int j = 0; j < 1; j++) {

                        stars[j].setBackground(filledStar);

                    }


                }
            });



            star2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Drawable emptyStar = getResources().getDrawable(R.drawable.empty_star_icon);
                    Drawable filledStar = getResources().getDrawable(R.drawable.filled_start_icon);

                    for (int i = 0; i < 5; i++) {

                        stars[i].setBackground(emptyStar);

                    }
                    for (int i = 0; i < 2; i++) {

                        stars[i].setBackground(filledStar);

                    }


                }
            });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Drawable emptyStar = getResources().getDrawable(R.drawable.empty_star_icon);
                Drawable filledStar = getResources().getDrawable(R.drawable.filled_start_icon);

                for (int i = 0; i < 5; i++) {

                    stars[i].setBackground(emptyStar);

                }
                for (int i = 0; i < 3; i++) {

                    stars[i].setBackground(filledStar);

                }


            }
        });




        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Drawable emptyStar = getResources().getDrawable(R.drawable.empty_star_icon);
                Drawable filledStar = getResources().getDrawable(R.drawable.filled_start_icon);
                for (int i = 0; i < 5; i++) {

                    stars[i].setBackground(emptyStar);

                }

                for (int i = 0; i < 4; i++) {

                    stars[i].setBackground(filledStar);

                }


            }
        });


        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Drawable emptyStar = getResources().getDrawable(R.drawable.empty_star_icon);
                Drawable filledStar = getResources().getDrawable(R.drawable.filled_start_icon);

                for (int i = 0; i < 5; i++) {

                    stars[i].setBackground(emptyStar);

                }
                for (int i = 0; i < 5; i++) {

                    stars[i].setBackground(filledStar);

                }


            }
        });



    }
}