package com.example.talegateinstagram.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.Nullable;

public class LogoutDialogFragment extends DialogFragment {
    public interface OnLogoutListener {
        void onLogout(boolean log);
    }

    public LogoutDialogFragment() {
        //needed for library
    }

    public static LogoutDialogFragment newInstance() {
        LogoutDialogFragment frag = new LogoutDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Logout?");
        args.putString("message", "Do you want to log out?");
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        final OnLogoutListener listener = (OnLogoutListener) getActivity();
        alertDialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onLogout(true);
                dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onLogout(false);
                dismiss();
            }
        });
        return alertDialogBuilder.create();
    }
}
