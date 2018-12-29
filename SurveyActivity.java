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
import java.util.EventListener;
import java.util.List;

import butterknife.OnClick;
import xdroid.core.Global;
import xdroid.toaster.Toaster;

public class SurveyActivity extends AppCompatActivity {
    List<String> questionInput;
    List<String>memberinput;
    DatabaseReference rootref;
    TextView questionTextView;
    int memqCount;
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
                        int count=1,cn=1;
                        questionInput = new ArrayList<>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String question = ds.getValue().toString();
                            questionInput.add(count +" : "+question);
                            count++;
                            cn++;
                        }

                        questionTextView.setText(questionInput.get(0));
                        nextBtn.setEnabled(true);
                        editText.setEnabled(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                questionRef.addListenerForSingleValueEvent(eventListener);


//                DatabaseReference memberRef = rootref.child("members");
//                ValueEventListener eventListener2 = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        int count=1;
//                        memberinput = new ArrayList<>();
//
//                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                            String member = ds.getValue().toString();
//                           memberinput.add(count +" : "+member);
//                            count++;
//                        }
//                        memqCount = count-1;
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {}
//                };
//                memberRef.addListenerForSingleValueEvent(eventListener2);

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
public void nextPress(View view) {
    if (editText.getText().toString().trim().length() != 0) {

        rootref.child("users").child(user.getUid()).child("Answers").child(questionInput.get(count - 1)).setValue(editText.getText().toString());
        if (count != 5 && count != 6)
            questionTextView.setText(questionInput.get(count));
            count++;
            editText.setText("");

        if (count == 5) {
            nextBtn.setText("Submit");
            }
        if (count == 6)
        {setContentView(R.layout.signed_in_layout);
            finish();
         }
    }
         else {
        toast("Answer cannot be empty");
        }
}


//public int members() {
//
//
//    int n = Integer.parseInt(editText.getText().toString().trim());
//    return n;
//}

//
//    public  void memq(int n){
//    int count=1;
//     for (int i = 0; i < n; i++) {
//        if (editText.getText().toString().trim().length() != 0) {
//
////            if (nextBtn.getText() == "Submit") {
////                rootref.child("users").child(user.getUid()).child("Answers").child(questionInput.get(count - 1)).setValue(editText.getText().toString());
////                 setContentView(R.layout.signed_in_layout);
////            }
//
//            if (count < memqCount) {
//                questionTextView.setText(memberinput.get(count));
//                rootref.child("users").child(user.getUid()).child("Answers").child(memberinput.get(count - 1)).setValue(editText.getText().toString());
//                count++;
//                editText.setText("");
//            } else {
//                nextBtn.setText("Submit");
//            }
//
//        } else {
//            toast("Answer cannot be empty");
//        }
//    }
//
//}
}
