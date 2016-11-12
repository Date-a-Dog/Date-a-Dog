package dateadog.dateadog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import dateadog.dateadog.tindercard.FlingCardListener;
import dateadog.dateadog.tindercard.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import com.facebook.login.LoginManager;


public class DogSwipeActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface {
    public static MyAppAdapter myAppAdapter; //holds the app adapter
    private ArrayList<DogProfile> pending; //list of all dogs from database pending to be swiped
    private ArrayList<DogProfile> likedDogs; //list of all dogs liked from swiping
    private TextView noDogs; //displays when there are no dogs left
    public static ViewHolder viewHolder;
    private ArrayList<Data_TinderUI> al;
    private SwipeFlingAdapterView flingContainer;

    public static void removeBackground() {
        viewHolder.background.setVisibility(View.GONE);
        myAppAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipedog);
        noDogs = (TextView)findViewById(R.id.textNoDogs);
        //noDogs.setText("No More Dogs!");
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        al = new ArrayList<>();
        al.add(new Data_TinderUI("http://i.dailymail.co.uk/i/pix/2016/06/17/09/3562F5B200000578-3646283-image-m-30_1466150632154.jpg", "Mika, 3 years \nMy name is Mika and I love playing with my blue ducky, taking walks, and living life!"));
        al.add(new Data_TinderUI("https://i.ytimg.com/vi/opKg3fyqWt4/hqdefault.jpg", "Ko, 5 months\nMy name is Ko and I am an excited puppy ready for action!"));
        al.add(new Data_TinderUI("http://images.meredith.com/content/dam/bhg/Images/2012/11/28/405944_10150676681556019_1918501130_n.jpg.rendition.largest.ss.jpg", "Max, 2 years \nMy name is Max and I am a shy guy who loves to cuddle and take walks on a crisp Sunday afternoon!"));

        myAppAdapter = new MyAppAdapter(al, DogSwipeActivity.this);
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                al.remove(0);
                myAppAdapter.notifyDataSetChanged();
                if (al.size() == 0) {
                    noDogs.setText("No More Dogs.\nRefresh Page Soon!");
                    noDogs.setTextColor(Color.BLUE);
                }
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                al.remove(0);
                myAppAdapter.notifyDataSetChanged();
                if (al.size() == 0) {
                    noDogs.setText("No More Dogs.\nRefresh Page Soon!");
                    noDogs.setTextColor(Color.BLUE);
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                myAppAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dog_swipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        int compare = R.id.action_logout;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(DogSwipeActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_likeddogs) {
            Intent intent = new Intent(DogSwipeActivity.this, LikedDogsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_home) {
            Intent intent = new Intent(DogSwipeActivity.this, DogSwipeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActionDownPerform() {
        Log.e("action", "bingo");
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public TextView DataText;
        public ImageView cardImage;

    }

    public class MyAppAdapter extends BaseAdapter {


        public List<Data_TinderUI> parkingList;
        public Context context;

        private MyAppAdapter(List<Data_TinderUI> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.DataText.setTextColor(Color.BLACK);
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.background.setBackgroundColor(Color.BLUE);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                viewHolder.cardImage.setBackgroundColor(Color.BLUE);
                viewHolder.cardImage.setMinimumHeight(60);
                viewHolder.cardImage.setMaxHeight(60);

                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.DataText.setText(parkingList.get(position).getDescription() + "");

            Glide.with(DogSwipeActivity.this).load(parkingList.get(position).getImagePath()).into(viewHolder.cardImage);

            return rowView;
        }
    }

    //this method loads the data into the al array needed from the pending dogs arraylist
    //takes in al and arrayList as params and returns the updated al
    private List<Data_TinderUI> loadAL(List<DogProfile> pending, List<Data_TinderUI>al) {
        return null;
    }
}