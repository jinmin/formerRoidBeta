package com.jinmin.formerroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapLayerFragment extends Fragment {

   public static final String ARG_SECTION_NUMBER = "section_number";

   public MapLayerFragment() {
   }

   public static Fragment newInstance(Context context) {
      MapLayerFragment f = new MapLayerFragment();
      return f;
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.map_layout, null);
      return viewGroup;
   }
}
