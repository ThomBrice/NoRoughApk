package com.example.isen.noroughapk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.isen.noroughapk.json_helper.JsonReader;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.app.Activity.RESULT_OK;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Thaddée Klein on 30/11/2016.
 */

public class AccountFragment extends Fragment {

    private ClickListenerFragment listenerFragment;

    public View view;

    EditText firstName;
    EditText surName;
    ImageView accountPicture;
    EditText handicap;
    TextView golfNameView;

    String golfName;


    public AccountFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        listenerFragment = (NavigationDrawer) this.getActivity();
        onResume();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();



        firstName = (EditText) view.findViewById(R.id.firstname);
        surName = (EditText) view.findViewById(R.id.surname);
        handicap = (EditText) view.findViewById(R.id.handicap);
        accountPicture = (ImageView) view.findViewById(R.id.account_picture);
        golfNameView = (TextView) view.findViewById(R.id.golfName);


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String TextfirstName = sharedPref.getString("FirstName", "");
        String TextSurname = sharedPref.getString("Surname", "");
        Float TextHandicap = sharedPref.getFloat("Handicap", 54.0f);
        String PictureBase64 = sharedPref.getString("AccountPicture", "null");
        golfName = sharedPref.getString("NomGolf","null");


        if (PictureBase64.equals("null")) {
            accountPicture.setImageResource(R.drawable.golf_player);
        } else {
            Bitmap profil = decodeToBase64(PictureBase64);
            accountPicture.setImageBitmap(Bitmap.createScaledBitmap(profil, profil.getWidth() / 10, profil.getHeight() / 10, false));
        }



        if(golfName.equals("null")){}
        else{
            golfNameView.setText(golfName);
        }

        firstName.setText(TextfirstName);
        surName.setText(TextSurname);
        handicap.setText(TextHandicap.toString());

        golfNameView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                listenerFragment.ClickListener("goToGetGolf");

            }
        });

        accountPicture.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dispatchTakePictureIntent();

            }
        });

        handicap.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(Float.parseFloat(handicap.getText().toString())>54 ){
                    Toast.makeText(getActivity(), "Handicap maximum : 54", Toast.LENGTH_SHORT).show();
                    handicap.setText("54.0");
                }
                if(Float.parseFloat(handicap.getText().toString())<0 ){
                    Toast.makeText(getActivity(), "Handicap minimum : 0", Toast.LENGTH_SHORT).show();
                    handicap.setText("0.0");
                }
            }
        });




    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    accountPicture.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10, false));
                    String PictureBase64 = encodeToBase64(bitmap);
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("AccountPicture", PictureBase64);
                    editor.commit();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public void onPause() {
        super.onPause();


        String FirstName = firstName.getText().toString();
        String Surname = surName.getText().toString();
        Float Handicap = Float.parseFloat(handicap.getText().toString());


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("FirstName", FirstName);
        editor.putString("Surname", Surname);
        editor.putFloat("Handicap", Handicap);
        editor.commit();


    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }
}




