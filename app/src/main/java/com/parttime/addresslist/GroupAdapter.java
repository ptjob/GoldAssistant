/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.parttime.addresslist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.qingmu.jianzhidaren.R;

public class GroupAdapter extends ArrayAdapter<EMGroup> {

	private LayoutInflater inflater;

	public GroupAdapter(Context context, int res) {
		super(context, res);
		this.inflater = LayoutInflater.from(context);
	}

    public void updateData(List<EMGroup> gs){
        if(gs != null){
            clear();
            addAll(gs);
        }
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_group, null);
        }

        ((TextView) convertView.findViewById(R.id.name)).setText(getItem(
                position).getGroupName());

		return convertView;
	}

	@Override
	public int getCount() {
		 return super.getCount();
	}

}