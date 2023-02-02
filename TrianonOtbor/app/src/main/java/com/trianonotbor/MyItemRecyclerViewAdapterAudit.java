package com.trianonotbor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ItemTovsheet} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapterAudit extends RecyclerView.Adapter<MyItemRecyclerViewAdapterAudit.ViewHolder> {

    private final List<ItemAudit> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;
    Context myctx;

    private final FragmentAudit.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterAudit(List<ItemAudit> items, FragmentAudit.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myctx=parent.getContext();
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_audit, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.auditHEAD.setText(mValues.get(position).auditHEAD);
        holder.auditTEXT.setText(mValues.get(position).auditTEXT);


      /*  holder.tripID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                //FragmentPhoto myFragment = new FragmentPhoto();
                FragmentOrders myFragment = new FragmentOrders();
                try {
                    myFragment.setIdTrip(mValues.get(position).tripID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String PREF_CURRENT_SHEET = "current_sheet";

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myctx);
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putString(PREF_CURRENT_SHEET, mValues.get(position).tripID);
                prefEditor.apply();

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public ArrayList<ItemAudit> getBox()
    {
        ArrayList<ItemAudit> box = new ArrayList<ItemAudit>();
        for ( ItemAudit p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemAudit mItem;
        public final View mView;
        public final TextView auditHEAD,  auditTEXT;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            auditHEAD = (TextView) view.findViewById(R.id.auditHEAD);
            auditTEXT=(TextView) view.findViewById(R.id.auditTEXT);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + auditHEAD.getText() + "'";
        }
    }



}
