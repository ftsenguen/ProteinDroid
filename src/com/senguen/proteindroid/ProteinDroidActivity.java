package com.senguen.proteindroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProteinDroidActivity extends Activity implements OnClickListener{
    
    Button bRun, bInfo, bClear;
    TextView tvResults;
    EditText etProtein, etTarget, etError;
    String output = "nothing";
    Float target=0f, protein;
    int error=0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initialize();
    }

	private void initialize() {
		
		bRun = (Button)findViewById(R.id.bRun);
		bRun.setOnClickListener(this);
		
		bClear = (Button)findViewById(R.id.bClear);
		bClear.setOnClickListener(this);
		
		bInfo = (Button)findViewById(R.id.bInfo);
		bInfo.setOnClickListener(this);
		
		tvResults = (TextView)findViewById(R.id.tvResults);
		etProtein = (EditText)findViewById(R.id.etProtein);
		etTarget = (EditText)findViewById(R.id.etTarget);
		etError = (EditText)findViewById(R.id.etError);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.bRun: run();
						break;
		case R.id.bClear: 	clear();
							break;
		case R.id.bInfo:	info();
							break;
		
		}
			
	}
	
	private void run(){
		String sequence = etProtein.getText().toString();
		protein = readProtein(sequence);
		target = Float.parseFloat(etTarget.getText().toString());
		error = Integer.parseInt(etError.getText().toString());
		checkSequence(sequence);
		deletions(sequence);
		additions(sequence);
		fragments(sequence);
		tvResults.setText(output);
	}
	
	private void clear(){
		tvResults.setText(" ");
		output = "";
	}
	
	private void info(){
		String sequence = etProtein.getText().toString();
		protein = readProtein(sequence);
		output = ("M+ = " + protein + '\n' +
				"M+H = " + (protein+1.008) + '\n' +
				"M+Na = "+ (protein+22.989) + '\n' +
				"M+K = " + (protein+39.0983) + '\n');
		tvResults.setText(output);
	}
	
	private float readProtein(String readSeq) {
		float runningcount = 0;
		
		float ala = 71.03711f;
		float arg = 156.10111f;
		float asn = 114.04293f;
		float asp = 115.02694f;
		float cys = 103.00918f;
		float gln = 128.05858f;
		float glu = 129.04259f;
		float gly = 57.02146f;	
		float his = 137.05891f;
		float ile = 113.08406f;
		float leu = 113.08406f;
		float lys = 128.09496f;
		float met = 131.04048f;
		float phe = 147.06841f;
		float pro = 97.05276f;
		float ser = 87.03203f;
		float thr = 101.04768f;
		float trp = 186.07931f;
		float tyr = 163.06333f;
		float val = 99.06841f;
		
		readSeq = readSeq.toUpperCase();
		
		for(int i=0; i < readSeq.length();i++){
			switch (readSeq.charAt(i)){
			case 'A':	runningcount = runningcount+ala;
						break;
			case 'C':	runningcount = runningcount+cys;
						break;
			case 'D':	runningcount = runningcount+asp;
						break;
			case 'E':	runningcount = runningcount+glu;
						break;
			case 'F':	runningcount = runningcount+phe;
						break;
			case 'G':	runningcount = runningcount+gly;
						break;
			case 'H':	runningcount = runningcount+his;
						break;
			case 'I':	runningcount = runningcount+ile;
						break;
			case 'K':	runningcount = runningcount+lys;
						break;
			case 'L':	runningcount = runningcount+leu;
						break;
			case 'M':	runningcount = runningcount+met;
						break;
			case 'N':	runningcount = runningcount+asn;
						break;
			case 'P':	runningcount = runningcount+pro;
						break;
			case 'Q':	runningcount = runningcount+gln;
						break;
			case 'R':	runningcount = runningcount+arg;
						break;
			case 'S':	runningcount = runningcount+ser;
						break;
			case 'T':	runningcount = runningcount+thr;
						break;
			case 'V':	runningcount = runningcount+val;
						break;
			case 'W':	runningcount = runningcount+trp;
						break;
			case 'Y':	runningcount = runningcount+tyr;
						break;
			case 'X':	runningcount = runningcount+0;
						break;
			}
		}
		
		return runningcount;
		//Copy stuff from the other program
	}
	
	private void deletions(String sequence){
		String delSeq = "";
		for (int i=0; i<sequence.length();i++){
			if(i==0){
				delSeq="X"+sequence.substring(1);
				checkSequence(delSeq);
			}
			else{
				delSeq=sequence.substring(0, i)+"X"+sequence.substring(i+1);
				checkSequence(delSeq);
			}
		}
	}
	
	private void additions(String sequence){
		String addSeq;
		for (int i=0;i < sequence.length();i++){
			addSeq = sequence.substring(0,i) + sequence.substring(i) + sequence.substring(i+1);
			checkSequence(addSeq);
		}
	}

	private void fragments(String sequence){
		String fragSeq;
		int counter = sequence.length();
		while (counter>0){
			int index = 0;
			while (index < counter){
				fragSeq = sequence.substring(index, counter);
				checkSequence(fragSeq);
				counter--;
				index++;
			}
		}
	}

	private void checkSequence(String seq) {
		float chkMass = readProtein(seq);
		int checkMult = 4;
		
		//check just for multiple M+
		while (checkMult > 0){
			if((target-error)<(chkMass*checkMult) && (target+error) > (chkMass*checkMult)){
					output = ("" + (chkMass*checkMult) + " is " + checkMult + "M+ of " + seq + '\n');
			}
			checkMult--;
		}
			
		//check multiple M+H
		checkMult=4;
		while (checkMult > 0){
			int checkMultH=4;
			while(checkMultH > 0){
				if (((target-error)<((chkMass*checkMult)+(1.008*checkMultH))/checkMultH) && ((target+error)>((chkMass*checkMult)+(1.008*checkMultH))/checkMultH)){
					if ((checkMult != checkMultH) || (checkMult == 1)){
						output = output + ("" + ((chkMass*checkMult + 1.008*checkMultH)/checkMultH) + " is " + checkMult + "M+" + checkMultH + "H of " + seq + '\n');
					}
				}
				checkMultH--;
			}
			checkMult--;
		}
		
		//check multiple M+Na
		checkMult=4;
		while (checkMult > 0){
			int checkMultNa=4;
			while(checkMultNa > 0){
				if (((target-error)<((chkMass*checkMult)+(22.989*checkMultNa))/checkMultNa) && ((target+error)>((chkMass*checkMult)+(22.989*checkMultNa))/checkMultNa)){
					if ((checkMult != checkMultNa) || (checkMult == 1)){
						output = output + ("" + ((chkMass*checkMult + 22.989*checkMultNa)/checkMultNa) + " is " + checkMult + "M+" + checkMultNa + "Na of" +seq+ '\n');
					}
				}
				checkMultNa--;
			}
			checkMult--;
		}
		
		//check multiple M+K
		checkMult=4;
		while (checkMult > 0){
			int checkMultK=4;
			while(checkMultK > 0){
				if (((target-error)<((chkMass*checkMult)+(39.0983*checkMultK))/checkMultK) && ((target+error)>((chkMass*checkMult)+(39.0983*checkMultK))/checkMultK)){
					if ((checkMult != checkMultK) || (checkMult == 1)){
						output = output + ("" + ((chkMass*checkMult + 39.0983*checkMultK)/checkMultK) + " is " + checkMult + "M+" + checkMultK + "K of " + seq + '\n');
					}
				}
				checkMultK--;
			}
			checkMult--;
		}
		
		//check multiple M+TFA
				checkMult=4;
				while (checkMult > 0){
					int checkMultTfa=4;
					while(checkMultTfa > 0){
						if (((target-error)<((chkMass*checkMult)+(114.02*checkMultTfa))/checkMultTfa) && ((target+error)>((chkMass*checkMult)+(114.02*checkMultTfa))/checkMultTfa)){
							if ((checkMult != checkMultTfa) || (checkMult == 1)){
								output = output+("" + ((chkMass*checkMult + 114.02*checkMultTfa)/checkMultTfa) + " is " + checkMult + "M+" + checkMultTfa + "Tfa of " +seq+ '\n');
							}
						}
						checkMultTfa--;
					}
					checkMult--;
				}
	}
}