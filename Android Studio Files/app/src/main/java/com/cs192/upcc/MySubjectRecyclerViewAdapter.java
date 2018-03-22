package com.cs192.upcc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs192.upcc.ViewSubjectFragment.OnListFragmentInteractionListener;
import com.cs192.upcc.dummy.DummyContent.DummyItem;

import org.w3c.dom.Text;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySubjectRecyclerViewAdapter extends RecyclerView.Adapter<MySubjectRecyclerViewAdapter.ViewHolder> {

     private final List<Subject> mValues;
     private final OnListFragmentInteractionListener mListener;

     public MySubjectRecyclerViewAdapter(List<Subject> items, OnListFragmentInteractionListener listener) {
          mValues = items;
          mListener = listener;
     }


     @Override
     public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.fragment_subject, parent, false);
          return new ViewHolder(view);
     }

     @Override
     public void onBindViewHolder(final ViewHolder holder, int position) {
          holder.mItem = mValues.get(position);
          holder.mIdView.setText(mValues.get(position).getSubjectName());
          holder.mContentView.setText(mValues.get(position).getSubjectDesc());
          String unitInString = Integer.toString(mValues.get(position).getUnits()) + "u";
          holder.mUnitView.setText(unitInString);
          holder.mYear.setText(mValues.get(position).getYearString());


          holder.mView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    if (null != mListener) {
                         // Notify the active callbacks interface (the activity, if the
                         // fragment is attached to one) that an item has been selected.
                         mListener.onListFragmentInteraction(holder.mItem);
                    }
               }
          });
     }

     @Override
     public int getItemCount() {
          return mValues.size();
     }

     public class ViewHolder extends RecyclerView.ViewHolder {
          public final View mView;
          public final TextView mIdView;
          public final TextView mContentView;
          public final TextView mUnitView;
          public final TextView mYear;
          public Subject mItem;

          public ViewHolder(View view) {
               super(view);
               mView = view;
               mIdView = (TextView) view.findViewById(R.id.id);
               mContentView = (TextView) view.findViewById(R.id.content);
               mUnitView = (TextView) view.findViewById(R.id.units);
               mYear = (TextView) view.findViewById(R.id.year);
          }

          @Override
          public String toString() {
               return super.toString() + " '" + mContentView.getText() + "'";
          }
     }
}
