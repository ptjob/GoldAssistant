package com.quark.adapter;

import java.util.List;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.model.UserCommentModle;

/**
 * @ClassName: 我的兼职评论
 * 
 * @Description:
 * 
 * @author:howe
 * 
 * @date: 2014-11-15 上午10:15:52
 */
public class JianliScanAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<UserCommentModle> list;
	private Context context;

	public JianliScanAdapter(Context context, List<UserCommentModle> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_comment, null);
			holder.comment = (ImageView) convertView
					.findViewById(R.id.comment_type);
			// holder.title = (TextView)
			// convertView.findViewById(R.id.comment_work);
			holder.remark = (TextView) convertView
					.findViewById(R.id.comment_contenx);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (list.get(i).getComment().equals("优秀")) {
			holder.comment.setBackgroundResource(R.drawable.icon_score_1);
		}
		if (list.get(i).getComment().equals("好评")) {
			holder.comment.setBackgroundResource(R.drawable.icon_score_2);
		}
		if (list.get(i).getComment().equals("中评")) {
			holder.comment.setBackgroundResource(R.drawable.icon_score_3);
		}
		if (list.get(i).getComment().equals("差评")) {
			holder.comment.setBackgroundResource(R.drawable.icon_score_4);
		}
		if (list.get(i).getComment().equals("放飞机")) {
			holder.comment.setBackgroundResource(R.drawable.icon_score_5);
		}

		String s = list.get(i).getTitle();
		String ss = list.get(i).getRemark();
		String str = s + ":" + ss;

		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new ForegroundColorSpan(R.color.ziti_huise), 0,
				s.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.remark.setText(style);
		return convertView;
	}

	private static class ViewHolder {
		ImageView comment;
		TextView remark;
		// TextView title ;
	}

}
