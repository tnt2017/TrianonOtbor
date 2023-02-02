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
public class MyItemRecyclerViewAdapterInformerBranch extends RecyclerView.Adapter<MyItemRecyclerViewAdapterInformerBranch.ViewHolder> {

    private final List<ItemInformerBranch> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;
    Context myctx;

    private final FragmentInformerBranch.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterInformerBranch(List<ItemInformerBranch> items, FragmentInformerBranch.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myctx=parent.getContext();
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_informer_branch, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.informer_branchID.setText(mValues.get(position).informer_branchID + "(" + mValues.get(position).informer_branchTRIP + ")");
        holder.informer_branchDSTART.setText(mValues.get(position).informer_branchDSTART);
        holder.informer_branchKG.setText(mValues.get(position).informer_branchKG + " кг");
        holder.informer_branchM3.setText(mValues.get(position).informer_branchM3 + " м3");

        holder.informer_branchID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentPhoto myFragment = new FragmentPhoto();
                myFragment.setIdTovsheet(mValues.get(position).informer_branchID);
                final String PREF_CURRENT_SHEET = "current_sheet";

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myctx);
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putString(PREF_CURRENT_SHEET, mValues.get(position).informer_branchID);
                prefEditor.apply();

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public ArrayList<ItemInformerBranch> getBox()
    {
        ArrayList<ItemInformerBranch> box = new ArrayList<ItemInformerBranch>();
        for ( ItemInformerBranch p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemInformerBranch mItem;
        public final View mView;
        public final TextView informer_branchID,  informer_branchDSTART, informer_branchKG, informer_branchM3;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            informer_branchID = (TextView) view.findViewById(R.id.informer_branchID);
            informer_branchDSTART=(TextView) view.findViewById(R.id.informer_branchDSTART);
            informer_branchKG=(TextView) view.findViewById(R.id.informer_branchKG);
            informer_branchM3=(TextView) view.findViewById(R.id.informer_branchM3);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + informer_branchID.getText() + "'";
        }
    }



}
