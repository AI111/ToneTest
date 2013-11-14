package com.example.tonetest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;

import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class GenerateSound extends Activity implements OnClickListener{
 private final float duration = 0.5f; // seconds
 private final int sampleRate = 10000;
 private final int numSamples = (int) (duration * sampleRate);
 public final short sample[] = new short[numSamples];
 Object syncObj = new Object();
 volatile int curButtone=0;
 volatile int buttoneCount=0;
 long start,end;
volatile StringBuilder builder = new StringBuilder();
short[] phoneFrequencies={697,770,852,941,1209,1336,1477,1633};
short[][] tones={
				{1336,941},//0
				{1209,697},//1
				{1336,697},//2
				{1477,697},//3
				//{1633,697},
				
				{1209,770},//4
				{1336,770},//5
				{1477,770},//6
				//{1633,770},
				
				{1209,852},//7
				{1336,852},//8
				{1477,852},//9
				//{1633,852},
				
				{1209,941},//*
				
				{1477,941},//#
				//{1633,941},
				};

EditText et;
 TextView tv1;
 Button bt1,bt2,bt3,bt4,bt5,bt6;
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  et = (EditText)findViewById(R.id.editText1);
  
 tv1=(TextView)findViewById(R.id.textView1);
  //tv2=(TextView)findViewById(R.id.textView1);
  bt1=(Button)findViewById(R.id.button1);
  bt2=(Button)findViewById(R.id.button2);
  bt3=(Button)findViewById(R.id.button3);

bt1.setOnClickListener(this);
bt2.setOnClickListener(this);
bt3.setOnClickListener(this);
Thread thread = new Thread(new Runnable() {
    public void run() {
    	
  	  while(true)
  	  {
  		  synchronized (syncObj) {
			try {
				syncObj.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
  		for(int i=0;i<et.getText().length();i++){
			char tmp =et.getText().charAt(i);
			tmp=Character.toLowerCase(tmp);
			if(tmp=='a'||tmp=='b'||tmp=='c')//2
			{
				genDtmfSignal(97, (int)tmp,2);
				Log.d("SET 2",tmp+"");
			}else 
			if(tmp=='d'||tmp=='e'||tmp=='f')//3
			{
				genDtmfSignal(100, (int)tmp,3);
				Log.d("SET 3",tmp+"");
			}else
			if(tmp=='g'||tmp=='h'||tmp=='i')//4
			{
				genDtmfSignal(103, (int)tmp,4);
				Log.d("SET 2",tmp+"");
			}else
			if(tmp=='j'||tmp=='k'||tmp=='l')//5
			{
				genDtmfSignal(106, (int)tmp,5);
			}else
			if(tmp=='m'||tmp=='n'||tmp=='o')//6
			{
				genDtmfSignal(109,(int)tmp,6);
			}else
			if(tmp=='p'||tmp=='q'||tmp=='r'||tmp=='s')//7
				{
				genDtmfSignal(112,(int)tmp,7);
			}else
			if(tmp=='t'||tmp=='u'||tmp=='v')//8
			{
				genDtmfSignal(116, (int)tmp,8);	
			}else
			if(tmp=='w'||tmp=='x'||tmp=='y'||tmp=='z')//9
			{
				genDtmfSignal(119, (int)tmp,9);		
			}else{
				
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (curButtone) {
			case  1:
				
				break;
			case  2:
				builder.append((char)(97+buttoneCount));
				break;
			case  3:
				builder.append((char)(100+buttoneCount));
				break;
			case  4:
				builder.append((char)(103+buttoneCount));
				break;
			case  5:
				builder.append((char)(106+buttoneCount));
				break;
			case  6:
				builder.append((char)(109+buttoneCount));
				break;
			case  7:
				builder.append((char)(112+buttoneCount));
				break;
			case  8:
				builder.append((char)(116+buttoneCount));
				break;
			case  9:
				builder.append((char)(119+buttoneCount));
	
				break;
	
			default:builder.append('?');
				break;
			}
			curButtone=-1;
			buttoneCount=0;
			
		}
  		tv1.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tv1.setText(builder.toString());
				builder=new StringBuilder();
			}
		});
  	  }
      
 
    }   
  });
  thread.start();

 }

 @Override
 protected void onResume() {
  super.onResume();

  // Use a new tread as this can take a while
  
 }
 
 void genTone(short fr1,short fr2){
	 float increment1 = (float)((2*Math.PI) * fr1 / sampleRate); // angular increment for each sample
	 float increment2 = (float)((2*Math.PI) * fr2 / sampleRate); // angular increment for each sample
	short A=Short.MAX_VALUE/3;
	 float angle1 = 0;//sample[i] = (short) (16000*Math.sin(/*2 * Math.PI * i / (sampleRate/freqOfTone)*/angle+=increment1)
	 float angle2= 0;
	 // fill out the array
  for (int i = 0; i < numSamples; ++i) {
   sample[i] =  (short) ((A*FloatMath.sin(angle1+=increment1))+ (A*FloatMath.sin(angle2+=increment2)));
   
  }


 }
 private void searchTones(){
	 float tmp;
	 short f1=0,f2=0;
	 //StringBuilder stringBuilder = new StringBuilder();
	 for(short fr: phoneFrequencies){
		tmp= Gercel(sample, fr);
		 
		//stringBuilder.append((fr+"|  +"+tmp+"\n"));
		 if(tmp>2E12&&f1==0){
			 f1=fr;
		 }else if (tmp>2E12&&f1!=0){
			 f2= fr;
		 }
		
	}
	 
	 
		//}
	 int i =getNButtome( f1, f2);
	 
	// stringBuilder.append(" "+i+" /// "+f1+" "+f2);
	// tv2.setText(stringBuilder.toString());
	 if(curButtone==i){
		 buttoneCount++;
	 }else{
		 curButtone=i;
	 }
	Log.d("SEARCH RESULT","B "+i+" curButtone "+curButtone+" buttoneCount "+buttoneCount);
	 
	
 }
 private int getNButtome(short fr1,short fr2)
 {
	 int i=-1;
	 switch (fr1) {
	case 697:
		 switch (fr2) {
			case 1209:
					i= 1;
				break;
			case 1336:
					i= 2;
				break;
			case 1477:
					i= 3;
			default: 
				break;
			}
		break;
	case 770:
		 switch (fr2) {
			case 1209:
					i= 4;
				break;
			case 1336:
					i= 5;
				break;
			case 1477:
					i= 6;
			default: 
				break;
			}
		break;
	case 852:
		 switch (fr2) {
			case 1209:
					i= 7;
				break;
			case 1336:
					i= 8;
				break;
			case 1477:
					i=9;
			default: 
				break;
			}
	break;
	case 941:
	
	break;

	default: 
		break;
	}
	 return i;
 }
 

 public float Gercel(short[] in, int k){
		
		
	 int N = in.length; //точек ДПФ
    // int k = checkFrequency/N; //номер спектрального отсчта
	

    //массив промежуточных результатов
     float[] tmp = new float[N];

		// TODO Auto-generated method stub
		//параметр альфаN
	    double omega =2*Math.cos(2*Math.PI*k/sampleRate);
	    	//	2*Math.cos(2*Math.PI*checkFrequency/sampleRate);
	    tmp[0] = in[0];    
	    tmp[1] = (float) (in[1]+omega*tmp[0]);
	    //поворотный к-т W_N^-k
	    float wr =    FloatMath.cos((float) (2*Math.PI*k/sampleRate)); //реальная часть
	    float wi =    FloatMath.sin((float) (2*Math.PI*k/sampleRate)); //мнимая часть
	    float coef=2*wr;
	 // итерационный расчет массива v согласно (14)
	    for(int i = 2; i < N; i++){
	        tmp[i] =(float) (in[i]+omega*tmp[i-1]- tmp[i-2]);
	    }
	    float SR = tmp[N-1]*wr-tmp[N-2];
	    float SI = tmp[N-1]*wi;
	    
	    float ans=(float) (tmp[N-1]*tmp[N-1]-omega*tmp[N-1]*tmp[N-2]+tmp[N-2]*tmp[N-2]);
	    //печать результата
	    //System.out.print("ANS "+k+" "+SR+" "+SI+" "+ ans);

	return ans;
	
}
 void playSound(){
  AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
		  sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
    AudioFormat.ENCODING_PCM_16BIT, numSamples,
    AudioTrack.MODE_STREAM);
  audioTrack.write(sample, 0, numSamples);
  audioTrack.play();
 }
 private void genDtmfSignal(int begin, int end,int number)
 {
	 
	 for( ;begin<=end;begin++){
		 start=System.currentTimeMillis();
		 genTone(tones[number][0], tones[number][1]);
		 playSound();
		 searchTones();
		 try {
			Thread.sleep(500-(System.currentTimeMillis()-start));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
 }

@SuppressLint("NewApi") @Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.button1:
		synchronized (syncObj) {
			syncObj.notifyAll();
		}
		
		break;
	case R.id.button2:
		Thread thread1 = new Thread(new Runnable() {
		      public void run() {
		    	  
		        genTone(tones[2][0],tones[2][1]);
		        playSound();
//		        tv.post(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						searchTones();
//					}
//				});
	       
		      }   
		    });
		thread1.start();
		searchTones();
		break;
	case R.id.button3:
		TrigonometricFunctionsChart draw = new TrigonometricFunctionsChart(sample, sampleRate);
		startActivity(draw.execute(this));
		
		for(short fr: phoneFrequencies){
			Log.d("SEARCH "+fr,""+Gercel(sample, fr));
		}
//	case R.id.button6:
//		tv2.setText(tv.getText());
//		break;
	default:
		break;
	}
}
}
