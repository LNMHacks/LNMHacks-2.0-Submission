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

public  class tenth extends Activity {
    int i=0,one=0,two=0,three=0,art=0,nmed=0,med=0,comm=0;
    RadioGroup r1;
    int max=0,max1=0;
    String ii="art",ia="art";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assess);

        r1= (RadioGroup) findViewById(R.id.radioGroup);
       final TextView t;
        t = (TextView) findViewById(R.id.textView7);

        final String[] Ques={ " 1.Do you like to participate in community or services and/or volunteering? ",
                                "2.Are you a good listner and can make friends with different kinds of people?",
                "3.Do you find it difficult to deal with huge set of numbers and data?",
                "4.Do you feel confident in handling other people’s money?",
                "5.Do you enjoy reading technical materials and solve technical problems?",
                "6.Do you enjoy working in a labororatory and experimenting?",
                "7.Do you find it’s exciting to learn how thing’s grow and stay alive?",
                "8.Do you think working in a hospital or other medical facilities is a bad idea?",
                "9.Can you analyse financial information and interpret it to others?",
                "10.Do you find new technologies interesting and you think they are fun?",
                "11.Do you like to spend time in library collecting information about world history,evolution etc.?",
                "12.Do you find it boring to learn chemical formulas and learn physics theory?",
                "13.Do you like to read business papers?",
                "14.Do you show long interest in how rules and regulations work?",
                "15.Do you have interest in computer coding?",
                "16.Do you find it boring to mug up facts and figures?",
                "17.According to you in which branch does your interest lie"

        };
        t.setText(Ques[0]);
i++;
        Button b;
        b= (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(R.id.R61==r1.getCheckedRadioButtonId())
                    {
                        one++;
                        switch (i)
                        {
                            case 1 : art++ ;
                                     break;
                            case 2 : art++;
                                     comm++;
                                     break;
                            case 3 : comm--;
                                     break;
                            case 4 : comm+=2;
                                     break;
                            case 5 : nmed+=2;
                                     break;
                            case 6 : nmed++;
                                     med++;
                                     break;
                            case 7 : med+=2;
                                     break;
                            case 8 : med--;
                                     break;
                            case 9 : comm+=2;
                                     break;
                            case 10 : nmed+=2;
                                      med+=2;
                                     break;
                            case 11 : art+=2;
                                      break;
                            case 12 : nmed-=1.5;
                                      med-=1.5;
                                      break;
                            case 13 : comm++;
                                      break;
                            case 14 : art+=1.5;
                                    break;
                            case 15 : nmed++;
                                     break;
                            case 16 : art-=1;
                                    break;
                        }
                    }
                    else if(R.id.R61==r1.getCheckedRadioButtonId())
                    {

                        two++;
                        switch (i)
                        {
                            case 1 : art--;
                                break;
                            case 2 : art--;
                                comm--;
                                break;
                            case 3 :comm++;
                                    break;
                            case 4 : comm-=1.5;
                                    break;
                            case 5 : nmed--;
                                break;
                            case 6 : nmed--;
                                    med--;
                                break;
                            case 7 : med--;
                                break;
                            case 8 : med++;
                                break;
                            case 9 : comm--;
                                break;
                            case 10 : nmed--;
                                      med--;
                                break;
                            case 11 : art-=1.5;
                                break;
                            case 12 : nmed++;
                                      med++;
                                    break;
                            case 13 : comm--;
                                break;
                            case 14 : art--;
                                break;
                            case 15 : nmed--;
                                break;
                            case 16 : art+=1.5;
                                break;
                        }
                    }
                    else if(R.id.R61==r1.getCheckedRadioButtonId())
                    {
                        three++;
                    }
                if(i<16)
                {
                   r1.clearCheck();
                   t.setText(Ques[i]);
               i++;}
                else
               {
                   //Toast.makeText(tenth.this,String.valueOf(one),Toast.LENGTH_LONG).show();

                   if(art>max)
                   {
                       max=art;
                       ii="Humanities";
                   }
                   if(med>max)
                   {
                       max=med;
                       ii="Science (PCB)";
                   }
                   if(nmed>max)
                   {
                       max=nmed;
                       ii="Science (PCM)";
                   }
                   if(comm>max)
                   {
                       max=comm;
                       ii="Commerce";
                   }

                   Intent i = getIntent();
                   String sci = i.getExtras().getString("Sci");
                   String mat = i.getExtras().getString("Mat");
                   String ss = i.getExtras().getString("SS");
                    String inst=i.getExtras().getString("feel");
                   int Sci = Integer.parseInt(sci);
                   int Mat = Integer.parseInt(mat);
                   int Ss = Integer.parseInt(ss);
                   if(!(Sci<0||Mat<0||Ss<0||Sci>100||Mat>100||Ss>100)) {

                       art*=9.8;
                       med*=9.8;
                       nmed*=9.8;
                       comm*=9.8;
                    int sc=(Mat+Sci)/2;
                          Sci *=0.4;
                       Mat*=0.4;
                       Ss*=0.4;
                      sc*=0.4;
                      /*  Sci/=5;
                       Mat/=5;
                       Ss/=5;
                       sc/=5;*/
                       if(Sci<-2)
                           Sci=-2;
                       if(Mat<-2)
                           Sci=-2;
                       if(Ss<-2)
                           Ss=-2;
                        if(sc<-2)
                            sc=-2;
                       art+=Ss;
                       nmed+=sc;
                       med+=Sci;
                       comm+=Mat;

                       if(art>max1)
                       {
                           max1=art;
                           ia="Humanities";
                       }
                       if(med>max1)
                       {
                           max1=med;
                           ia="Science (PCB)";
                       }
                       if(nmed>max1)
                       {
                           max1=nmed;
                           ia="Science (PCM)";
                       }
                       if(comm>max1)
                       {
                           max1=comm;
                           ia="Commerce";
                       }

                       View v = getLayoutInflater().inflate(R.layout.start, null);
                       TextView tt;
                       setContentView(v);
                       tt = (TextView) v.findViewById(R.id.tv1);
                       String name = i.getExtras().getString("name");

                       if(ia.equals(inst))
                       {
                           if((ia.equals(ii)))
                           {
                               tt.setText(name + " , We suggest you to go with  "+ inst +  " because all your answers somewhat points towards that direction");

                           }
                           else
                           {
                               tt.setText(name + " , Although your inputs from those questions suggest your interest in " + ii +" but using your boards marks and your gut feeling we suggest you to go with "+ ia);

                           }
                       }
                       else
                       {
                           if(!(ia.equals(ii)))
                           {
                                if(ii.equals(inst))
                                {
                                    tt.setText(name + " , Although your inputs suggest that you should go with  " + inst + " but because of your average performance in boards in the subject of your interest we feel that you should go with "+ ia + " as you have better scope in it");
                                }
                                else
                                {
                                    tt.setText(name + " , you should not go with  "+ inst +" as your answers as well as marks in boards don't suggest scope in that particular field.I suggest you to go with "+ ia);
                                }
                           }
                           else
                               {
                                   tt.setText(name + " , you should not go with with "+ inst+ " as your answers as well as marks in boards don't suggest scope in that  particular field.I suggest you to go with "+ ia);
                               }

                       }


                   }
                   else
                   {
                       View v = getLayoutInflater().inflate(R.layout.start, null);
                       TextView tt;
                       setContentView(v);
                       tt = (TextView) v.findViewById(R.id.tv1);
                       tt.setText("Some Data enter is wrong . Please check it and try again ");
                   }

               }


            }
        });






    }
}
