package com.rizafu.stickyswipepullindexlistview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.rizafu.stickyswipepullindexlistview.customview.IndexView;
import com.rizafu.stickyswipepullindexlistview.customview.PullToRefreshStickyList;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * all action click at SwipeAdapter
 *
 * base on :
 * https://github.com/emilsjolander/StickyListHeaders
 * https://github.com/47deg/android-swipelistview
 * https://github.com/chrisbanes/Android-PullToRefresh
 * https://github.com/xuyangbill/ListIndex
 */
public class MyActivity extends Activity {
    private String[] countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        final PullToRefreshStickyList stlist = (PullToRefreshStickyList)findViewById(R.id.stickSwipeList);
	    IndexView indexView = (IndexView)findViewById(R.id.indexView);
	    TextView selectIndex = (TextView)findViewById(R.id.select_index);

	    StickyAdapter adapter = new StickyAdapter(this);
        stlist.getRefreshableView().setAdapter(adapter);

	    /** indexable listview */
	    indexView.init(stlist,selectIndex);

	    /** action pull to refresh */

	    stlist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<StickyListHeadersListView>() {
		    @Override
		    public void onRefresh(PullToRefreshBase<StickyListHeadersListView> refreshView) {
			    SystemClock.sleep(3000);
			    stlist.onRefreshComplete();
		    }
	    });

    }

    public class StickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {


        private LayoutInflater inflater;

        public StickyAdapter(Context context){
            inflater = LayoutInflater.from(context);
            countries = context.getResources().getStringArray(R.array.countries);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return countries.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return countries[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, parent , false);
                holder.swipelist = (SwipeListView) convertView.findViewById(R.id.example_lv_list);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.swipelist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            List<SwipeItem> dataItem = new ArrayList<SwipeItem>();
            SwipeItem currentItem = new SwipeItem();
            currentItem.setLabel(countries[position]);
            dataItem.add(currentItem);
	        SwipeAdapter sadapter = new SwipeAdapter(getApplicationContext() , dataItem , position);

            holder.swipelist.setAdapter(sadapter);
            return convertView;
        }

        @Override
        public View getHeaderView(int position, View convertView,
                                  ViewGroup parent) {
            HeaderViewHolder holder;
            if(convertView == null){
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.header, parent , false);
                holder.text = (TextView) convertView.findViewById(R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            //set header text first char
            String headerText = ""+countries[position].subSequence(0, 1).charAt(0);
            holder.text.setText(headerText);
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            //return the first character of the country as ID because this is what headers are based upon
            return countries[position].subSequence(0, 1).charAt(0);
        }

        class HeaderViewHolder {
            TextView text;
        }

        class ViewHolder {
            //TextView text;
            SwipeListView swipelist;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SwipeItem {
        private String label;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public class SwipeViewHolder {
        TextView label;
        Button edit_btn;
        Button delete_btn;
    }

    public class SwipeAdapter extends BaseAdapter {
        List<SwipeItem> data;
        Context context;
        int parent_postion;

        public SwipeAdapter(Context context, List<SwipeItem> data , int parent_postion){
            this.context = context;
            this.data = data;
            this.parent_postion = parent_postion;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int pos, View cview, ViewGroup viewGroup) {
            SwipeItem curitem = this.data.get(pos);
            SwipeViewHolder swipeholder;
            if(cview == null){
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                cview = li.inflate(R.layout.package_row , viewGroup , false);
                swipeholder = new SwipeViewHolder();
                swipeholder.label = (TextView)cview.findViewById(R.id.front_label);
                swipeholder.label.setText(countries[parent_postion]);
                swipeholder.edit_btn = (Button) cview.findViewById(R.id.btn_edit);
                swipeholder.delete_btn = (Button) cview.findViewById(R.id.btn_delete);

	            /* item click action*/

	            swipeholder.label.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
			            Toast.makeText(getApplicationContext() , "Item Click "+countries[parent_postion] , Toast.LENGTH_SHORT).show();
		            }
	            });

                /* button action */

                swipeholder.edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext() , "Edit "+countries[parent_postion] , Toast.LENGTH_SHORT).show();
                    }
                });

                swipeholder.delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext() , "Delete "+countries[parent_postion] , Toast.LENGTH_SHORT).show();
                    }
                });

                cview.setTag(swipeholder);
            } else {
                swipeholder = (SwipeViewHolder)cview.getTag();
            }

            return cview;
        }
    }
}
