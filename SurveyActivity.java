package com.crosscipher.survey_revision2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static xdroid.toaster.Toaster.toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import xdroid.core.Global;
import xdroid.toaster.Toaster;

public class SurveyActivity extends AppCompatActivity {
    List<String> questionInput;
    DatabaseReference rootref;
    TextView questionTextView;
    int questionCount;
    getJsonFile asyncTask;
    Button nextBtn;
    EditText editText;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        rootref=FirebaseDatabase.getInstance().getReference();
            editText= findViewById(R.id.messageEdit);
        nextBtn=findViewById(R.id.next);

       user=FirebaseAuth.getInstance().getCurrentUser();

        String phoneNumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        ref.child("phonenumber").setValue(phoneNumber);

        questionTextView=findViewById(R.id.questionTxt);
/*
        List<String> questions = new ArrayList<>();
        questions.add("What is your Name?");
        questions.add("What is your gender?");
        questions.add("How old are you?");
        questions.add("What is your location?");
        questions.add("What is your parents'?");

        for(String question : questions) {
            rootref.child("questions").child(question).setValue(true);
        }*/


        /*rootref.child("questions").child("1").setValue("What is your Name?");
        rootref.child("questions").child("2").setValue("What is your gender?");
        rootref.child("questions").child("3").setValue("How old are you?");
        rootref.child("questions").child("4").setValue("What is your location?");
        rootref.child("questions").child("5").setValue("What is your parents'?");
        rootref.child("questions").child("6").setValue("Are you human?");
        rootref.child("questions").child("7").setValue("Are you alive?");*/





        asyncTask= new getJsonFile();
        asyncTask.execute();
    }


    @SuppressLint("StaticFieldLeak")
    public class getJsonFile extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {

                DatabaseReference questionRef = rootref.child("questions");
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count=1;
                        questionInput = new ArrayList<>();
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String question = ds.getValue().toString();
                            questionInput.add(count +" : "+question);
                            count++;
                        }
                        questionCount = count-1;

                        questionTextView.setText(questionInput.get(0));
                        nextBtn.setEnabled(true);
                        editText.setEnabled(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                questionRef.addListenerForSingleValueEvent(eventListener);

            } catch (NullPointerException e) {

                Toaster.toast("Could not Communicate with the Server.");

            }

            return null;
        }


        @Override

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

            }
            catch (NullPointerException e){
                toast("Null pointer exception");
            }
        }
    }



 int count=1;
   // int answerIndex;
public void nextPress(View view){
    if(editText.getText().toString().trim().length()!=0){

        if(nextBtn.getText()=="Submit"){
            rootref.child("users").child(user.getUid()).child("Answers").child(questionInput.get(count-1)).setValue(editText.getText().toString());
            setContentView(R.layout.signed_in_layout);
        }

        if (count<questionCount){
            questionTextView.setText(questionInput.get(count));
            rootref.child("users").child(user.getUid()).child("Answers").child(questionInput.get(count-1)).setValue(editText.getText().toString());
            count++;
            editText.setText("");
            }
        else
        {nextBtn.setText("Submit");}

    }
    else{
        toast("Answer cannot be empty");
    }

}

}
