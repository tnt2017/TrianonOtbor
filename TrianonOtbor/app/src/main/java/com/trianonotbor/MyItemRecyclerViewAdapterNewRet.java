package com.trianonotbor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ItemTovsheet} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapterNewRet extends RecyclerView.Adapter<MyItemRecyclerViewAdapterNewRet.ViewHolder> {

    private final List<ItemDocument> mValues;
    private ArrayList<String> mDataset;
    boolean[] checked;
    View mView;

    private final FragmentNewRet.OnFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapterNewRet(List<ItemDocument> items, FragmentNewRet.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_document, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        //holder.docum_tovID.setText(mValues.get(position).tovID);
        holder.docum_tovName.setText(mValues.get(position).tovName);
        holder.docum_tovKol.setText(mValues.get(position).tovKol + "шт");
        holder.docum_tovPrice.setText(mValues.get(position).tovPrice + "руб");

      /*  holder.tovKol.addTextChangedListener(new TextWatcher()
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
*/
        holder.docum_tovName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*//Activity a = (Activity) ;
                String result = "https://svc.trianon-nsk.ru/tpic/preview.php?src=" + mValues.get(position).tovID;
                //Toast.makeText(a, result, Toast.LENGTH_LONG).show();

                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result)); //IdSheet.getText()
                    v.getContext().startActivity(browserIntent);
                }
                catch (Exception ex)
                {

                }*/

                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                FragmentTovProps myFragment = new FragmentTovProps();
                myFragment.setIDTOV(mValues.get(position).tovID);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            }


        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ArrayList<ItemDocument> getBox()
    {
        ArrayList<ItemDocument> box = new ArrayList<ItemDocument>();
        for ( ItemDocument p : mValues)
        {
            if(p.box)
                box.add(p);
        }
        return box;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemDocument mItem;
        public final View mView;
        public final TextView docum_tovName, docum_tovPrice, docum_tovKol;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //docum_tovID = (TextView) view.findViewById(R.id.docum_tovID);
            docum_tovName = (TextView) view.findViewById(R.id.docum_tovName);
            docum_tovPrice=(TextView) view.findViewById(R.id.docum_tovPrice);
            docum_tovKol=(TextView) view.findViewById(R.id.docum_tovKol);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + docum_tovName.getText() + "'";
        }
    }






}
