package com.trianonotbor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ItemTovsheet} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapterTovsheets extends RecyclerView.Adapter<MyItemRecyclerViewAdapterTovsheets.ViewHolder> {

    private final List<ItemTovsheets> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;
    Context myctx;

    private final FragmentTovsheets.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterTovsheets(List<ItemTovsheets> items, FragmentTovsheets.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myctx=parent.getContext();
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_tovsheets, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.sheetID.setText(mValues.get(position).sheetID + "(" + mValues.get(position).sheetTRIP + ")");
        holder.sheetORG.setText(mValues.get(position).sheetORG);
        holder.sheetDPICK.setText(mValues.get(position).sheetDPICK);
        holder.sheetSECTOR.setText(mValues.get(position).sheetSECTOR);

        holder.sheetID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentTovsheet myFragment = new FragmentTovsheet();

                myFragment.setIdTovsheet(mValues.get(position).sheetID);
                final String PREF_CURRENT_SHEET = "current_sheet";

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myctx);
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putString(PREF_CURRENT_SHEET, mValues.get(position).sheetID);
                prefEditor.apply();


                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public ArrayList<ItemTovsheets> getBox()
    {
        ArrayList<ItemTovsheets> box = new ArrayList<ItemTovsheets>();
        for ( ItemTovsheets p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemTovsheets mItem;
        public final View mView;
        public final TextView sheetID,  sheetORG, sheetDPICK, sheetSECTOR;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            sheetID = (TextView) view.findViewById(R.id.sheetID);
            sheetORG=(TextView) view.findViewById(R.id.sheetORG);
            sheetDPICK=(TextView) view.findViewById(R.id.sheetDPICK);
            sheetSECTOR=(TextView) view.findViewById(R.id.sheetSECTOR);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + sheetID.getText() + "'";
        }
    }






}
