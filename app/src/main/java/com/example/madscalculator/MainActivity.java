package com.example.madscalculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView expression_text;
    TextView result_text;

    boolean valid_division = true;
    boolean valid_expression = true;
    String input = "0";
    Float fontSize = 36f;

    FirebaseDatabase firebaseDatabase;
    ArrayList<String> value = new ArrayList<>(10);

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("users");


        expression_text = findViewById(R.id.expression);
        result_text = findViewById(R.id.result);
        expression_text.setText(input);

    }

    public void Answer(View view) {

        input = result_text.getText().toString();

        if(input!="") {
            expression_text.setText(input);

            if (value.size() == 10) {
                value.remove(0);
            }
            value.add(input);

            if (!value.isEmpty())
                databaseReference.setValue(value);
        }

    }



    public void result() {

        ArrayList<String> number = convertStringToList(input);


        while (number.contains("*") && number.indexOf("*") != number.size() - 1) {

            double num1 = Double.parseDouble(number.get(number.indexOf("*") - 1));
            double num2 = Double.parseDouble(number.get(number.indexOf("*") + 1));

            double result = num1 * num2;

            number.set(number.indexOf("*") - 1, Double.toString(result));
            number.remove(number.indexOf("*") + 1);
            number.remove("*");
        }

        while (number.contains("+") && number.indexOf("+") != number.size() - 1) {
            double num1 = Double.parseDouble(number.get(number.indexOf("+") - 1));
            double num2 = Double.parseDouble(number.get(number.indexOf("+") + 1));
            double result = num1 + num2;
            number.set(number.indexOf("+") - 1, Double.toString(result));
            number.remove(number.indexOf("+") + 1);
            number.remove("+");
        }

        while (number.contains("/") && number.indexOf("/") != number.size() - 1) {
            double num1 = Double.parseDouble(number.get(number.indexOf("/") - 1));
            double num2 = Double.parseDouble(number.get(number.indexOf("/") + 1));
            double result = 0;
            if (num2 != 0) {
                result = num1 / num2;
                number.set(number.indexOf("/") - 1, Double.toString(result));
            } else {
                valid_division = false;
                result_text.setText(R.string.divideZero);
                break;
            }

            number.remove(number.indexOf("/") + 1);
            number.remove("/");

        }

        while (number.contains("-") && number.indexOf("-") != number.size() - 1) {
            double num1 = Double.parseDouble(number.get(number.indexOf("-") - 1));
            double num2 = Double.parseDouble(number.get(number.indexOf("-") + 1));
            double result = num1 - num2;
            number.set(number.indexOf("-") - 1, Double.toString(result));
            number.remove(number.indexOf("-") + 1);
            number.remove("-");

        }

        if (valid_division) {
            String output = number.get(0);

            result_text.setText(output);
        }
        valid_division = true;

        input = result_text.getText().toString();
    }


    public static ArrayList<String> convertStringToList(String s){

        ArrayList<String> list = new ArrayList<>();
        String result = "";
        for(int i=0;i<s.length();i++){
            while (Character.isDigit(s.charAt(i)) || s.charAt(i)=='.'){
                result = result+s.charAt(i);
                if(i<s.length()-1)
                    i++;
                else
                    break;
            }

            list.add(result);
            result = "";

            if(i<s.length()-1){
                result = result+s.charAt(i);
                list.add(result);}

            result = "";


        }

        return list;

    }

    public void history(View view) {
        Intent intent = new Intent(this,History.class);
        startActivity(intent);
    }

    public void decimal(View view) {
        if(!valid_expression)
            invalid_expression();
        create_expression(".");

        valid_expression = false;
    }

    public void AllClear(View view) {
        input = "0";
        expression_text.setText(input);
        result_text.setText(" ");
        valid_expression=true;
    }



    public void Back(View view) {

        double aDouble = Double.parseDouble(expression_text.getText().toString());

        if (aDouble == 0) {
            expression_text.setText("0");
            result_text.setText("");
            input = "0";
        }

        else if(input.length()>=1){
            input=input.substring(0,input.length()-1);
             expression_text.setText(input);
             result();}
        valid_expression = true;
    }
    public void Divide(View view) {

        if(!valid_expression)
            invalid_expression();

        create_expression("/");

        result();
        valid_expression = false;

    }
    public void multiplication(View view) {

        if(!valid_expression)
            invalid_expression();

        create_expression("*");
        valid_expression = false;
    }
    public void addition(View view) {

        if(!valid_expression)
            invalid_expression();
        create_expression("+");
        valid_expression = false;
    }
    public void subtraction(View view) {

        if(!valid_expression)
            invalid_expression();
        create_expression("-");
        valid_expression = false;
    }

    private void create_expression(String s){
        input = input + s;
        expression_text.setText(input);
    }
    private void invalid_expression(){
        input = input.substring(0,input.length()-1);
    }

    public void zero(View view) {
        create_expression("0");
        result();
        valid_expression = true;
    }
    public void one(View view) {

        if(input.length()==1 && input=="0")
        input="";
        create_expression("1");

        result();
        valid_expression = true;
    }
    public void two(View view) {

        if(input.length()==1 && input=="0")
            input="";
        create_expression("2");
         result();
        valid_expression = true;
    }
    public void three(View view) {
        if(input.length()==1 && input=="0")
            input="";
        create_expression("3");
        result();
        valid_expression = true;
    }
    public void four(View view) {
        if(input.length()==1 && input=="0")
            input="";
        create_expression("4");
        result();
        valid_expression = true;
    }
    public void five(View view) {
        if(input.length()==1 && input=="0")
            input="";
        create_expression("5");
        result();
        valid_expression = true;
    }
    public void six(View view) {
        if(input.length()==1 && input=="0")
            input="";
        create_expression("6");
        result();
        valid_expression = true;
    }
    public void seven(View view) {
        if(input.length()==1 && input=="0")
            input="";
        create_expression("7");
        result();
        valid_expression = true;
    }
    public void eight(View view) {
        if(input.length()==1 && input=="0")
            input="";
        create_expression("8");
        result();
        valid_expression = true;
    }
    public void nine(View view) {
        if(input.length()==1 && input=="0")
            input="";
        create_expression("9");
        result();
        valid_expression = true;
    }


}









