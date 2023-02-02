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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ItemTovsheet} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapterTrips extends RecyclerView.Adapter<MyItemRecyclerViewAdapterTrips.ViewHolder> {

    private final List<ItemTrips> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;
    Context myctx;

    private final FragmentTrips.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterTrips(List<ItemTrips> items, FragmentTrips.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myctx=parent.getContext();
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_trips, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.tripID.setText(mValues.get(position).tripID + "(" + mValues.get(position).tripTRIP + ")");
        holder.tripDSTART.setText(mValues.get(position).tripDSTART);
        holder.tripKG.setText(mValues.get(position).tripKG + " кг");
        holder.tripM3.setText(mValues.get(position).tripM3 + " м3");
        holder.tripLINES.setText(mValues.get(position).tripM3 + " строк");

        holder.tripID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                //FragmentPhoto myFragment = new FragmentPhoto();
                FragmentOrders myFragment = new FragmentOrders();
                try {
                    myFragment.setIdTrip(mValues.get(position).tripID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


    public ArrayList<ItemTrips> getBox()
    {
        ArrayList<ItemTrips> box = new ArrayList<ItemTrips>();
        for ( ItemTrips p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemTrips mItem;
        public final View mView;
        public final TextView tripID,  tripDSTART, tripKG, tripM3, tripLINES;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tripID = (TextView) view.findViewById(R.id.tripID);
            tripDSTART=(TextView) view.findViewById(R.id.tripDSTART);
            tripKG=(TextView) view.findViewById(R.id.tripKG);
            tripM3=(TextView) view.findViewById(R.id.tripM3);
            tripLINES=(TextView) view.findViewById(R.id.tripLINES);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tripID.getText() + "'";
        }
    }



}
