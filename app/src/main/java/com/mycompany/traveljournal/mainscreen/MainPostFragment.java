package com.mycompany.traveljournal.mainscreen;

import android.content.Intent;
import android.view.MenuItem;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListFragment;
import com.mycompany.traveljournal.mapscreen.MapActivity;
import com.mycompany.traveljournal.profilescreen.ProfileActivity;
import com.mycompany.traveljournal.wishlistscreen.WishListActivity;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class MainPostFragment extends PostsListFragment {

    @Override
    public void populateList() {

    }

    @Override
    public void refreshList() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_mapview){
            executeMapIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    private void executeMapIntent(){
        Intent i = new Intent(getActivity(), MapActivity.class);
        startActivity(i);
    }
}
