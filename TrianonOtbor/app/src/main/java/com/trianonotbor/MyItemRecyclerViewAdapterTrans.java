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
public class MyItemRecyclerViewAdapterTrans extends RecyclerView.Adapter<MyItemRecyclerViewAdapterTrans.ViewHolder> {

    private final List<ItemTrans> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;

    private final FragmentTrans.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterTrans(List<ItemTrans> items, FragmentTrans.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_trans, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        //holder.docum_tovID.setText(mValues.get(position).tovID);
        holder.trans_tovSheet.setText(mValues.get(position).tovSheet);
        holder.trans_tovSheetInfo.setText(mValues.get(position).tovBarcode);
        holder.trans_tovSector.setText(mValues.get(position).tovSector);
        holder.trans_kol1.setText(mValues.get(position).tovKol );
        holder.trans_Org.setText( mValues.get(position).tovOrg );

        holder.mCheckBox.setChecked(mValues.get(position).box);
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValues.get(position).box=!mValues.get(position).box;
            }
        });


        holder.trans_tovSheet.setOnClickListener(new View.OnClickListener() {
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

    public ArrayList<ItemTrans> getBox()
    {
        ArrayList<ItemTrans> box = new ArrayList<ItemTrans>();
        for ( ItemTrans p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemTrans mItem;
        public final View mView;
        public final TextView trans_tovSheet,trans_tovSheetInfo,trans_tovSector,  trans_kol1, trans_Org; //, docum_tovPrice;
        public final CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //docum_tovID = (TextView) view.findViewById(R.id.docum_tovID);
            trans_tovSheet = (TextView) view.findViewById(R.id.trans_tovSheet);
            trans_tovSheetInfo=(TextView) view.findViewById(R.id.trans_tovSheetInfo);
            trans_kol1=(TextView) view.findViewById(R.id.trans_kol1);
            trans_tovSector=(TextView) view.findViewById(R.id.trans_tovSector);
            mCheckBox = (CheckBox) view.findViewById(R.id.trans_cbBox);
            trans_Org=(TextView) view.findViewById(R.id.trans_Org);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + trans_tovSheet.getText() + "'";
        }
    }






}
