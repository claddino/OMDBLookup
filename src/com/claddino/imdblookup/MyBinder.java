package com.claddino.imdblookup;


import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

class MyBinder implements ViewBinder{
    
    private String tag = MyBinder.class.getName();

	@Override
    public boolean setViewValue(View view, Object data,
            String textRepresentation) {
    	ImageView photo = (ImageView) view.findViewById(R.id.poster);
        if (view.getId() ==  R.id.imdbRating) {
             float f = 0.0f;
			try {
				f = Float.parseFloat(textRepresentation);
			} catch (NumberFormatException e) {
				// TODO: handle exception
				Log.d(tag, e.getMessage());
				((RatingBar) view).setRating(f);
			}
			((RatingBar) view).setRating(f);
            
            return true;
        } 
        
        else if (view.getId() ==  R.id.director) {
        	   ((TextView) view).setText((String) "Director : " +  data);
        	   return true;
		}
        
        else if (view.getId() ==  R.id.poster) {
        	Uri myUri = Uri.parse(textRepresentation);
        	Picasso.with(photo.getContext()).load(myUri).into(photo);
        	//TODO add placeholder for poster
     	   return true;
		}
        
      /*  else if (view.getId() ==  R.id.imgposter) {
        	Uri myUri = Uri.parse(textRepresentation);
        	
        	if (myUri.toString().length()>1) {
        		Picasso.with(imgphoto.getContext()).load(myUri).into(imgphoto);
			}
        	else {
        		Picasso.with(imgphoto.getContext()).load(R.drawable.ic_launcher).into(imgphoto);
			}
        	//TODO add placeholder for poster
     	   return true;
		}*/
        
        	 return false;
        
       
    }

}