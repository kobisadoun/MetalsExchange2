/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kobi.metalsexchange.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class CalculateFragment extends Fragment {

    private static final String LOG_TAG = CalculateFragment.class.getSimpleName();
    private ShareActionProvider mShareActionProvider;
    private String mExchangeRate;

    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mRateView;
    private TextView mRateUnitView;
    private Spinner mSpinner;
    private RadioButton mRadioGram;
    private RadioButton mRadioTroy;

    private String mMetalId;
    private double mMetalPrice;
    private long mDate;

    public CalculateFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMetalId = arguments.getString("METAL_ID");
            mMetalPrice = arguments.getDouble("CURRENT_VALUE");
            mDate = arguments.getLong("CURRENT_DATE");
        }

        View rootView = inflater.inflate(R.layout.fragment_calculate, container, false);
        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_day_textview);
        mRateView = (TextView) rootView.findViewById(R.id.detail_rate_textview);
        mRateUnitView = (TextView) rootView.findViewById(R.id.detail_rate_unit_textview);
        mRadioGram = (RadioButton) rootView.findViewById(R.id.radioGram);
        mRadioTroy = (RadioButton) rootView.findViewById(R.id.radioTroy);
        mSpinner = (Spinner) rootView.findViewById(R.id.gold_purity_spinner);
        mSpinner.setAdapter(new ArrayAdapter<KaratEnum>(getActivity(), android.R.layout.simple_spinner_dropdown_item, KaratEnum.values()));


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIconView.setImageResource(Utility.getArtResourceForMetal(mMetalId));
        String friendlyDateText = Utility.getFriendlyDayString(getActivity(), mDate);
        mFriendlyDateView.setText(friendlyDateText);

        String preferredCurrency = Utility.getPreferredCurrency(getActivity());

        if(Utility.isGrams(getActivity())) {
            mRadioGram.setSelected(true);
        }
        else {
            mRadioTroy.setSelected(true);
        }

        String rate = Utility.getFormattedCurrency(mMetalPrice, preferredCurrency, getActivity(), true);
        mRateView.setText(rate);
        mRateUnitView.setText("("+Utility.getWeightName(Utility.isGrams(getActivity()), getActivity())+")");

        String metalName = Utility.getMetalName(mMetalId, getActivity());
//        mExchangeRate = getActivity().getString(R.string.app_name)+"("+friendlyDateText+" ["+Utility.getWeightName(Utility.isGrams(getActivity()), getActivity())+"])"+":\n"+
//                "------"+metalName+"----"+"\n"+
//                rate +"\n"+
//                otherRate1 +"\n"+
//                otherRate2 +"\n"+
//                otherRate3;

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareExchangeRatesIntent());
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.calculatefragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mExchangeRate != null) {
            mShareActionProvider.setShareIntent(createShareExchangeRatesIntent());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareExchangeRatesIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mExchangeRate);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}