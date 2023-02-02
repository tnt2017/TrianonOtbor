package com.trianonotbor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
public class MyItemRecyclerViewAdapterComplektovka extends RecyclerView.Adapter<MyItemRecyclerViewAdapterComplektovka.ViewHolder> {

    private final List<ItemComplektovka> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;

    private final FragmentComplektovka.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterComplektovka(List<ItemComplektovka> items, FragmentComplektovka.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_complektovka, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        //holder.docum_tovID.setText(mValues.get(position).tovID);
        holder.complektovka_tovSheet.setText(mValues.get(position).tovSheet);
        holder.complektovka_tovSheetInfo.setText(mValues.get(position).tovBarcode);
        holder.complektovka_tovSector.setText(mValues.get(position).tovSector);
        holder.complektovka_kol1.setText(mValues.get(position).tovKol );


        holder.mCheckBox.setChecked(mValues.get(position).box);
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValues.get(position).box=!mValues.get(position).box;
            }
        });


        holder.complektovka_tovSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentTovsheet myFragment = new FragmentTovsheet();
                myFragment.setIdTovsheet(mValues.get(position).tovSheet);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }


        });




    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ArrayList<ItemComplektovka> getBox()
    {
        ArrayList<ItemComplektovka> box = new ArrayList<ItemComplektovka>();
        for ( ItemComplektovka p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemComplektovka mItem;
        public final View mView;
        public final TextView complektovka_tovSheet,complektovka_tovSheetInfo,complektovka_tovSector,  complektovka_kol1; //, docum_tovPrice;
        public final CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //docum_tovID = (TextView) view.findViewById(R.id.docum_tovID);
            complektovka_tovSheet = (TextView) view.findViewById(R.id.complektovka_tovSheet);
            complektovka_tovSheetInfo=(TextView) view.findViewById(R.id.complektovka_tovSheetInfo);
            complektovka_kol1=(TextView) view.findViewById(R.id.complektovka_kol1);
            complektovka_tovSector=(TextView) view.findViewById(R.id.complektovka_tovSector);
            mCheckBox = (CheckBox) view.findViewById(R.id.complektovka_cbBox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + complektovka_tovSheet.getText() + "'";
        }
    }






}
