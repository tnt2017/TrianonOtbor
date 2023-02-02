package com.trianonotbor;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
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
public class MyItemRecyclerViewAdapterTovcard extends RecyclerView.Adapter<MyItemRecyclerViewAdapterTovcard.ViewHolder> {

    private final List<ItemTovcard> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;


    private final FragmentTovcard.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterTovcard(List<ItemTovcard> items, FragmentTovcard.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_tovcard, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        holder.docID.setText(mValues.get(position).docID + " (" + mValues.get(position).docNNAKL + ")");

        if(mValues.get(position).docTYP.equals("Прих"))
          holder.docID.setTextColor(Color.RED);

        holder.docDAT.setText(mValues.get(position).docDAT);
        holder.docNNAKL.setText(mValues.get(position).docTXT);
        holder.docEMP.setText(mValues.get(position).docEMP);
        holder.docKOL.setText(mValues.get(position).docKol);

        holder.mCheckBox.setChecked(mValues.get(position).box);

        holder.docID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentDocument myFragment = new FragmentDocument();
                myFragment.setIdDoc(mValues.get(position).docID);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }
        });



        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValues.get(position).box=!mValues.get(position).box;
            }
        });

        holder.docKOL.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged (CharSequence s, int start, int before, int count )
            {
                mValues.get(position).docKol=s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ArrayList<ItemTovcard> getBox()
    {
        ArrayList<ItemTovcard> box = new ArrayList<ItemTovcard>();
        for ( ItemTovcard p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemTovcard mItem;
        public final View mView;
        public final TextView  docID, docDAT, docNNAKL, docEMP, docKOL ;
        public final CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            docID =(TextView) view.findViewById(R.id.docID);
            docDAT = (TextView) view.findViewById(R.id.docDAT);
            docNNAKL=(TextView) view.findViewById(R.id.docNNAKL);
            docEMP = (TextView) view.findViewById(R.id.docEMP);
            docKOL = (EditText) view.findViewById(R.id.docKol);
            mCheckBox = (CheckBox) view.findViewById(R.id.cbBox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + docID.getText() + "'";
        }
    }






}
