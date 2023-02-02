package com.trianonotbor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ItemTovsheet} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapterHistSection extends RecyclerView.Adapter<MyItemRecyclerViewAdapterHistSection.ViewHolder> {

    private final List<ItemHistSection> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;
    Context myctx;

    private final FragmentHistSection.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterHistSection(List<ItemHistSection> items, FragmentHistSection.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myctx=parent.getContext();
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_histsection, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.histsec_header.setText(mValues.get(position).tripID + "(" + mValues.get(position).tripTRIP + ")");
        holder.histsec_descr.setText(mValues.get(position).tripDSTART);

        holder.histsec_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentTovProps myFragment = new FragmentTovProps();
                myFragment.setIDTOV(mValues.get(position).tripID);
                final String PREF_CURRENT_SHEET = "current_sheet";

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myctx);
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putString(PREF_CURRENT_SHEET, mValues.get(position).tripID);
                prefEditor.apply();


                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public ArrayList<ItemHistSection> getBox()
    {
        ArrayList<ItemHistSection> box = new ArrayList<ItemHistSection>();
        for ( ItemHistSection p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemHistSection mItem;
        public final View mView;
        public final TextView histsec_header, histsec_descr;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            histsec_header = (TextView) view.findViewById(R.id.histsec_header);
            histsec_descr=(TextView) view.findViewById(R.id.histsec_descr);
         }

        @Override
        public String toString() {
            return super.toString() + " '" + histsec_header.getText() + "'";
        }
    }



}
