package com.jab.storepedia.adater;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jab.storepedia.R;
import com.jab.storepedia.app.AppController;
import com.jab.storepedia.model.Lcomment;
import com.jab.storepedia.model.Location;
import com.jab.storepedia.model.Store;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Lcomment_adapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Activity activity;
	private List<Lcomment> lcommentitem;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	
	public Lcomment_adapter(Activity activity, List<Lcomment> lcommentitem) {
        this.activity = activity;
        this.lcommentitem = lcommentitem;
    }
 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lcommentitem.size();
	}

	@Override
	 public Object getItem(int location) {
        return lcommentitem.get(location);
    }

	@Override
	public long getItemId(int position) {
        return position;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.lcomment, null);
 
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView agreed = (TextView) convertView.findViewById(R.id.agreed);
        TextView disagreed = (TextView) convertView.findViewById(R.id.disagreed);
        TextView comment = (TextView) convertView.findViewById(R.id.comment);
        Lcomment m = lcommentitem.get(position);
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        username.setText(m.getUsername()); 
        agreed.setText(String.valueOf(m.getagreed()));
        agreed.setTextColor(Color.GREEN);
        disagreed.setText(String.valueOf(m.getdisagreed()));
        disagreed.setTextColor(Color.RED);
        comment.setText(m.getcomment());
		return convertView;
	}
	

}
