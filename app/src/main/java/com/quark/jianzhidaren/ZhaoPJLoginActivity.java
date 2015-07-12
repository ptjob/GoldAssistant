package com.quark.jianzhidaren;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.qingmu.jianzhidaren.R;

/**
 * 招兼职为商家入口
 * @author cluo
 *
 */
public class ZhaoPJLoginActivity extends Activity{

	private Button look;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_findpartjob);
		look = (Button)findViewById(R.id.look);
		look.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
				look.setVisibility(View.INVISIBLE);//隐藏随便看看
			}
		});
		
	}
}
