package com.trianonotbor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
public class MyItemRecyclerViewAdapterZakaz extends RecyclerView.Adapter<MyItemRecyclerViewAdapterZakaz.ViewHolder> {

    private final List<ItemTovs> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;
    Context myctx;
    SharedPreferences settings;

    private final FragmentZakaz.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterZakaz(List<ItemTovs> items, FragmentZakaz.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myctx=parent.getContext();
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_zakaz, parent, false);
        return new ViewHolder(mView);
    }

    private void showForgotDialog(Context c, String tovid, String tovname) {
        final EditText taskEditText = new EditText(c);

        taskEditText.setText("0");
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Введите количество товара?")
                .setMessage("ID=" + tovid)
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String tovkol = String.valueOf(taskEditText.getText());
                        Toast.makeText(c, "Добавляем в корзину: " + tovid + ":" + tovkol + ":" + tovname, Toast.LENGTH_SHORT).show();

                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
                        String basket=settings.getString(Utils.PREF_BASKET,"");

                        SharedPreferences.Editor prefEditor = settings.edit();
                        prefEditor.putString(Utils.PREF_BASKET, basket + "\r\n" + tovid + ":" + tovkol + ":" + tovname );
                        prefEditor.apply();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.tripID.setText(mValues.get(position).tovNAME + "(" + mValues.get(position).tovCMC + ")");
        holder.tripDSTART.setText(mValues.get(position).tovGROUP_FLAG);
        holder.tripID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(mValues.get(position).tovGROUP_FLAG.equals("0"))
                {

                    AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                    FragmentZakaz myFragment = new FragmentZakaz();
                    try {
                        myFragment.setIdTrip(mValues.get(position).tovCMC);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String PREF_CURRENT_GRP = "current_grp";

                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myctx);
                    SharedPreferences.Editor prefEditor = settings.edit();
                    prefEditor.putString(PREF_CURRENT_GRP, mValues.get(position).tovCMC);
                    prefEditor.apply();

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                }
                else
                {
                    showForgotDialog(mView.getContext(), mValues.get(position).tovCMC, mValues.get(position).tovNAME);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public ArrayList<ItemTovs> getBox()
    {
        ArrayList<ItemTovs> box = new ArrayList<ItemTovs>();
        for ( ItemTovs p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemTovs mItem;
        public final View mView;
        public final TextView tripID,  tripDSTART;//, tripKG, tripM3, tripLINES;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tripID = (TextView) view.findViewById(R.id.zakazID);
            tripDSTART=(TextView) view.findViewById(R.id.zakazDSTART);
            /*tripKG=(TextView) view.findViewById(R.id.zakazKG);
            tripM3=(TextView) view.findViewById(R.id.zakazM3);
            tripLINES=(TextView) view.findViewById(R.id.zakazLINES);*/
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tripID.getText() + "'";
        }
    }



}
