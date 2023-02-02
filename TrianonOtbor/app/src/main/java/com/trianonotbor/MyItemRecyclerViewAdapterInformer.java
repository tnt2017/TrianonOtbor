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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ItemTovsheet} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapterInformer extends RecyclerView.Adapter<MyItemRecyclerViewAdapterInformer.ViewHolder> {

    private final List<ItemInformer> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;
    Context myctx;

    private final FragmentInformer.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterInformer(List<ItemInformer> items, FragmentInformer.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myctx=parent.getContext();
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_informer, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.informerID.setText(mValues.get(position).informerID + "->" + mValues.get(position).informerTRIP);
        holder.informerDSTART.setText(mValues.get(position).informerDSTART);
        holder.informerKG.setText(mValues.get(position).informerKG + " кг");
        holder.informerM3.setText(mValues.get(position).informerM3 + " м3");
        holder.informerLINES.setText(mValues.get(position).informerM3 + " строк");
        holder.informerDSTAMP.setText(mValues.get(position).informerDSTAMP);
        holder.informerID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentInformerBranch myFragment = new FragmentInformerBranch();
                try {
                    myFragment.setIdTrip(mValues.get(position).informerDSTAMP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String PREF_CURRENT_SHEET = "current_sheet";

               /* SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myctx);
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putString(PREF_CURRENT_SHEET, mValues.get(position).informerID);
                prefEditor.apply();*/

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public ArrayList<ItemInformer> getBox()
    {
        ArrayList<ItemInformer> box = new ArrayList<ItemInformer>();
        for ( ItemInformer p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemInformer mItem;
        public final View mView;
        public final TextView informerID,  informerDSTART, informerKG, informerM3, informerLINES, informerDSTAMP;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            informerID = (TextView) view.findViewById(R.id.informerID);
            informerDSTART=(TextView) view.findViewById(R.id.informerDSTART);
            informerKG=(TextView) view.findViewById(R.id.informerKG);
            informerM3=(TextView) view.findViewById(R.id.informerM3);
            informerLINES=(TextView) view.findViewById(R.id.informerLINES);
            informerDSTAMP=(TextView) view.findViewById(R.id.informerDSTAMP);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + informerID.getText() + "'";
        }
    }



}
