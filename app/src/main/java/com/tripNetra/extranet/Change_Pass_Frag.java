package com.tripNetra.extranet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Objects;

public class Change_Pass_Frag extends DialogFragment {

    public Change_Pass_Frag() {}

    TextInputLayout opwdTIL,npwdTIL,cpwdTIL;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_change_pass, container, false);

        opwdTIL = view.findViewById(R.id.oldpwdTIL);
        npwdTIL = view.findViewById(R.id.newpasswdTIL);
        cpwdTIL = view.findViewById(R.id.cnfrmpasswordTIL);

        view.findViewById(R.id.submitbutton).setOnClickListener(v -> {
            final String old_password = ((EditText) view.findViewById(R.id.oldpwdET)).getText().toString(),
                    new_password = ((EditText) view.findViewById(R.id.newpasswordET)).getText().toString(),
                    cpassword = ((EditText) view.findViewById(R.id.confrmpwdET)).getText().toString();

            opwdTIL.setErrorEnabled(false);npwdTIL.setErrorEnabled(false);cpwdTIL.setErrorEnabled(false);

            if(old_password.equals("")){
                opwdTIL.setError("Enter Old Password");
            }else if(new_password.equals("")){
                npwdTIL.setError("Enter New Password");
            } else if(cpassword.equals("")){
                cpwdTIL.setError("Confirm New Password");
            }else if(!cpassword.equals(new_password)){
                cpwdTIL.setError("New Password Dont Match");
            }else {
                Bundle bb = new Bundle();
                bb.putString("old_password",old_password);
                bb.putString("new_password",new_password);

                ((Dashboard_Main_Act) Objects.requireNonNull(getActivity())).updatepass(bb);
            }
        });

        return view;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

}