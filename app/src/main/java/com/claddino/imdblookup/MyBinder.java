package com.claddino.imdblookup;


import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

class MyBinder implements ViewBinder{
    
    private String tag = MyBinder.class.getName();



    // Contains references to the handle and content views
    private View mHandle;
    private View mContent;

    // Does the panel start expanded?
    private boolean mExpanded = false;
    // The height of the content when collapsed
    private int mCollapsedHeight = 0;
    // The full expanded height of the content (calculated)
    private int mContentHeight = 0;
    // How long the expand animation takes
    private int mAnimationDuration = 0;

    // Listener that gets fired onExpand and onCollapse
    private OnExpandListener mListener;



	@Override
    public boolean setViewValue(final View view, Object data,
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
        else if (view.getId() ==  R.id.year) {

            ((TextView) view).setText((String) "(" +  data +")");
            return true;
        }
        else if (view.getId() ==  R.id.director) {
            view.setVisibility(View.GONE);
        	   ((TextView) view).setText((String) "Director : " +  data);
        	   return true;
		}
        else if (view.getId() ==  R.id.metascore) {

            ((TextView) view).setText((String) "MetaScore : " +  data);
            return true;
        }
        else if (view.getId() ==  R.id.awards) {

            ((TextView) view).setText((String) "Awards : " +  data);
            return true;
        }
        else if (view.getId() ==  R.id.country) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(view.getVisibility()== View.VISIBLE){
                        view.setVisibility(View.INVISIBLE);

                    }
                    else {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            });
            ((TextView) view).setText((String) "Country : " +  data);
            return true;
        }
        else if (view.getId() ==  R.id.language) {


            ((TextView) view).setText((String) "Language : " + data);
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

    private class PanelToggler implements View.OnClickListener {
        public void onClick(View v) {
            Animation a;
            if (mExpanded) {
                a = new ExpandAnimation(mContentHeight, mCollapsedHeight);
                mListener.onCollapse(mHandle, mContent);
            } else {
                a = new ExpandAnimation(mCollapsedHeight, mContentHeight);
                mListener.onExpand(mHandle, mContent);
            }
            a.setDuration(mAnimationDuration);
            mContent.startAnimation(a);
            mExpanded = !mExpanded;
        }

        /**
         * This is a private animation class that handles the expand/collapse
         * animations. It uses the animationDuration attribute for the length
         * of time it takes.
         */
        private class ExpandAnimation extends Animation {
            private final int mStartHeight;
            private final int mDeltaHeight;

            public ExpandAnimation(int startHeight, int endHeight) {
                mStartHeight = startHeight;
                mDeltaHeight = endHeight - startHeight;
            }

            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                android.view.ViewGroup.LayoutParams lp =
                        mContent.getLayoutParams();
                lp.height = (int) (mStartHeight + mDeltaHeight *
                        interpolatedTime);
                mContent.setLayoutParams(lp);
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        }

        /**
         * Simple OnExpandListener interface
         */


        private class DefaultOnExpandListener implements OnExpandListener {
            public void onCollapse(View handle, View content) {}
            public void onExpand(View handle, View content) {}
        }
    }
    }



