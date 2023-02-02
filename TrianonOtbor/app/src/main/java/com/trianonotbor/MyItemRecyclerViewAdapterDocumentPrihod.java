package com.trianonotbor;

import android.graphics.Color;
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
public class MyItemRecyclerViewAdapterDocumentPrihod extends RecyclerView.Adapter<MyItemRecyclerViewAdapterDocumentPrihod.ViewHolder> {

    private final List<ItemDocumentPrihod> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;

    private final FragmentDocumentPrihod.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterDocumentPrihod(List<ItemDocumentPrihod> items, FragmentDocumentPrihod.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_document_prihod, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        //holder.docum_tovID.setText(mValues.get(position).tovID);
        holder.docum_prihod_tovName.setText(mValues.get(position).tovName);
        holder.docum_prihod_tovBarcode.setText(mValues.get(position).tovBarcode);     //// 25-02-2022
        //holder.docum_prihod_tovIDL.setText(mValues.get(position).tovIDL);            //// 25-02-2022
        // holder.docum_prihod_tovSheet.setText(mValues.get(position).tovSheet);        //// 25-02-2022

        holder.docum_prihod_tovSector.setText(mValues.get(position).tovSector);
        holder.docum_prihod_kol1.setText(mValues.get(position).tovKol );


        holder.mCheckBox.setChecked(mValues.get(position).box2);


        if(mValues.get(position).box2)
        {
            holder.mCheckBox.setBackgroundColor(Color.CYAN);
        }
        else
            holder.mCheckBox.setBackgroundColor(Color.WHITE);


        if(mValues.get(position).box)
        {
            holder.mCheckBox.setBackgroundColor(Color.GREEN);
            holder.itemView.setBackgroundColor(Color.GREEN);
        }

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValues.get(position).box=!mValues.get(position).box;
            }
        });


        holder.docum_prihod_tovName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentTovProps myFragment = new FragmentTovProps();
                myFragment.setIDTOV(mValues.get(position).tovID);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }


        });

        holder.docum_prihod_tovSheet.setOnClickListener(new View.OnClickListener() {
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

    public ArrayList<ItemDocumentPrihod> getBox()
    {
        ArrayList<ItemDocumentPrihod> box = new ArrayList<ItemDocumentPrihod>();
        for ( ItemDocumentPrihod p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public ArrayList<ItemDocumentPrihod> getBox2()
    {
        ArrayList<ItemDocumentPrihod> box = new ArrayList<ItemDocumentPrihod>();
        for ( ItemDocumentPrihod p : mValues)
        {
            if(p.box2)
                box.add(p);
        }
        return box;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemDocumentPrihod mItem;
        public final View mView;
        public final TextView docum_prihod_tovName,docum_prihod_tovBarcode,docum_prihod_tovIDL,docum_prihod_tovSector, docum_prihod_tovSheet, docum_prihod_kol1; //, docum_tovPrice;
        public final CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //docum_tovID = (TextView) view.findViewById(R.id.docum_tovID);
            docum_prihod_tovName = (TextView) view.findViewById(R.id.docum_prihod_tovName);
            docum_prihod_tovBarcode=(TextView) view.findViewById(R.id.docum_prihod_tovBarcode);
            docum_prihod_tovIDL=(TextView) view.findViewById(R.id.docum_prihod_tovIDL);
            docum_prihod_kol1=(TextView) view.findViewById(R.id.docum_prihod_kol1);
            docum_prihod_tovSector=(TextView) view.findViewById(R.id.docum_prihod_tovSector);
            docum_prihod_tovSheet=(TextView) view.findViewById(R.id.docum_prihod_tovSheet);
            mCheckBox = (CheckBox) view.findViewById(R.id.docum_prihod_cbBox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + docum_prihod_tovName.getText() + "'";
        }
    }






}
