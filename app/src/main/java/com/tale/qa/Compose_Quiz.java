package com.tale.qa;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.tale.qa.compose_quiz.ktc_tab.Activity_Ktc_tab;
import com.tale.qa.compose_quiz.question_tab.Question_tab_Activity;

@SuppressWarnings("deprecation")
public class Compose_Quiz extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose__quiz);

        TabHost thost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = thost.newTabSpec("Question tab");
        TabHost.TabSpec tab2 = thost.newTabSpec("KeytoC tab");

        tab1.setIndicator("Question");
        //tab1.setIndicator("",getResources().getDrawable(R.drawable.profile));   <com.tale.qa.layout.activity_compose__quiz/>
        tab1.setContent(new Intent(this,Question_tab_Activity.class));

        tab2.setIndicator("KeytoC..");
        //tab2.setIndicator("",getResources().getDrawable(R.drawable.classs));
        tab2.setContent(new Intent(this, Activity_Ktc_tab.class));

        thost.addTab(tab1);
        thost.addTab(tab2);

    }

}
