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

import com.cs192.upcc.dummy.DummyContent;
import com.cs192.upcc.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ViewSubjectFragment extends Fragment {

     // TODO: Customize parameter argument names
     private static final String ARG_COLUMN_COUNT = "column-count";
     // TODO: Customize parameters
     private int mColumnCount = 1;
     private OnListFragmentInteractionListener mListener;
     private OnDataPass dataPasser;
     /**
      * Mandatory empty constructor for the fragment manager to instantiate the
      * fragment (e.g. upon screen orientation changes).
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


     // TODO: Customize parameter initialization
     @SuppressWarnings("unused")
     public static ViewSubjectFragment newInstance(int columnCount) {
          ViewSubjectFragment fragment = new ViewSubjectFragment();
          Bundle args = new Bundle();
          args.putInt(ARG_COLUMN_COUNT, columnCount);
          fragment.setArguments(args);
          return fragment;
     }

     @Override
     public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          if (getArguments() != null) {
               mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
          }
     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
          View view = inflater.inflate(R.layout.fragment_subject_list, container, false);
          passTitle("Subjects that can be taken");
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

     @Override
     public void onDetach() {
          super.onDetach();
          mListener = null;
     }

     /**
      * This interface must be implemented by activities that contain this
      * fragment to allow an interaction in this fragment to be communicated
      * to the activity and potentially other fragments contained in that
      * activity.
      * <p/>
      * See the Android Training lesson <a href=
      * "http://developer.android.com/training/basics/fragments/communicating.html"
      * >Communicating with Other Fragments</a> for more information.
      */
     public interface OnListFragmentInteractionListener {
          // TODO: Update argument type and name
          void onListFragmentInteraction(Subject subject);
     }
}
