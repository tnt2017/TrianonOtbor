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
public class MyItemRecyclerViewAdapterZakazTovs extends RecyclerView.Adapter<MyItemRecyclerViewAdapterZakazTovs.ViewHolder> {

    private final List<ItemTovs> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;
    Context myctx;

    private final FragmentZakazTovs.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterZakazTovs(List<ItemTovs> items, FragmentZakazTovs.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myctx=parent.getContext();
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_zakaz_tovs, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.tripID.setText(mValues.get(position).tovCMC + "(" + mValues.get(position).tovNAME + ")" + mValues.get(position).tovGROUP_FLAG);
        holder.tripDSTART.setText(mValues.get(position).tovGROUP_FLAG);
        /*holder.tripKG.setText(mValues.get(position).tripKG + " кг");
        holder.tripM3.setText(mValues.get(position).tripM3 + " м3");
        holder.tripLINES.setText(mValues.get(position).tripM3 + " строк");*/

        holder.tripID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();

                if(mValues.get(position).tovGROUP_FLAG.equals("0"))
                {

                }

                FragmentZakaz myFragment = new FragmentZakaz();
                try
                {
                    myFragment.setIdTrip(mValues.get(position).tovCMC);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                final String PREF_CURRENT_GRP = "current_grp";

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myctx);
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putString(PREF_CURRENT_GRP, mValues.get(position).tovCMC);
                prefEditor.apply();

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public ArrayList<ItemTovs> getBox()
    {
        ArrayList<ItemTovs> box = new ArrayList<ItemTovs>();
        for ( ItemTovs p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemTovs mItem;
        public final View mView;
        public final TextView tripID,  tripDSTART;//, tripKG, tripM3, tripLINES;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tripID = (TextView) view.findViewById(R.id.zakaz_tovs_id);
            tripDSTART=(TextView) view.findViewById(R.id.zakaz_tovs_name);
            /*tripKG=(TextView) view.findViewById(R.id.zakazKG);
            tripM3=(TextView) view.findViewById(R.id.zakazM3);
            tripLINES=(TextView) view.findViewById(R.id.zakazLINES);*/
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tripID.getText() + "'";
        }
    }



}
