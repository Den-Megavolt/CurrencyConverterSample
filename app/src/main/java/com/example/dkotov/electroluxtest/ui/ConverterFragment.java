package com.example.dkotov.electroluxtest.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dkotov.electroluxtest.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.HashMap;

import model.Currencies;
import model.MessageEvent;
import util.RatesRetriever;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConverterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConverterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConverterFragment extends Fragment {

    private static final String TAG = "ElectroluxTest: " + ConverterFragment.class.getSimpleName();

    private TextView origCurrencyTextView;
    private TextView convCurrencyTextView;
    private EditText origCurrencyEditText;
    private EditText convCurrencyEditText;
    private Spinner origCurrencySpinner;
    private Spinner convCurrencySpinner;
    private ProgressBar mProgressBar;

    //Default base for retrieve rates if USD
    private String currencyCode;
    private double currencyRate;


    private OnFragmentInteractionListener mListener;

    public ConverterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConverterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConverterFragment newInstance() {
        ConverterFragment fragment = new ConverterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_currency, container, false);

        origCurrencyTextView = (TextView) rootView.findViewById(R.id.origCurrencyTextView);
        convCurrencyTextView = (TextView) rootView.findViewById(R.id.convCurrencyTextView);

        origCurrencyEditText = (EditText) rootView.findViewById(R.id.origCurrencyEditText);
        convCurrencyEditText = (EditText) rootView.findViewById(R.id.convCurrencyEditText);

        origCurrencySpinner = (Spinner) rootView.findViewById(R.id.origCurrencySpinner);
        convCurrencySpinner = (Spinner) rootView.findViewById(R.id.convCurrencySpinner);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.request_progress);

        origCurrencySpinner.setSelection(0);
        convCurrencySpinner.setSelection(1);
        convCurrencySpinner.setEnabled(false);

        origCurrencyTextView.setText("1 " + origCurrencySpinner.getSelectedItem().toString() +
                getString(R.string.equals));
        convCurrencyTextView.setText("0 " + convCurrencySpinner.getSelectedItem().toString());

        origCurrencyEditText.addTextChangedListener(new CurrencyTextWatcher(origCurrencyEditText));
        convCurrencyEditText.addTextChangedListener(new CurrencyTextWatcher(convCurrencyEditText));

        origCurrencySpinner.setOnItemSelectedListener(new CurrencySpinnerItemClickListener(origCurrencySpinner));
        convCurrencySpinner.setOnItemSelectedListener(new CurrencySpinnerItemClickListener(convCurrencySpinner));

        currencyCode = Currencies.USD.name();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        showProgress(true);
        Log.d(TAG, "Currency code: " + currencyCode);
    }

    private void populateViews(HashMap<String, Double> rates) {
        origCurrencyTextView.setText("1 " + origCurrencySpinner.getSelectedItem().toString() + " " +
                getString(R.string.equals));
        currencyRate = rates.get(Currencies
                .getEnumByString(convCurrencySpinner.getSelectedItem().toString()));
        convCurrencyTextView
                .setText(currencyRate + " " + convCurrencySpinner.getSelectedItem().toString());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.d(TAG, "MessageEvent Received");
        showProgress(false);
        if (event.getStatus() == 0) {
            populateViews(event.getRates());
        } else {
            Toast.makeText(getContext(), "Request error", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class CurrencyTextWatcher implements TextWatcher {

        private View view;

        private CurrencyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            double convValue;
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
//            decimalFormat.setRoundingMode(RoundingMode.UP);
            switch (view.getId()) {
                case R.id.origCurrencyEditText:
                    if (origCurrencyEditText.isFocused()) {
                        try {
                            convValue = (Double.valueOf(s.toString()) * currencyRate);
                            convCurrencyEditText.setText(String.valueOf(decimalFormat.format(convValue)));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            convCurrencyEditText.setText("");
                        }
                    }
                    break;
                case R.id.convCurrencyEditText:
                    if (convCurrencyEditText.isFocused()) {
                        try {
                            convValue = (Double.valueOf(s.toString()) / currencyRate);
                            origCurrencyEditText.setText(String.valueOf(decimalFormat.format(convValue)));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            origCurrencyEditText.setText("");
                        }
                    }
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class CurrencySpinnerItemClickListener implements AdapterView.OnItemSelectedListener {

        private View changedView;

        private CurrencySpinnerItemClickListener(View view) {
            this.changedView = view;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (changedView.getId()) {
                case R.id.origCurrencySpinner:
                    if (origCurrencySpinner.getSelectedItemPosition() == 0) {
                        convCurrencySpinner.setSelection(1);
                    } else {
                        convCurrencySpinner.setSelection(0);
                    }
                    currencyCode = Currencies
                            .getEnumByString(origCurrencySpinner.getSelectedItem().toString());
                    showProgress(true);
                    RatesRetriever.retrieveRates(currencyCode);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
