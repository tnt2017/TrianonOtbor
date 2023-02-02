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
public class MyItemRecyclerViewAdapterPoisk extends RecyclerView.Adapter<MyItemRecyclerViewAdapterPoisk.ViewHolder> {

    private final List<ItemPoisk> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;

    private final FragmentPoisk.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterPoisk(List<ItemPoisk> items, FragmentPoisk.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_poisk, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.poiskID.setText(mValues.get(position).docID + "(" + mValues.get(position).docNNAKL + ")");
        holder.poiskORG.setText(mValues.get(position).sheetID);
        holder.poiskDPICK.setText(mValues.get(position).sheetDPICK);
        holder.poiskCHECKER.setText(mValues.get(position).sheetCHECKER);
        holder.poiskKOR.setText(mValues.get(position).sheetKOR);
        holder.poiskSUMMA.setText(mValues.get(position).sheetSUMMA + "руб");

        holder.poiskID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nnakl=mValues.get(position).docNNAKL;

                if(mValues.get(position).docNNAKL.indexOf("впк")>-1)
                {
                    AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                    FragmentDocumentPrihod myFragment = new FragmentDocumentPrihod();
                    myFragment.setIdDoc(mValues.get(position).docID);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                }
                else
                {
                    AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                    FragmentDocument myFragment = new FragmentDocument();
                    myFragment.setIdDoc(mValues.get(position).docID);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public ArrayList<ItemPoisk> getBox()
    {
        ArrayList<ItemPoisk> box = new ArrayList<ItemPoisk>();
        for ( ItemPoisk p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemPoisk mItem;
        public final View mView;
        public final TextView poiskID,  poiskORG, poiskDPICK, poiskCHECKER, poiskKOR, poiskSUMMA;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            poiskID = (TextView) view.findViewById(R.id.poiskID);
            poiskORG=(TextView) view.findViewById(R.id.poiskORG);
            poiskDPICK=(TextView) view.findViewById(R.id.poiskDPICK);
            poiskCHECKER=(TextView) view.findViewById(R.id.poiskCHECKER);
            poiskKOR=(TextView) view.findViewById(R.id.poiskKOR);
            poiskSUMMA=(TextView) view.findViewById(R.id.poiskSUMMA);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + poiskID.getText() + "'";
        }
    }
}
