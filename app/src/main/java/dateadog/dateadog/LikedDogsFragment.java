package dateadog.dateadog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LikedDogsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LikedDogsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikedDogsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static final int NUM_COLUMNS = 2;

    private DADAPI dadapi;
    private List<Dog> likedDogs;
    private LikedDogsRecyclerViewAdapter adapter;

    public LikedDogsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return a new instance of fragment LikedDogsFragment
     */
    public static LikedDogsFragment newInstance() {
        LikedDogsFragment fragment = new LikedDogsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dadapi = DADAPI.getInstance(getContext().getApplicationContext());
        likedDogs = new ArrayList<>();
    }

    public void updateUI() {
        System.out.println("LikedDogsFragment: updateUI()");
        dadapi.getDateRequests(new DADAPI.DateRequestsDataListener() {
            @Override
            public void onGotDateRequests(Set<DateRequest> dateRequests) {
                final LongSparseArray<DateRequest> dogIdToDateRequest = new LongSparseArray<>();
                for (DateRequest request : dateRequests) {
                    dogIdToDateRequest.put(request.getDogId(), request);
                }

                dadapi.getLikedDogs(new DADAPI.DogsDataListener() {
                    @Override
                    public void onGotDogs(List<Dog> dogs) {
                        for (Dog dog : dogs) {
                            if (dogIdToDateRequest.get(dog.getDogId()) != null) {
                                dog.setDateRequest(dogIdToDateRequest.get(dog.getDogId()));
                            }
                        }
                        likedDogs.clear();
                        likedDogs.addAll(dogs);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            updateUI();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_liked_dogs, container, false);

        RecyclerView likedDogsRecyclerView = (RecyclerView) rootView.findViewById(R.id.likedDogsRecyclerView);
        likedDogsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), NUM_COLUMNS));
        adapter = new LikedDogsRecyclerViewAdapter(getActivity(), likedDogs);
        likedDogsRecyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}