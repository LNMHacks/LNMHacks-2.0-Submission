package daksh.careerguide;

/**
 * Created by Daksh on 11/11/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;



public  class Star extends Activity {
    String s;
    RadioGroup r;
    EditText E1,E2, E3, E4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenth);


        E1 = (EditText) findViewById(R.id.editText2);
        E2 = (EditText) findViewById(R.id.editText3);
        E3 = (EditText) findViewById(R.id.editText4);
        E4 = (EditText) findViewById(R.id.editText5);



        /*
        int S = Integer.parseInt(E2.getText().toString());
        int M = Integer.parseInt(E3.getText().toString());
        int SS = Integer.parseInt(E4.getText().toString());*/

        Button b1;
        r = (RadioGroup) findViewById(R.id.radioGroup) ;
        b1 = (Button) findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(R.id.RPM==r.getCheckedRadioButtonId())
                {
                    s="Science (PCM)";
                }
                if(r.getCheckedRadioButtonId()==R.id.RPB)
                {
                    s="Science (PCB)";
                } if(r.getCheckedRadioButtonId()==R.id.RC)
                {
                    s="Commerce";
                } if(r.getCheckedRadioButtonId()==R.id.RA)
                {
                    s="Humanities";
                }
                String name = E1.getText().toString();
                Toast.makeText(Star.this,"Hi  "+String.valueOf(name)+ " !! Start the Test",Toast.LENGTH_LONG).show();
                Intent act1 = new Intent(Star.this, tenth.class);
                act1.putExtra("Sci",E2.getText().toString());
                act1.putExtra("Mat",E3.getText().toString());
                act1.putExtra("SS",E4.getText().toString());
                 act1.putExtra("feel",s);
                act1.putExtra("name",name);
                startActivity(act1);

            }
        });

    }
    }