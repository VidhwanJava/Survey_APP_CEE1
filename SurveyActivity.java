package com.threefoolz.survey_revision2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import static xdroid.toaster.Toaster.toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.threefoolz.survey_revision2.R;


import java.util.ArrayList;

import java.util.List;


import xdroid.toaster.Toaster;

public class SurveyActivity extends AppCompatActivity {
    List<String> questionInput;
    List<String>memberinput;
    List<String>envinput;
    List<String>dssinput;
    DatabaseReference rootref,userRef;
    TextView questionTextView,head;
    RadioGroup rdo,rdo2;
    RadioButton val,btn1,btn2,in,out,both;
    getJsonFile asyncTask;
    Button nextBtn;
    EditText editText,editn;
    FirebaseUser user;
    CheckBox hptn,dm,hrt,kdny,cncr;
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
        in=findViewById(R.id.in);
        out=findViewById(R.id.out);
        both=findViewById(R.id.both);
        rdo2=findViewById(R.id.rdo2);
        hptn=findViewById(R.id.hptn);
        dm=findViewById(R.id.dm);
        hrt=findViewById(R.id.hrt);
        kdny=findViewById(R.id.kdny);
        cncr=findViewById(R.id.cncr);

       user=FirebaseAuth.getInstance().getCurrentUser();

        String phoneNumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        userRef =FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("collected_samples").push();
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("phonenumber").setValue(phoneNumber);

//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
//        ref.child("phonenumber").setValue(phoneNumber);

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


                DatabaseReference dssRef = rootref.child("disease");
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

 int count=1,nwcount=0,i=1,selectedId,a=1,loop=0,qst=0,patient=1;
String accept="yes",condition="qstn",data;
@SuppressLint("SetTextI18n")
public void nextPress(View view) {

    if (editText.getText().toString().trim().length() != 0 || editText.getText().toString().trim().equals("pass")) {
if(condition.equals("qstn")) {
    if (count == 7){
        condition = "mmbr";
        accept="no";
        count++;
    }
        if(count!=7 && accept.equals("yes"))
    userRef.child("Answers").child(questionInput.get(count - 1)).setValue(editText.getText().toString().trim());
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
        editText.setVisibility(View.GONE);
        editText.setText("dummy");          // to override null value check
        editn.setVisibility(View.VISIBLE);
        editn.requestFocus();
        count++;
    }

}

if(condition.equals("mmbr")){

    if(a==1) {
        editText.setText("pass");

        if (editn.length()==0)
            toast("Enter the number of members");
        else {
        loop = Integer.parseInt(editn.getText().toString().trim());
            a = 0;
            questionTextView.setText(memberinput.get(nwcount));
            editText.setVisibility(View.VISIBLE);
            editText.setText("");
            editn.setVisibility(View.GONE);
            head.setText("Member : " + i);
        }
    }
else {

        if (nwcount < 7) {

            userRef.child("Answers").child("Member : " + i).child(memberinput.get(nwcount)).setValue(editText.getText().toString().trim());
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
                editText.setVisibility(View.GONE);
                rdo.setVisibility(View.VISIBLE);
                questionTextView.setText(envinput.get(0));
                editText.setText("dummy");  //to override null value check
                condition="envsts";
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

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
        userRef.child("Answers").child("envstst").child(envinput.get(qst)).setValue(data);
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
        userRef.child("Answers").child("envstst").child(envinput.get(qst)).setValue(data);
        qst++;
        questionTextView.setText(envinput.get(qst));
        btn1.setText("Smokeless");
        btn2.setText("Smokey");
        both.setVisibility(View.VISIBLE);
        btn1.setChecked(false);
        btn2.setChecked(false);
        break;
    }
    case 2:{
        selectedId = rdo.getCheckedRadioButtonId();
        val = (RadioButton) findViewById(selectedId);
        data = val.getText().toString();
        userRef.child("Answers").child("envstst").child(envinput.get(qst)).setValue(data);
        qst++;
        questionTextView.setText(envinput.get(qst));
        rdo.setVisibility(View.GONE);
        rdo2.setVisibility(View.VISIBLE);
        btn1.setChecked(false);
        btn2.setChecked(false);
        both.setChecked(false);
        both.setVisibility(View.GONE);
        break;
    }
    case 3:{
        selectedId = rdo2.getCheckedRadioButtonId();
        val = (RadioButton) findViewById(selectedId);
        data = val.getText().toString();
        userRef.child("Answers").child("envstst").child(envinput.get(qst)).setValue(data);
        qst++;
        questionTextView.setText(envinput.get(qst));
        rdo2.setVisibility(View.GONE);
        rdo.setVisibility(View.VISIBLE);
        in.setVisibility(View.VISIBLE);
        out.setVisibility(View.VISIBLE);
        btn1.setText("Present");
        btn2.setText("Absent");
        btn1.setOnClickListener(v -> {
            btn1.setChecked(true);
            in.setEnabled(true);
            out.setEnabled(true);
        });
        btn2.setOnClickListener(y -> {
            btn2.setChecked(true);
            in.setEnabled(false);
            out.setEnabled(false);
        });
        break;
    }
    case 4:{
        selectedId = rdo.getCheckedRadioButtonId();
        val = (RadioButton) findViewById(selectedId);
        data = val.getText().toString();
        if(data.equals("Present"))
            toast("Select whether Inside or Outside");
        else {
            userRef.child("Answers").child("envstst").child(envinput.get(qst)).setValue(data);
            qst++;
            questionTextView.setText(envinput.get(qst));
            btn1.setText("Proper");
            btn2.setText("Improper");
            in.setVisibility(View.GONE);
            out.setVisibility(View.GONE);
            btn1.setChecked(false);
            btn2.setChecked(false);
        }
        break;
    }
    case 5:{
        selectedId = rdo.getCheckedRadioButtonId();
        val = (RadioButton) findViewById(selectedId);
        data = val.getText().toString();
        userRef.child("Answers").child("envstst").child(envinput.get(qst)).setValue(data);
        btn2.setVisibility(View.GONE);
        btn1.setVisibility(View.GONE);
        condition="dss";
        head.setText("Disease");
        questionTextView.setText(dssinput.get(0));
        editText.setVisibility(View.VISIBLE);
        editText.setText("");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(editText.getWindowToken(), 0,0);
        editText.requestFocus();
        qst=0;
    }
    }
    }
    else if(condition.equals("dss")){

        switch (qst) {
            case 0: {
                userRef.child("Answers").child("Diseases").child("Patient : "+patient).child(dssinput.get(qst)).setValue(editText.getText().toString().trim());
                qst++;
                questionTextView.setText(dssinput.get(qst));
                editText.setVisibility(View.GONE);
                hptn.setVisibility(View.VISIBLE);
                hrt.setVisibility(View.VISIBLE);
                dm.setVisibility(View.VISIBLE);
                kdny.setVisibility(View.VISIBLE);
                cncr.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInputFromWindow(editText.getWindowToken(), 0,0);
                break;
            }
            case 1:{
                String get="";
                if(hptn.isChecked())
                    get = "HPTN";
                if (dm.isChecked())
                    get = get+" "+"DM";
                if (hrt.isChecked())
                    get = get+" "+"Heart";
                if (kdny.isChecked())
                    get = get+" "+"Kidney";
                if (cncr.isChecked())
                    get = get+" Cancer";
                userRef.child("Answers").child("Diseases").child("Patient : "+patient).child(dssinput.get(qst)).setValue(get);
                qst++;
                questionTextView.setText(dssinput.get(qst));
                hptn.setVisibility(View.GONE);
                hrt.setVisibility(View.GONE);
                dm.setVisibility(View.GONE);
                kdny.setVisibility(View.GONE);
                cncr.setVisibility(View.GONE);
                hptn.setChecked(false);
                hrt.setChecked(false);
                cncr.setChecked(false);
                kdny.setChecked(false);
                dm.setChecked(false);
                editText.setVisibility(View.GONE);
                btn2.setVisibility(View.VISIBLE);
                btn1.setVisibility(View.VISIBLE);
                btn1.setChecked(false);
                btn2.setChecked(false);
                btn1.setText("Yes");
                btn2.setText("No");
                break;
            }
            case 2:{
                selectedId = rdo.getCheckedRadioButtonId();
                val = (RadioButton) findViewById(selectedId);
                data = val.getText().toString();
                userRef.child("Answers").child("Diseases").child("Patient : "+patient).child(dssinput.get(qst)).setValue(data);


                btn1.setText("Allopathie");
                btn2.setText("Alternative");
                btn1.setChecked(false);
                btn2.setChecked(false);
                both.setVisibility(View.VISIBLE);
                both.setChecked(false);
                qst++;
                questionTextView.setText(dssinput.get(qst));
                break;
            }
            case 3:{
                selectedId = rdo.getCheckedRadioButtonId();
                val = (RadioButton) findViewById(selectedId);
                data = val.getText().toString();
                userRef.child("Answers").child("Diseases").child("Patient : "+patient).child(dssinput.get(qst)).setValue(data);
                qst++;
                questionTextView.setText(dssinput.get(qst));
                btn1.setChecked(false);
                btn2.setChecked(false);
                both.setVisibility(View.GONE);
                both.setChecked(false);
                btn1.setText("Regular");
                btn2.setText("Irregular");
                break;
            }
            case 4:{
                selectedId = rdo.getCheckedRadioButtonId();
                val = (RadioButton) findViewById(selectedId);
                data = val.getText().toString();
                userRef.child("Answers").child("Diseases").child("Patient : "+patient).child(dssinput.get(qst)).setValue(data);
                btn1.setChecked(false);
                btn2.setChecked(false);
                qst++;
                questionTextView.setText("Any more members with diseased condition?");
                btn1.setText("Yes");
                btn2.setText("No");
                break;
            }
            case 5:{
                selectedId = rdo.getCheckedRadioButtonId();
                val = (RadioButton) findViewById(selectedId);
                data = val.getText().toString();
                if (data.equals("No")) {
                    toast("Thanks");
                    finish();
                    break;
                }
                    else if (data.equals("Yes")){
                    patient++;
                    qst=0;
                    btn1.setChecked(false);
                    btn2.setChecked(false);
                    questionTextView.setText(dssinput.get(0));
                    editText.setVisibility(View.VISIBLE);
                    editText.setText("");
                    btn2.setVisibility(View.GONE);
                    btn1.setVisibility(View.GONE);
                    break;
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
