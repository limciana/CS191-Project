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
 * Rayven Ely Cruz      3/22/18  Added methods
 */

/*
 * File Creation Date: 3/18/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */
package com.cs192.upcc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.cs192.upcc.dummy.DummyContent;
//import com.cs192.upcc.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ViewSubjectFragment extends Fragment {

     // TODO: Customize parameter argument names
     private static final String ARG_COLUMN_COUNT = "column-count"; //column count
     // TODO: Customize parameters
     private int mColumnCount = 1;  //one column list
     private OnListFragmentInteractionListener mListener; //listener
     private OnDataPass dataPasser; //for passing of data

     /**
      * Mandatory empty constructor for the fragment manager to instantiate the
      * fragment (e.g. upon screen orientation changes).
      */
      /*
     * Name: ViewSubjectFragment
     * Creation Date: 3/18/18
     * Purpose: Required contstructo
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public ViewSubjectFragment() {
     }


     /*
     * Name: onDataPass
     * Creation Date: 3/22/18
     * Purpose: interface to pass data to activity
     * Arguments:
     *      nonde
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public interface OnDataPass {
          public void onTitlePass(String data);
     }

     /*
     * Name: passTitle
     * Creation Date: 3/22/18
     * Purpose: Handles the strings being passed through the interfaces
     * Arguments:
     *      data - the title string
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public void passTitle(String data) {

          dataPasser.onTitlePass(data);
     }


     /*
     * Name: newInstance
     * Creation Date: 3/18/18
     * Purpose: Required contstructor
     * Arguments:
     *      columnCount
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @SuppressWarnings("unused")
     public static ViewSubjectFragment newInstance(int columnCount) {
          ViewSubjectFragment fragment = new ViewSubjectFragment();
          Bundle args = new Bundle();
          args.putInt(ARG_COLUMN_COUNT, columnCount);
          fragment.setArguments(args);
          return fragment;
     }

     /*
     * Name: onCreate
     * Creation Date: 3/18/18
     * Purpose: required function
     * Arguments:
     *      savedInstanceState
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @Override
     public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          if (getArguments() != null) {
               mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
          }
     }
     /*
     * Name: onCreateView
     * Creation Date: 3/18/18
     * Purpose: Required contstructor, setups the fragment
     * Arguments:
     *      inflater, container, savedInstaceState
     * Other Requirements:
     *      none
     * Return Value: View
     */
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
          View view = inflater.inflate(R.layout.fragment_subject_list, container, false);
          passTitle("Subjects");
          // Set the adapter
          if (view instanceof RecyclerView) {
               Context context = view.getContext();
               RecyclerView recyclerView = (RecyclerView) view;
               if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
               } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
               }
               recyclerView.setAdapter(new MySubjectRecyclerViewAdapter(((MainDrawer) getActivity()).getResult(), mListener));
          }
          return view;
     }

     /*
     * Name: onAttach
     * Creation Date: 3/18/18
     * Purpose: Required function
     * Arguments:
     *      context
     * Other Requirements:
     *      none
     * Return Value: void
     */

     @Override
     public void onAttach(Context context) {
          super.onAttach(context);
          if (context instanceof OnListFragmentInteractionListener) {
               mListener = (OnListFragmentInteractionListener) context;
          } else {
               throw new RuntimeException(context.toString()
                       + " must implement OnListFragmentInteractionListener");
          }
          dataPasser = (OnDataPass) context;
     }
     /*
    * Name: onDetach
    * Creation Date: 3/18/18
    * Purpose: Required method
    * Arguments:
    *      none
    * Other Requirements:
    *      none
    * Return Value: void
    */
     @Override
     public void onDetach() {
          super.onDetach();
          mListener = null;
     }

     /*
     * Name: onListFragmentInteractionListener
     * Creation Date: 3/18/18
     * Purpose: listener for this fragment
     */
     public interface OnListFragmentInteractionListener {
          // TODO: Update argument type and name
          void onListFragmentInteraction(Subject subject);
     }
}
