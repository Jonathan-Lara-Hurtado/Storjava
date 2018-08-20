package mx.com.laracorp.storj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    Intent register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // logo = (ImageView)findViewById(R.id.logo);

        //logo.setImageResource(R.drawable.storjlogoblanco);


    }


    public void btnRegister(View v) {

        register = new Intent(getApplicationContext(), Main2Activity.class);
        startActivityForResult(register, 0);

    }


    public void btnLogin(View v) {


    }


}
