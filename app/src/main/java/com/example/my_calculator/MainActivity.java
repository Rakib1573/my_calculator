package com.example.my_calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtScreen = (TextView) findViewById(R.id.result);
        setNumericOnClickListener();
        setOperatorOnClickListener();


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerid);
        NavigationView navigationView =(NavigationView) findViewById(R.id.navid);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.homeid){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.horrorid){
            Intent intent = new Intent(this,Scientific.class);
            startActivity(intent);
        }
        return false;
    }

    private int[] numberButtons={R.id.button0,R.id.button1,R.id.button2,R.id.button3,R.id.button4,
            R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9};

    private int[] operationButtons={R.id.buttonAdd,R.id.buttonSub,R.id.buttonMul,R.id.buttonDiv};
    private TextView txtScreen;
    private boolean lastNum;
    private boolean stateError;




    private void setNumericOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if (stateError) {
                    txtScreen.setText(button.getText());
                    stateError = false;
                } else {
                    txtScreen.append(button.getText());
                }
                lastNum = true;
            }
        };
        for (int id : numberButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }


    private void setOperatorOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNum && !stateError) {
                    Button button = (Button) v;
                    txtScreen.append(button.getText());
                    lastNum= false;
                }
            }
        };
        for (int id : operationButtons) {
            findViewById(id).setOnClickListener(listener);
        }
        findViewById(R.id.buttonC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtScreen.setText("");
                lastNum = false;
                stateError = false;
            }
        });
        findViewById(R.id.buttonEQUAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });
    }


    private void onEqual() {
        if (lastNum && !stateError) {
            String txt = txtScreen.getText().toString();
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                double result = expression.evaluate();
                txtScreen.setText(Double.toString(result));
            } catch (ArithmeticException ex) {
                // Display an error message
                txtScreen.setText("Error");
                stateError = true;
                lastNum = false;
            }
        }
    }


}
