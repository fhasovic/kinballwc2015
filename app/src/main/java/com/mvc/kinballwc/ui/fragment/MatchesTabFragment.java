package com.mvc.kinballwc.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvc.kinballwc.R;
import com.mvc.kinballwc.model.Match;
import com.mvc.kinballwc.ui.adapter.MatchRecyclerAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Mario Velasco Casquero
 * Date: 13/07/2015
 * Email: m3ario@gmail.com
 */
public class MatchesTabFragment extends Fragment {

    private static final String TAG = "MatchesTabFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int tabNumber;

    public static MatchesTabFragment newInstance(int tabNumber) {
        MatchesTabFragment fragment = new MatchesTabFragment();
        fragment.tabNumber = tabNumber;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_matches_tab, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new MatchRecyclerAdapter(new ArrayList<Match>());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "ParseQuery get matches, tab: " + tabNumber);
        // Define the class we would like to query
        ParseQuery<Match> query = ParseQuery.getQuery(Match.class);
        // Define our query conditions
        //query.whereEqualTo("owner", ParseUser.getCurrentUser());
        // Execute the find asynchronously
        // TODO make query with date
        query.include("team1");
        query.include("team2");
        query.include("team3");
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<Match>() {
            public void done(List<Match> itemList, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "ParseQuery ok, tab: " + tabNumber + " matches: itemList: " + itemList.size());
                    onMatchesReceived(itemList);
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }


    private void onMatchesReceived(List<Match> itemList) {
        mAdapter = new MatchRecyclerAdapter(itemList);
        mRecyclerView.swapAdapter(mAdapter, false);
    }


}
