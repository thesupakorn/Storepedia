package com.jab.storepedia.adater;

import com.jab.storepedia.R;
import com.jab.storepedia.app.AppController;
import com.jab.storepedia.model.Store;
 
import java.util.ArrayList;
import java.util.List;
 
import android.app.Activity;
import android.content.Context;
import android.provider.SyncStateContract.Constants;
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
 
public class Store_Adapter extends BaseAdapter implements Filterable{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Store> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
 
    public Store_Adapter(Activity activity, List<Store> movieItems) {
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
 
  
    public void resetData(List<Store> mStore){
    	this.movieItems = mStore;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d("GGGGGG", "**** PUBLISHING RESULTS for: " + constraint + " datasize:" );
                movieItems = (List<Store>) results.values;
                Store_Adapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d("GGGGGG", "**** PERFORM FILTERING for: " + constraint);
                List<Store> filteredResults = getFilteredResults(constraint);
                

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

			private List<Store> getFilteredResults(CharSequence constraint) {
				// TODO Auto-generated method stub
				String filterString = constraint.toString().toLowerCase();
				
				String name = constraint.toString().split("<:>")[0];
				String cat = constraint.toString().split("<:>")[1];

	            FilterResults results = new FilterResults();
	            int count = movieItems.size();
	            final ArrayList<Store> nlist = new ArrayList<Store>(count);


	            for(Store mStore:movieItems){
	            	Log.d("GGGGGG","Genre:" + mStore.getGenre() + " filter_string:" + filterString);
	            	if(mStore.getTitle().toLowerCase().contains(name)&&mStore.getGenre().toLowerCase().contains(cat))
	            			nlist.add(mStore);
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
        Store m = movieItems.get(position);
// 
//        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
//         
//        // title
        title.setText(m.getTitle());
//         
//        // rating
        //rating.setText("Rating: " + String.valueOf(m.getRating()));
         
        // genre
        genre.setText(m.getGenre());
        
        
        // release year
        year.setText(String.valueOf(m.getSID()));
// 
        return convertView;
    }
 
}