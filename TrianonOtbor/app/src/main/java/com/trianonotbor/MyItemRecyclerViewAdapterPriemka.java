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
public class MyItemRecyclerViewAdapterPriemka extends RecyclerView.Adapter<MyItemRecyclerViewAdapterPriemka.ViewHolder> {

    private final List<ItemPriemka> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;

    private final FragmentPriemka.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterPriemka(List<ItemPriemka> items, FragmentPriemka.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_priemka, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.priemkaID.setText(mValues.get(position).sheetORG + "(" + mValues.get(position).sheetTRIP + ")");
        holder.priemkaORG.setText(mValues.get(position).sheetID);
        holder.priemkaDPICK.setText(mValues.get(position).sheetDPICK);
        holder.priemkaCHECKER.setText(mValues.get(position).sheetCHECKER);
        holder.priemkaKOR.setText(mValues.get(position).sheetKOR + "кор");
        holder.priemkaSUMMA.setText(mValues.get(position).sheetSUMMA + "руб");


        holder.priemkaID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentDocumentPrihod myFragment = new FragmentDocumentPrihod();
                myFragment.setIdDoc(mValues.get(position).sheetID);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public ArrayList<ItemPriemka> getBox()
    {
        ArrayList<ItemPriemka> box = new ArrayList<ItemPriemka>();
        for ( ItemPriemka p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemPriemka mItem;
        public final View mView;
        public final TextView priemkaID,  priemkaORG, priemkaDPICK, priemkaCHECKER, priemkaKOR, priemkaSUMMA;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            priemkaID = (TextView) view.findViewById(R.id.priemkaID);
            priemkaORG=(TextView) view.findViewById(R.id.priemkaORG);
            priemkaDPICK=(TextView) view.findViewById(R.id.priemkaDPICK);
            priemkaCHECKER=(TextView) view.findViewById(R.id.priemkaCHECKER);
            priemkaKOR=(TextView) view.findViewById(R.id.priemkaKOR);
            priemkaSUMMA=(TextView) view.findViewById(R.id.priemkaSUMMA);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + priemkaID.getText() + "'";
        }
    }
}
