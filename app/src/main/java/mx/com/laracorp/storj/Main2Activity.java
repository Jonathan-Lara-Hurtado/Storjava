package mx.com.laracorp.storj;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

public class Main2Activity extends AppCompatActivity {


    EditText email;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main2);



    }



    public void btnsign(View v) throws NoSuchAlgorithmException {

        Register register= new Register();

        Alertastorj alertastorj = new Alertastorj();
        email = (EditText) findViewById(R.id.txtemail);
        password =(EditText)findViewById(R.id.txtpassword);

        if(email.getText().toString().isEmpty()){

            alertastorj.setMensaje(getString(R.string.email_alert));
            alertastorj.show(getFragmentManager(),null);


        }else {



            if (password.getText().toString().isEmpty()) {

                alertastorj.setMensaje(getString(R.string.white_password));
                alertastorj.show(getFragmentManager(),null);


            }else{

                register.execute();

            }


        }





    }






    public class Register extends AsyncTask<String,String,String> {

        private String candena;
        private StringBuilder total;
        private String resultadofinal;
        String emailcadena;
        String passcadena;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            email = (EditText) findViewById(R.id.txtemail);
            password =(EditText)findViewById(R.id.txtpassword);
            emailcadena  = email.getText().toString();
            Sha sha = new Sha();

            try {

                passcadena = sha.hash256(password.getText().toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://api.storj.io/users");
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
//  urlConnection.setChunkedStreamingMode(0);


                Log.d("passwordss",passcadena);
                JSONObject jsonParam = new JSONObject();

                jsonParam.put("email",emailcadena);
                jsonParam.put("password",passcadena);

                DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                os.close();

                Log.i("ResponseCode",String.valueOf(urlConnection.getResponseCode()));
                Log.i("msg",urlConnection.getResponseMessage());


                // urlConnection.getErrorStream()
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                total = new StringBuilder();
                String line;

                while ((line=r.readLine())!=null){
                    total.append(line).append('\n');

                }

                System.out.println(total);



                urlConnection.disconnect();



            }catch (Exception e){
                //  e.printStackTrace();
                //  Log.d("exceptionConexion",e.getMessage());
            }
            return String.valueOf(total);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Alertastorj alertastorj = new Alertastorj();
            try {

                Log.d("mensajesinjson",s);

                JSONObject mensajejson = new JSONObject(s);
                Log.d("mensajejson",mensajejson.getString("error"));

                switch (mensajejson.getString("error")){

                    case "Must supply an email":
                        alertastorj.setMensaje(getString(R.string.email_alert));
                        alertastorj.show(getFragmentManager(),null);
                        break;

                    case "Email is already registered":
                        alertastorj.setMensaje(getString(R.string.Existing_email));
                        alertastorj.show(getFragmentManager(),null);
                        break;

                    case "Invalid email":
                        alertastorj.setMensaje(getString(R.string.Invalid_Email));
                        alertastorj.show(getFragmentManager(),null);
                        break;

                        default:
                            break;
                }
            }catch (Throwable t){
                //t.getCause();
                alertastorj.setMensaje(getString(R.string.Successful_registration));
                alertastorj.show(getFragmentManager(),null);
                email = (EditText) findViewById(R.id.txtemail);
                email.setText("");
                password =(EditText)findViewById(R.id.txtpassword);
                password.setText("");
                //Log.e("App","error");
            }


        }
    }

}
