package com.example.axel.projectplanner;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;

                if(possibleEmail.contains("@smail.iitpkd.ac.in"))
                {
                    editor.putString("email", possibleEmail);
                    editor.commit();
                    break;
                }
                else if(possibleEmail.contains("@iitpkd.ac.in"))
                {
                    editor.putString("email", possibleEmail);
                    editor.commit();
                    break;
                }
            }
        }
        if(pref.getString("email",null)==null)
        {
            //Toast.makeText(this, "NO ACCOUNT FOUND!",
                    //Toast.LENGTH_LONG).show();
            AlertDialog diaBox = AskOption();
            diaBox.show();
        }
        else
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    //Do something after time
                    Intent intent = new Intent(login.this,MainActivity.class);
                    startActivity(intent);
                }
            }, 3000);
        }
    }
    @Override
    public void onBackPressed() {
        //AlertDialog diaBox = AskOption();
        //diaBox.show();
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("No Account Found")
                .setMessage("No iitpkd.ac.in account has been detected. \nPlease check if permission is granted (settings->applications-> projectplanner->permissions).")
                /*.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })*/
                .create();
        return myQuittingDialogBox;

    }
}
