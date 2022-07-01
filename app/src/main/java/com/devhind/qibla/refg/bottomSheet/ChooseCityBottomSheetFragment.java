package com.devhind.qibla.refg.bottomSheet;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.utilites.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChooseCityBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    String citySeceted ="";

    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;
    Bundle bundle2 ;
    public static ChooseCityBottomSheetFragment newInstance() {
        return new ChooseCityBottomSheetFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_choose_city_bottom_sheet, container, false);

        Bundle bundle = this.getArguments();
        bundle2 = new Bundle();
        if (bundle != null) {
            String type = bundle.getString(Constants.KEY_SRVICE_TYPE);
            bundle2.putString(Constants.KEY_SRVICE_TYPE,type);

//                    TextView textView = root.findViewById(R.id.input_service_type);
//                    textView.setText(type);
        }


        RadioGroup radioGroup = root.findViewById(R.id.radioCity);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rd = root.findViewById(i);
                rd.getText().toString();
                Toast.makeText(getContext(), "City :" +rd.getText().toString() , Toast.LENGTH_SHORT).show();
                bundle2.putString("City",rd.getText().toString() );





                // mListener.onItemClick(  rd.getText().toString());
            }
        });

        root.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (citySeceted!=null) {
                    AddOrderBottomSheetFragment addOrderBottomSheetFragment =
                            AddOrderBottomSheetFragment.newInstance();
                    addOrderBottomSheetFragment.show(getActivity().getSupportFragmentManager(),
                            AddOrderBottomSheetFragment.TAG);

                    Log.e("cityy44yy" , citySeceted);

                    addOrderBottomSheetFragment.setArguments(bundle2);
                }
            }
        });



        return root;
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    //   view.findViewById(R.id.radio).setOnClickListener(this);
     //  view.findViewById(R.id.btn_next).setOnClickListener(this);

//       RadioGroup radioGroup = view.findViewById(R.id.radioCity);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton rd = view.findViewById(i);
//                rd.getText().toString();
//                Toast.makeText(getContext(), "City :" +rd.getText().toString() , Toast.LENGTH_SHORT).show();
//
//
//                // mListener.onItemClick(  rd.getText().toString());
//            }
//        });
//        Log.e("citySeceted2" , citySeceted);
//
//        view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (citySeceted!=null) {
//                    AddOrderBottomSheetFragment addOrderBottomSheetFragment =
//                            AddOrderBottomSheetFragment.newInstance();
//                    addOrderBottomSheetFragment.show(getActivity().getSupportFragmentManager(),
//                            AddOrderBottomSheetFragment.TAG);
//
//                    Log.e("cityy44yy" , citySeceted);
//
//                    bundle.putString("City",citySeceted.toString() );
//                    addOrderBottomSheetFragment.setArguments(bundle);
//                }
//            }
//        });
//
//
//       radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//           @Override
//           public void onCheckedChanged(RadioGroup radioGroup, int i) {
//               RadioButton rd = view.findViewById(i);
//               rd.getText().toString();
//               Toast.makeText(getContext(), "City :" +rd.getText().toString() , Toast.LENGTH_SHORT).show();
//
//
//              // mListener.onItemClick(  rd.getText().toString());
//           }
//       });
//        view.findViewById(R.id.textView2).setOnClickListener(this);
//        view.findViewById(R.id.textView3).setOnClickListener(this);
//        view.findViewById(R.id.textView4).setOnClickListener(this);
//        binding.radio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
//                var radioButton = findViewById<View>(checkedId) as RadioButton
//                //  Toast.makeText(baseContext, radioButton.getText(), Toast.LENGTH_SHORT).show()
//                accountType = radioButton.text.toString()
//
//        }
//        )
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof ItemClickListener) {
//            mListener = (ItemClickListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement ItemClickListener");
//        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override public void onClick(View view) {
        TextView tvSelected = (TextView) view;
        RadioGroup radioGroup =(RadioGroup)view;
        mListener.onItemClick(tvSelected.getText().toString());
        dismiss();
    }
    public interface ItemClickListener {
        void onItemClick(String item);
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_choose_city_bottom_sheet, container, false);
//    }
}