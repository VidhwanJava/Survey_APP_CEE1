package com.crosscipher.survey_revision2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


import java.util.ArrayList;

import java.util.List;


import xdroid.toaster.Toaster;

public class SurveyActivity extends AppCompatActivity {
    List<String> questionInput;
    List<String>memberinput;
    List<String>envinput;
    List<String>dssinput;
    DatabaseReference rootref;
    TextView questionTextView;

    getJsonFile asyncTask;
    Button nextBtn;
    Button nwbtn;
    EditText editText;
    EditText editn;
    FirebaseUser user;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        rootref=FirebaseDatabase.getInstance().getReference();
            editText= findViewById(R.id.messageEdit);
        nextBtn=findViewById(R.id.next);
        editn=findViewById(R.id.editn);
        nwbtn=findViewById(R.id.nwbtn);


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
                        int count=1;
                        questionInput = new ArrayList<>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String question = ds.getValue().toString();
                            questionInput.add(count +" : "+question);
                            count++;
                        }

                        questionTextView.setText(questionInput.get(0));
                        nextBtn.setEnabled(true);
                        editText.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                questionRef.addListenerForSingleValueEvent(eventListener);


                DatabaseReference memberRef = rootref.child("members");
                ValueEventListener eventListener2 = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 1;
                        memberinput = new ArrayList<>();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String member = ds.getValue().toString();
                            memberinput.add(count + " : " + member);
                            count++;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                memberRef.addListenerForSingleValueEvent(eventListener2);



                DatabaseReference envRef = rootref.child("envsts");
                ValueEventListener eventListener3 = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count=1;
                        envinput = new ArrayList<>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String qstn = ds.getValue().toString();
                            envinput.add(count +" : "+qstn);
                            count++;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                envRef.addListenerForSingleValueEvent(eventListener3);


                DatabaseReference dssRef = rootref.child("members");
                ValueEventListener eventListener4 = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count=1;
                        dssinput = new ArrayList<>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String member = ds.getValue().toString();
                            dssinput.add(count +" : "+member);
                            count++;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                dssRef.addListenerForSingleValueEvent(eventListener4);

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

    int i=1;
String condition="qstn";
 int count=1,nwcount=0;
int a=1;int loop=0,flag=0;
String accept="yes";
@SuppressLint("SetTextI18n")
public void nextPress(View view) {
    if (editText.getText().toString().trim().length() != 0) {
if(condition == "qstn") {
    if (count == 7){
        condition = "mmbr";
        accept="no";
        count++;

    }
        if(count!=7 && accept=="yes")
    rootref.child("users").child(user.getUid()).child("Answers").child(questionInput.get(count - 1)).setValue(editText.getText().toString().trim());

        if (count != 5 && count != 6 && count != 7 && accept=="yes")
        questionTextView.setText(questionInput.get(count));
       if(count!=7 && accept=="yes") {
           count++;
           editText.setText("");
       }

    if (count == 5)
        nextBtn.setText("Submit");

    if (count == 6) {
        questionTextView.setText("Enter no. of family members");
        editText.setVisibility(View.INVISIBLE);
        editText.setText("dummy");          // to override null value check
        editn.setVisibility(View.VISIBLE);
        count++;
//            finish();

    }

}


if(condition=="mmbr"){

    if(a==1) {
        editText.setText("");
        loop=Integer.parseInt(editn.getText().toString().trim());
        a=0;
        questionTextView.setText(memberinput.get(nwcount));
        editText.setVisibility(View.VISIBLE);
        editn.setVisibility(View.INVISIBLE);
    }
else {
//        TODO: This for loop should iterate "loop" times.
//        Each time all values of each member should be accepted.
//        for loop should be within the pushing statement. nc=0; i=1 ;mmbrip=0;


        if (nwcount < 7) {

            rootref.child("users").child(user.getUid()).child("Answers").child("Member : " + i).child(memberinput.get(nwcount)).setValue(editText.getText().toString().trim());
            nwcount++;
            if(nwcount!=7)
            questionTextView.setText(memberinput.get(nwcount));
            editText.setText("");
            if (nwcount == 7) {
                i++;
                nwcount = 0;
                questionTextView.setText(memberinput.get(nwcount));
            }
            if (i == loop + 1) {
               finish();
            }
        }
        }
    }
}
         else {
        toast("Answer cannot be empty");
        }


}


}
