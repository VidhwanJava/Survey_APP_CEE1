package com.crosscipher.survey_revision2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView questionTextView,head;
    RadioGroup rdo;
    RadioButton val,btn1,btn2;
    getJsonFile asyncTask;
    Button nextBtn;
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
        head = findViewById(R.id.head);
        rdo=findViewById(R.id.rdo);
        btn1=findViewById(R.id.ad);
        btn2=findViewById(R.id.inad);


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
                    @SuppressLint("SetTextI18n")
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
                        head.setText("General Information");
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

 int count=1,nwcount=0,i=1,selectedId,a=1,loop=0,qst=0;
String accept="yes",condition="qstn",data;
@SuppressLint("SetTextI18n")
public void nextPress(View view) {

    if (editText.getText().toString().trim().length() != 0) {
if(condition.equals("qstn")) {
    if (count == 7){
        condition = "mmbr";
        accept="no";
        count++;

    }
        if(count!=7 && accept.equals("yes"))
    rootref.child("users").child(user.getUid()).child("Answers").child(questionInput.get(count - 1)).setValue(editText.getText().toString().trim());

        if (count != 5 && count != 6 && count != 7 && accept.equals("yes"))
        questionTextView.setText(questionInput.get(count));
       if(count!=7 && accept.equals("yes")) {
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

if(condition.equals("mmbr")){


    if(a==1) {
        editText.setText("");
        loop=Integer.parseInt(editn.getText().toString().trim());
        a=0;
        questionTextView.setText(memberinput.get(nwcount));
        editText.setVisibility(View.VISIBLE);
        editn.setVisibility(View.INVISIBLE);
        head.setText("Member : "+i);

    }
else {
//        if(count==10)
//            condition="envsts";

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
                head.setText("Member : "+i);
            }
            if (i == loop + 1) {
                head.setText("Environmental Sanitation");
                editText.setVisibility(View.INVISIBLE);
                rdo.setVisibility(View.VISIBLE);
                questionTextView.setText(envinput.get(0));
//               finish();
                editText.setText("dummy");  //to override null value check
//                count=10;
                condition="envsts";
                i=0;
            }
        }
        }
    }


   else if(condition.equals("envsts")){

switch (qst) {
    case 0:{
        selectedId = rdo.getCheckedRadioButtonId();
        val = (RadioButton) findViewById(selectedId);
        data = val.getText().toString();
        rootref.child("users").child(user.getUid()).child("Answers").child("envstst").child(envinput.get(qst)).setValue(data);
        qst++;
        questionTextView.setText(envinput.get(qst));
        btn1.setChecked(false);
        btn2.setChecked(false);
        break;

    }
    case 1:{
        selectedId = rdo.getCheckedRadioButtonId();
        val = (RadioButton) findViewById(selectedId);
        data = val.getText().toString();
        rootref.child("users").child(user.getUid()).child("Answers").child("envstst").child(envinput.get(qst)).setValue(data);
        qst++;
        questionTextView.setText(envinput.get(qst));
        btn1.setText("Somkeless");
        btn2.setText("Smokey");
        btn1.setChecked(false);
        btn2.setChecked(false);
        break;
    }
    case 2:{
        selectedId = rdo.getCheckedRadioButtonId();
        val = (RadioButton) findViewById(selectedId);
        data = val.getText().toString();
        rootref.child("users").child(user.getUid()).child("Answers").child("envstst").child(envinput.get(qst)).setValue(data);
        qst++;
        questionTextView.setText(envinput.get(qst));
        // here create a new radio group and make it visible, and the older one invisibe;

        break;
    }

}
    }
}
         else {
        toast("Answer cannot be empty");
        }

}


}
