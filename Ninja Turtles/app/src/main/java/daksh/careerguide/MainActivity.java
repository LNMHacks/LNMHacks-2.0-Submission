package daksh.careerguide;


        import android.app.Activity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    String[] country = { "Enter a Value ","India", "USA", "China", "Japan", "Other",  };
    void change(String[] arr,int n)
    {
    int f=country.length;
        for(int i=1;i<f;f=i++)
            country[i]=null;
        for(int i=1;i<n;i++)
            country[i]=arr[i-1];



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        on();
    }
      public void on() {
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(aa);

    }





    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        if(position>0) {
            Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_LONG).show();
            String[] state = {"UP", "Rajasthan ", "MP"};
           int n= state.length;
            change(state,n);
            on();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {


    }

}
