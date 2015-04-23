package com.jab.storepedia.adater;

import com.jab.storepedia.R;
import com.jab.storepedia.app.AppController;
import com.jab.storepedia.model.Location;
import com.jab.storepedia.model.Store;
 
import java.util.ArrayList;
import java.util.List;
 
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
 
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
 
public class Location_Adapter extends BaseAdapter implements Filterable {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Location> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
 
    public Location_Adapter(Activity activity, List<Location> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }
 
    @Override
    public int getCount() {
        return movieItems.size();
    }
 
    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public void resetData(List<Location> Location){
    	this.movieItems = Location;
    }
    
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d("GGGGGG", "**** PUBLISHING RESULTS for: " + constraint + " datasize:" );
                movieItems = (List<Location>) results.values;
                Location_Adapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d("GGGGGG", "**** PERFORM FILTERING for: " + constraint);
                List<Location> filteredResults = getFilteredResults(constraint);
                

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

			private List<Location> getFilteredResults(CharSequence constraint) {
				// TODO Auto-generated method stub
				Log.d("GGGGGG","111111" + constraint);

				//String filterString = constraint.toString().toLowerCase();
				String name = constraint.toString();
				Log.d("GGGGGG","222222movieItems.size(): " + movieItems.size());
	            FilterResults results = new FilterResults();
	            int count = movieItems.size();
	            final ArrayList<Location> nlist = new ArrayList<Location>(count);
	            Log.d("GGGGGG","333333movieItems.size(): " + movieItems.size());
	            for(Location Location:movieItems){
	            	Log.d("GGGGGG","Title:" + Location.getTitle());
	            	//Log.d("GGGGGG","Genre:" + Location.getGenre() + " Name:" + name + " Cat: " + cat);
	            	if(Location.getTitle().toLowerCase().contains(name))
	            			nlist.add(Location);
	            }
                
	            results.values = nlist;
	            results.count = nlist.size();

	            return nlist;
			}
        };
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);
 
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        //TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
 
        // getting movie data for the row
        Location m = movieItems.get(position);
// 
//        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
//         
//        // title
        title.setText(m.getTitle());
//         
//        // rating
        //rating.setText("LID: " + String.valueOf(m.getLID()));
         
        // genre
        //genre.setText(m.getNum());
        
        //year.setText(m.getNum());
        
        
        // release year
        //year.setText("Number of Store: " +String.valueOf(m.getNum()));
// 
        return convertView;
    }
 
}
