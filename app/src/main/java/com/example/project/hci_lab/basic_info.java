package com.example.project.hci_lab;

import android.Manifest;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.HashMap;
import java.util.Map;


public class basic_info extends AppCompatActivity implements NumberPicker.OnValueChangeListener,AdapterView.OnItemSelectedListener{

    private ImageButton ben_image;
    final int permission_all=11;
    String permissions[]={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    public static final int MY_PERMISSION_CAMERA=100;
    ImageButton btn_save_proceed,btn_home;
    public static final int qr_request_code=100;
    public static final int qr_result_code=200;
    private  LinearLayout linearLayout;
    EditText editText_name,editText_age,editText_Hamlet,editText_ward,editText_male_number,
            editText_female_number,editText_transgender_number,
            editText_beneficiary_code,editText_gp_vc_name, editText_district,editText_subdivision,editText_block;


    Spinner spinner_caste,spinner_religion,spinner_acquired_house,spinner_house_type,spinner_secc,spinner_secc_type;




    RadioGroup rg_gender,rg_gp_vc_type;
    RadioButton radioButton_gender,radioButton_gp_vc_type;
    Button qr_scan;

    Integer empty_checker_counter =0;
    String empty_checker_message="";



    // Creating Volley RequestQueue.
    RequestQueue requestQueue;

    // Creating Progress dialog.
    ProgressDialog progressDialog;


    String Name_Holder,Age_Holder,Hamlet_Holder,Ward_Holder,Male_number_Holder,Female_number_Holder,Transgender_number_Holder,
            Caste_Holder,Religion_Holder,acquired_house_Holder,house_type_Holder,secc_Holder,secc_type_Holder,gender_Holder="Male",
    District_Holder,Subdivision_Holder,Block_Holder,GP_Holder,GP_VC_Type_Holder,bi_Surveyor_Holder,house_holde_code_Holder,bi_server_response;




    // Storing server url into String variable.
    String HttpUrl = "http://nakshakantha.com/apis/basic_info_insert.php";
    // String HttpUrl1 = "http://www.sebaloy.in/apis/insert_image.php";

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(basic_info.this, QR_ScanActivity.class);
                    startActivityForResult(intent, qr_request_code);
                } else {
                    Toast.makeText(this,"Please grant the permission!",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

public boolean has_Permission(Context context, String permissions[]){
        int  currentAPIVersion= Build.VERSION.SDK_INT;
        if(currentAPIVersion>=Build.VERSION_CODES.M){
            for(String permission:permissions){
                if(ContextCompat.checkSelfPermission( context,permission)!=PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
}

public void intialization_tools()
{   ben_image=findViewById( R.id.ben_image );
    Drawable drawable=getResources().getDrawable( R.drawable.ben_image );
    ben_image.setBackground( drawable );
    qr_scan=findViewById( R.id.btn_qr_code );
    linearLayout=findViewById( R.id.linear_scroll );
    btn_save_proceed = (ImageButton) findViewById( R.id.save_proceed );
    btn_home = (ImageButton) findViewById( R.id.btn_home );

    progressDialog = new ProgressDialog(this  );

    rg_gender = (RadioGroup) findViewById(R.id.radio_gender);
    rg_gp_vc_type = (RadioGroup) findViewById(R.id.radio_gp_vc_type);

    spinner_caste = (Spinner) findViewById( R.id.spinner_caste );
    spinner_religion = (Spinner) findViewById( R.id.spinner_religion );
    spinner_acquired_house = (Spinner) findViewById( R.id.spinner_acquired_house);
    spinner_house_type = (Spinner) findViewById( R.id.spinner_house_type );
    spinner_secc = (Spinner) findViewById( R.id.spinner_secc );
    spinner_secc_type = (Spinner) findViewById( R.id.spinner_secc_type );

    spinner_caste.setOnItemSelectedListener(this );

    spinner_religion.setOnItemSelectedListener(this);
    spinner_acquired_house.setOnItemSelectedListener(this);
    spinner_house_type.setOnItemSelectedListener(this);
    spinner_secc.setOnItemSelectedListener(this);
    spinner_secc_type.setOnItemSelectedListener(this);


    editText_name = (EditText) findViewById(R.id.editText_name);
    editText_age= (EditText) findViewById(R.id.editText_age);
    editText_Hamlet = (EditText) findViewById(R.id.editText_Hamlet);
    editText_ward = (EditText) findViewById(R.id.editText_ward);
    editText_male_number = (EditText) findViewById(R.id.editText_male_number);
    editText_female_number = (EditText) findViewById(R.id.editText_female_number);
    editText_transgender_number = (EditText) findViewById(R.id.editText_transgender_number);
    editText_beneficiary_code= (EditText) findViewById(R.id.editText_beneficiary_code);

    editText_district= (EditText) findViewById(R.id.editText_district);
    editText_subdivision= (EditText) findViewById(R.id.editText_subdivision);
    editText_block=(EditText) findViewById(R.id.editText_block);
    editText_gp_vc_name= (EditText) findViewById(R.id.editText_gp_vc_name);

}

    public void GetValueFromEditText(){
        // Toast.makeText(Registration.this, "Start button....", Toast.LENGTH_LONG).show();
        Name_Holder = editText_name.getText().toString().trim();
        Age_Holder= editText_age.getText().toString().trim();
        Hamlet_Holder = editText_Hamlet.getText().toString().trim();
        Ward_Holder = editText_ward.getText().toString().trim();
        Male_number_Holder = editText_male_number.getText().toString().trim();
        Female_number_Holder= editText_female_number.getText().toString().trim();
        Transgender_number_Holder = editText_transgender_number.getText().toString().trim();
        house_holde_code_Holder=editText_beneficiary_code.getText().toString().trim();

        District_Holder=editText_district.getText().toString().trim();
        Subdivision_Holder=editText_subdivision.getText().toString().trim();
        Block_Holder=editText_block.getText().toString().trim();
        GP_Holder=editText_gp_vc_name.getText().toString().trim();
       // GP_VC_Type_Holder="test gp vc";

      //  Toast.makeText(basic_info.this, "Start button....", Toast.LENGTH_LONG).show();
//        GP_Holder=editText_gp_vc_name.getText().toString().trim();
        // organizationHolder = ((RadioButton) findViewById(radioGrouporganization.getCheckedRadioButtonId())).getText().toString();
        // Toast.makeText(Registration.this, "End button...."+NameHolder+"..."+Father_NameHolder, Toast.LENGTH_LONG).show();

    }



    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

       Caste_Holder= spinner_caste.getSelectedItem().toString();
       Religion_Holder=spinner_religion.getSelectedItem().toString();
       acquired_house_Holder=spinner_acquired_house.getSelectedItem().toString();
       house_type_Holder=spinner_house_type.getSelectedItem().toString();
       secc_Holder= spinner_secc.getSelectedItem().toString();
       secc_type_Holder= spinner_secc_type.getSelectedItem().toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_basic_info );

        intialization_tools();

        bi_server_response=getIntent().getStringExtra("cm_server_response");

        // $send_self_image."#".$send_name."#".$send_code."#".$send_district."#".$send_subdivision."#".$send_block."#".$send_gp_vc_name."#".$send_gp_vc_type;
        String[] parts = bi_server_response.split("\\#");



        editText_district.setText(parts[3]);

        editText_subdivision.setText(parts[4]);
        editText_block.setText(parts[5]);
        editText_gp_vc_name.setText(parts[6]);
        RadioButton b1 = (RadioButton) findViewById(R.id.radio_gp);
        RadioButton b2 = (RadioButton) findViewById(R.id.radio_vc);
        b2.setClickable( false );
        b1.setClickable( false );
        Toast.makeText(getBaseContext(), parts[7].trim(), Toast.LENGTH_SHORT).show();
        if((parts[7].trim()).equals( "GP" ))


        {
           // GP_VC_Type_Holder=radioButton_gp_vc_type.getText().toString();
                                                //   Toast.makeText(getBaseContext(), "GP==="+parts[7].trim(), Toast.LENGTH_SHORT).show();
            b1.setChecked(true);
           b2.setChecked( false );
        }
        else
        {
            //GP_VC_Type_Holder=radioButton_gp_vc_type.getText().toString();
           // Toast.makeText(getBaseContext(),"VC===="+ parts[7].trim(), Toast.LENGTH_SHORT).show();
            b2.setChecked(true);
           b1.setChecked( false );
        }


       // cm_tv_surveyor_gp_vc_type_holder=parts[7];
        GP_VC_Type_Holder=parts[7].toString().trim();

     qr_scan.setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(basic_info.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSION_CAMERA);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }else {
                // Permission has already been granted
                // Log.i( "qrscan============","log++++++++++++++++++++++++++++++++++++"  );
                Intent intent=new Intent(basic_info.this,QR_ScanActivity.class );
                startActivityForResult( intent,qr_request_code);
            }

        }
    } );

        bi_Surveyor_Holder=parts[2];







        // final String value = ((RadioButton)findViewById(rg_gender.getCheckedRadioButtonId())).getText().toString();
       // Toast.makeText(getBaseContext(), value, Toast.LENGTH_SHORT).show();


        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(RadioGroup group, int checkedId)
                                                  {
                                                      radioButton_gender = (RadioButton) findViewById(checkedId);
                                                      gender_Holder=radioButton_gender.getText().toString();
                                                     // Toast.makeText(getBaseContext(), gender_Holder, Toast.LENGTH_SHORT).show();
                                                  }
                                              }
        );

//        rg_gp_vc_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                                                 @Override
//                                                 public void onCheckedChanged(RadioGroup group, int checkedId)
//                                                 {
//                                                     radioButton_gp_vc_type = (RadioButton) findViewById(checkedId);
//                                                     GP_VC_Type_Holder=radioButton_gp_vc_type.getText().toString();
//                                                     Toast.makeText(getBaseContext(), GP_VC_Type_Holder, Toast.LENGTH_SHORT).show();
//                                                 }
//                                             }
//        );
//
//

//        ben_image.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //sec_4 fragment=new sec_4();
//                //getSupportFragmentManager().beginTransaction().replace( R.id.fragment_benimage_container,fragment,"null" ).addToBackStack( "null" ).commit();
//                startActivity( new Intent( basic_info.this,Registration.class ) );
//            }
//        } );

        ben_image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(has_Permission(getApplicationContext(),permissions))
                {   FragmentManager fragmentManager=getFragmentManager();
                My_dialog_fragment dialog=new My_dialog_fragment();
                dialog.show(fragmentManager,"MyDialog");
                }else{
                    Toast.makeText( getApplicationContext(),"Please provide all the applications!",Toast.LENGTH_SHORT ).show();
                    ActivityCompat.requestPermissions( basic_info.this,permissions,permission_all );
                }
            }
        } );



        btn_home.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(basic_info.this,circle_menu.class);
                i.putExtra("server_response", getIntent().getStringExtra("cm_server_response"));
                startActivity(i);
            }});


        //FloatingActionButton btn_save_proceed = (FloatingActionButton) findViewById( R.id.save_proceed );
        btn_save_proceed.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetValueFromEditText();


                if(empty_checker()==true) {

                    progressDialog.setMessage( "Please Wait, We are Inserting Your Data on Server" );
                    progressDialog.show();



                  //  Toast.makeText(getBaseContext(), "success 11.....", Toast.LENGTH_SHORT).show();

                 /*   String s=Name_Holder+".."+Age_Holder+".."+Hamlet_Holder+".."+Ward_Holder+".."+Male_number_Holder+".."+Female_number_Holder+".."
                            +Transgender_number_Holder+".."+Caste_Holder+".."+Religion_Holder+".."+acquired_house_Holder+".."+house_type_Holder+".."+
                            secc_Holder+".."+secc_type_Holder+".."+gender_Holder+".."+District_Holder+".."+Subdivision_Holder+".."+Block_Holder+".."+GP_Holder+".."+GP_VC_Type_Holder
                            +bi_Surveyor_Holder+".."+house_holde_code_Holder+"..";


                   Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();

                   */
                    My_dialog_fragment my_dialog_fragment=new My_dialog_fragment();
                    Drawable drawable=ben_image.getDrawable();
                    Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
                    my_dialog_fragment.uploadBitmap(bitmap,getApplicationContext(),house_holde_code_Holder);

                    StringRequest stringRequest = new StringRequest( Request.Method.POST, HttpUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {

                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();
                                   // Toast.makeText(getBaseContext(), "success 10.....", Toast.LENGTH_SHORT).show();

                                    // Showing response message coming from server.
                                    Toast.makeText( basic_info.this, ServerResponse, Toast.LENGTH_LONG ).show();
                                    reseter();

                                    Intent i = new Intent(basic_info.this,desc_family_2.class);
                                    //i.putExtra("server_response", ServerResponse.trim());
                                    startActivity(i);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();

                                    // Showing error message if something goes wrong.
                                    Toast.makeText( basic_info.this, volleyError.toString(), Toast.LENGTH_LONG ).show();

                                }
                            } ) {
                        @Override
                        protected Map<String, String> getParams() {

                            // Creating Map String Params.
                            Map<String, String> params = new HashMap<String, String>();

                            // Adding All values to Params.
                            params.put( "head_name_sender", Name_Holder );
                            params.put( "age_sender", Age_Holder );
                            params.put( "hamlet_sender", Hamlet_Holder );
                            params.put( "ward_sender", Ward_Holder );
                            params.put( "male_sender", Male_number_Holder );
                            params.put( "female_sender", Female_number_Holder );
                            params.put( "transgender_sender", Transgender_number_Holder );
                            params.put( "caste_sender", Caste_Holder );
                            params.put( "religion_sender", Religion_Holder );
                            params.put( "ownership_of_house_sender", acquired_house_Holder );
                            params.put( "house_type_sender", house_type_Holder );
                            params.put( "secc_sender", secc_Holder );
                            params.put( "secc_type_sender", secc_type_Holder );
                            params.put( "gender_sender", gender_Holder );


                            params.put( "district_sender", District_Holder );
                            params.put( "subdivision_sender", Subdivision_Holder );
                            params.put( "block_sender", Block_Holder );
                            params.put( "gp_sender", GP_Holder );
                            params.put( "gp_vc_type_sender", GP_VC_Type_Holder );
                            params.put( "surveyor_sender", bi_Surveyor_Holder );
                            params.put( "house_holde_code_sender",house_holde_code_Holder );


                            // String uploadImage = getStringImage();
                            //  params.put("image",uploadImage );
                            return params;
                        }

                    };

                    // Creating RequestQueue.
                    RequestQueue requestQueue = Volley.newRequestQueue( basic_info.this );

                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add( stringRequest );

                }

            }
        } );




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("called","onActivityResult========");
        if(requestCode==qr_request_code && resultCode==RESULT_OK){
            if(data!=null){
                final Barcode barcode=data.getParcelableExtra( "barcode" );
                editText_beneficiary_code.post( new Runnable() {
                    @Override
                    public void run() {
                        editText_beneficiary_code.setText( barcode.displayValue );
                    }
                } );

            }

        }
    }


    public boolean empty_checker()
    {
        empty_checker_message="";
        empty_checker_counter=0;

//        if(NameHolder.equals(""))
//        {
//            empty_checker_message=empty_checker_message+"1. Please put your Name\n";
//
//        }else
//        {
//            empty_checker_counter=empty_checker_counter +1;
//        }
//        if(Father_NameHolder.equals(""))
//        {
//            empty_checker_message=empty_checker_message+"2. Please put your Name\n";
//
//        }else
//        {
//            empty_checker_counter=empty_checker_counter +1;
//        }
//
//        if(snipper_QualificationHolder.equals("Choose Qualification"))
//        {
//            empty_checker_message=empty_checker_message+"3. Please select the Qualification\n";
//
//        }else
//        {
//            empty_checker_counter=empty_checker_counter +1;
//        }
//        if(snipper_BloodHolder.equals("Choose Blood Group"))
//        {
//            empty_checker_message=empty_checker_message+"4. Please select the Blood Group\n";
//
//        }else{
//            empty_checker_counter=empty_checker_counter +1;
//        }
//
//


        if(empty_checker_counter==0)
        {
            return true;
        }
        else
        {
            Toast.makeText(basic_info.this, empty_checker_message, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void reseter()
    {
//        Name.getText().clear();
////        father_name.getText().clear();
////        mother_name.getText().clear();
////        address.getText().clear();
////        pin.setText("");
////        Phone.setText("");
////        emergency_phone.setText("");
////        email.setText("");
////        aadhaar.setText("");
////        remark.setText("");
    }





    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {


        editText_age.setText(String.valueOf(numberPicker.getValue()) );
    }

    public void showNumberPicker(View view) {

        age_NumberPickerDialog newFragment = new age_NumberPickerDialog();
        newFragment.setValueChangeListener( this );
        newFragment.show( getFragmentManager(), "time picker" );


    }
//    public void qr_codeMethod(View view){
//    Log.i("tag===========","qr_codeMethod");
//        Intent intent = new Intent( basic_info.this,QR_ScanActivity.class);
//       // startActivity( intent );
//        startActivityForResult( intent, qr_request_code );
//    }

}
