package com.trianonotbor;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ItemTovsheet} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapterTovWithOneBarcode extends RecyclerView.Adapter<MyItemRecyclerViewAdapterTovWithOneBarcode.ViewHolder> implements AdapterView.OnItemClickListener {

    private final List<ItemTovsheet> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    Context ctx;
    View mView;


    private final FragmentTovWithOneBarcode.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterTovWithOneBarcode(List<ItemTovsheet> items, FragmentTovWithOneBarcode.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        //mContext
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_otbor, parent, false);
        ctx=parent.getContext();
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mtovID.setText(mValues.get(position).tovID);
        holder.mtovName.setText(mValues.get(position).tovName);
        holder.tovKol.setText(mValues.get(position).tovKol);
        holder.tovSection.setText(mValues.get(position).tovSection);

        holder.tovSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                Toast.makeText(activity, "123", Toast.LENGTH_SHORT).show();
                FragmentInvent myFragment = new FragmentInvent();
                myFragment.setIdtov(mValues.get(position).tovID);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();

            }


        });

        holder.tovIDL.setText(mValues.get(position).tovIDL);
        holder.tovNomenkl.setText(mValues.get(position).tovNomenkl);
        holder.mCheckBox.setChecked(mValues.get(position).box);
        holder.tovKor.setText(mValues.get(position).tovKor);
        holder.tovBarcodes.setText(mValues.get(position).tovBARCODES);
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValues.get(position).box=!mValues.get(position).box;
            }
        });

        holder.tovKol.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged (CharSequence s, int start, int before, int count )
            {
                mValues.get(position).tovKol=s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.mtovName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentInvent myFragment = new FragmentInvent();
                myFragment.setIdtov(mValues.get(position).tovID);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();

            }


        });


    }

    /*
    public void switchContent(int id, Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }


    private void fragmentJump(Feed mItemSelected) {
        FragmentTovcard mFragment = new FragmentTovcard();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("item_selected_key", mItemSelected);
        mFragment.setArguments(mBundle);
        //switchContent(R.id.fragment_container_view_tag, mFragment);
    }*/


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ArrayList<ItemTovsheet> getBox()
    {
        ArrayList<ItemTovsheet> box = new ArrayList<ItemTovsheet>();
        for ( ItemTovsheet p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemTovsheet mItem;
        public final View mView;
        public final TextView mtovID, mtovName, tovIDL, tovSection, tovNomenkl, tovKor, tovBarcodes;
        public final EditText tovKol;
        public final CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mtovID = (TextView) view.findViewById(R.id.tovID);
            mtovName = (TextView) view.findViewById(R.id.tovName);
            tovIDL=(TextView) view.findViewById(R.id.tovIDL);
            tovSection=(TextView) view.findViewById(R.id.tovSection);
            tovNomenkl=(TextView) view.findViewById(R.id.tovNomenkl);
            tovKol = (EditText) view.findViewById(R.id.tovKol);
            tovKor = (TextView) view.findViewById(R.id.tovKor);
            tovBarcodes = (TextView) view.findViewById(R.id.tovBARCODES);
            mCheckBox = (CheckBox) view.findViewById(R.id.cbBox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tovSection.getText() + "'";
        }
    }

}