package com.trianonotbor;

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
public class MyItemRecyclerViewAdapterStrikeouts extends RecyclerView.Adapter<MyItemRecyclerViewAdapterStrikeouts.ViewHolder> {

    private final List<ItemStrikeout> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;


    private final FragmentStrikeouts.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterStrikeouts(List<ItemStrikeout> items, FragmentStrikeouts.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_strikeout, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.tovID.setText(mValues.get(position).tovID);
        holder.mtovName.setText(mValues.get(position).tovName);
        holder.tovSection.setText(mValues.get(position).tovSection);
        holder.tovNNAKL.setText(mValues.get(position).tovNNAKL);
        holder.tovDSTAMP.setText(mValues.get(position).tovDSTAMP);
        holder.tovEMP.setText(mValues.get(position).tovEMP);
        holder.tovTRIP.setText(mValues.get(position).tovTRIP);


        holder.strikeouts_tovKol.setText(mValues.get(position).strikeouts_tovKol);
        holder.mtovName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentTovProps myFragment = new FragmentTovProps();
                myFragment.setIDTOV(mValues.get(position).tovID);
                //FragmentInvent myFragment = new FragmentInvent();
                //myFragment.setIdtov(mValues.get(position).tovID);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }


        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ArrayList<ItemStrikeout> getBox()
    {
        ArrayList<ItemStrikeout> box = new ArrayList<ItemStrikeout>();
        for ( ItemStrikeout p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemStrikeout mItem;
        public final View mView;
        public final TextView tovID, mtovName, tovSection, tovNNAKL, tovDSTAMP, tovEMP, tovTRIP, strikeouts_tovKol;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tovID=(TextView) view.findViewById(R.id.tovID);
            mtovName = (TextView) view.findViewById(R.id.tovName);
            tovSection = (TextView) view.findViewById(R.id.strikeouts_tovSection);
            tovNNAKL = (TextView) view.findViewById(R.id.tovNNAKL);
            tovDSTAMP = (TextView) view.findViewById(R.id.tovDSTAMP);
            tovEMP= (TextView) view.findViewById(R.id.tovEMP);
            tovTRIP= (TextView) view.findViewById(R.id.tovTRIP);

            strikeouts_tovKol=(TextView) view.findViewById(R.id.strikeouts_tovKol);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tovNNAKL.getText() + "'";
        }
    }






}
