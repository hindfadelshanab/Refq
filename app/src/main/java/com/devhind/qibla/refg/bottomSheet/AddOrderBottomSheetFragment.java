package com.devhind.qibla.refg.bottomSheet;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.devhind.qibla.refg.Older.Fragment.OldHomeFragment;
import com.devhind.qibla.refg.R;
import com.devhind.qibla.refg.databinding.FragmentAddOrderBottomSheetBinding;
import com.devhind.qibla.refg.model.Order;
import com.devhind.qibla.refg.utilites.Constants;
import com.devhind.qibla.refg.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddOrderBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener , TimePickerDialog.OnTimeSetListener  {

    FragmentAddOrderBottomSheetBinding binding;

    FirebaseFirestore db  ;
    Order order ;
    PreferenceManager preferenceManager ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentAddOrderBottomSheetBinding.inflate(inflater,container,false);
        db = FirebaseFirestore.getInstance();
        order = new Order();
        preferenceManager = new PreferenceManager(getContext());


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String city = bundle.getString("City");
            String type = bundle.getString(Constants.KEY_SRVICE_TYPE);
            Log.e("cityyyy" , city);
           binding.inputServiceType.setText(type);
           binding.inputOrderCity.setText(city);
           order.setCity(city);
           order.setServiceType(type);
        }



        binding.inputStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        binding.inputStartTime.setText( selectedHour + ":" + selectedMinute);
                        order.setStartTime( selectedHour + ":" + selectedMinute);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        binding.inputEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        binding.inputEndTime.setText( selectedHour + ":" + selectedMinute);
                        order.setEndTime( selectedHour + ":" + selectedMinute);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        binding.simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                order.setDate( i2 +" -" + (i1+1) + " - " + i);

            }
        });

        binding.btnBackFromAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();


            }
        });
//        binding.imgArrowBack.setOnClickListener(view -> {
//            dismiss();
//        });
       binding.btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.setOldId(preferenceManager.getString(Constants.KEY_USER_ID));
                order.setOldName(preferenceManager.getString(Constants.KEY_NAME));
                order.setAccept(false);

                addOder(order );
            }
        });

        return binding.getRoot();
    }



    public static final String TAG = "ActionBottomDialog";
    private AddOrderBottomSheetFragment.ItemClickListener mListener;
    public static AddOrderBottomSheetFragment newInstance() {
        return new AddOrderBottomSheetFragment();
    }

    private void addOder(Order order ){
        DocumentReference ref = db.collection(Constants.KEY_COLLECTION_ORDERS).document();
       order.setOrderId(ref.getId());
        ref.set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Order Added", Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.success_add_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_back_to_home);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dismiss();
                    }
                });
                dialog.show();
            }
        }) ;
    }


    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //   view.findViewById(R.id.radio).setOnClickListener(this);
        //  view.findViewById(R.id.btn_next).setOnClickListener(this);
//        RadioGroup radioGroup = view.findViewById(R.id.radioCity);
//
//        view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ChooseCityBottomSheetFragment chooseCityBottomSheetFragment =
//                        ChooseCityBottomSheetFragment.newInstance();
//                chooseCityBottomSheetFragment.show(getActivity().getSupportFragmentManager(),
//                        ChooseCityBottomSheetFragment.TAG);
//            }
//        });
//
//
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
        dismiss();
    }
    @Override public void onClick(View view) {
        TextView tvSelected = (TextView) view;
        RadioGroup radioGroup =(RadioGroup)view;
        mListener.onItemClick(tvSelected.getText().toString());
        dismiss();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }

    public interface ItemClickListener {
        void onItemClick(String item);
    }
}