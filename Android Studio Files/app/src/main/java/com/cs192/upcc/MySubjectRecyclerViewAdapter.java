/*
 * This is a course requirement for CS 192 Software Engineering II
 * under the supervision of Asst. Prof. Ma. Rowena C. Solamo
 * of the Department of Computer Science, College of Engineering,
 * University of the Philippines, Diliman
 * for the AY 2017-2018.
 * This code is written by Rayven Ely Cruz.
 */

/* Code History
 * Programmer           Date     Description
 * Rayven Ely Cruz      3/18/18  Created the fragment.
 * Rayven Ely Cruz      3/22/18  Loaded layout
 */

/*
 * File Creation Date: 3/18/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */

package com.cs192.upcc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs192.upcc.ViewSubjectFragment.OnListFragmentInteractionListener;
// import com.cs192.upcc.dummy.DummyContent.DummyItem;

import org.w3c.dom.Text;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySubjectRecyclerViewAdapter extends RecyclerView.Adapter<MySubjectRecyclerViewAdapter.ViewHolder> {

     private final List<Subject> mValues;  //loaded values
     private final OnListFragmentInteractionListener mListener; //listener
     /*
     * Name: MySubjectRecyclerViewAdapter
     * Creation Date: 3/18/18
     * Purpose: Handles the initialization of the ViewAdapter
     * Arguments:
     *      items, listener
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public MySubjectRecyclerViewAdapter(List<Subject> items, OnListFragmentInteractionListener listener) {
          mValues = items;
          mListener = listener;
     }

     /*
     * Name: onCreateViewHolder
     * Creation Date: 3/18/18
     * Purpose: Handles the initialization of the ViewHolder
     * Arguments:
     *      (ViewGroup parent, int viewType)
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @Override
     public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.fragment_subject, parent, false);
          return new ViewHolder(view);
     }
     /*
     * Name: onBindViewHolder
     * Creation Date: 3/18/18
     * Purpose: Handles the initialization of the ViewHolder
     * Arguments:
     *      final ViewHolder holder, int position
     * Other Requirements:
     *      none
     * Return Value: void
     */
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
     /*
     * Name: getItemCount
     * Creation Date: 3/18/18
     * Purpose: getter for itemcount
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: int
     */
     @Override
     public int getItemCount() {
          return mValues.size();
     }

     public class ViewHolder extends RecyclerView.ViewHolder {
          public final View mView; // view for the layout
          public final TextView mIdView; // view for the layout
          public final TextView mContentView; // view for the layout
          public final TextView mUnitView; // view for the layout
          public final TextView mYear; // view for the layout
          public Subject mItem; // view for the layout

          /*
          * Name: ViewHolder
          * Creation Date: 3/18/18
          * Purpose: ViewHolder class for recycler view
          * Arguments:
          *      none
          * Other Requirements:
          *      none
          * Return Value: void
          */
          public ViewHolder(View view) {
               super(view);
               mView = view;
               mIdView = (TextView) view.findViewById(R.id.id);
               mContentView = (TextView) view.findViewById(R.id.content);
               mUnitView = (TextView) view.findViewById(R.id.units);
               mYear = (TextView) view.findViewById(R.id.year);
          }
          /*
           * Name: toString
           * Creation Date: 3/18/18
           * Purpose: required function
           * Arguments:
           *      none
           * Other Requirements:
           *      none
           * Return Value: String
           */
          @Override
          public String toString() {
               return super.toString() + " '" + mContentView.getText() + "'";
          }
     }
}
