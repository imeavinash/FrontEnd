package com.avinashbehera.sabera.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.avinashbehera.sabera.util.Constants;
import com.avinashbehera.sabera.R;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private Button doneButton;
    private ArrayList<String> mCategoriesList;
    RelativeLayout screen;
    int screenWidth;
    int screenHeight;
    private static final String TAG = CategoryActivity.class.getSimpleName();
    private Button entButton;
    private Button acadButton;
    private Button miscButton;
    private String requestFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        doneButton = (Button) findViewById(R.id.doneButton);
        mCategoriesList = new ArrayList<>();
        getSupportActionBar().setTitle("CategoryActivity");






        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Log.d(TAG, "requestFrom = " + requestFrom);
                    //PrefUtilsPostQn.updateCategory(CategoryActivity.this,mCategoriesList);

                    Intent returnIntent = new Intent();
                    returnIntent.putStringArrayListExtra(Constants.EXTRA_RESULT_CATEGORY, mCategoriesList);
                    setResult(RESULT_OK, returnIntent);
                    finish();






            }
        });
    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {

            case R.id.checkbox_movies:
                if (checked)
                   mCategoriesList.add("movies");
                else
                   mCategoriesList.remove("movies");
                break;

            case R.id.checkbox_music:
                if (checked)
                    mCategoriesList.add("music");
                else
                    mCategoriesList.remove("music");
                break;

            case R.id.checkbox_sports:
                if (checked)
                    mCategoriesList.add("sports");
                else
                    mCategoriesList.remove("sports");
                break;

            case R.id.checkbox_tv:
                if (checked)
                    mCategoriesList.add("tv");
                else
                    mCategoriesList.remove("tv");
                break;

            case R.id.checkbox_science:
                if (checked)
                    mCategoriesList.add("science");
                else
                    mCategoriesList.remove("science");
                break;

            case R.id.checkbox_tech:
                if (checked)
                    mCategoriesList.add("tech");
                else
                    mCategoriesList.remove("tech");
                break;

            case R.id.checkbox_history:
                if (checked)
                    mCategoriesList.add("history");
                else
                    mCategoriesList.remove("history");
                break;

            case R.id.checkbox_politics:
                if (checked)
                    mCategoriesList.add("politics");
                else
                    mCategoriesList.remove("politics");
                break;

            case R.id.checkbox_geography:
                if (checked)
                    mCategoriesList.add("geography");
                else
                    mCategoriesList.remove("geography");
                break;

            case R.id.checkbox_literature:
                if (checked)
                    mCategoriesList.add("literature");
                else
                    mCategoriesList.remove("literature");
                break;

            case R.id.checkbox_philosophy:
                if (checked)
                    mCategoriesList.add("philosophy");
                else
                    mCategoriesList.remove("philosophy");
                break;

            case R.id.checkbox_bullshit:
                if (checked)
                    mCategoriesList.add("bullshit");
                else
                    mCategoriesList.remove("bullshit");
                break;

            case R.id.checkbox_no_bullshit:
                if (checked)
                    mCategoriesList.add("nobullshit");
                else
                    mCategoriesList.remove("nobullshit");
                break;

        }
    }


}
