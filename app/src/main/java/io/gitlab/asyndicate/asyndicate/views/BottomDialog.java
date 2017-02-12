package io.gitlab.asyndicate.asyndicate.views;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.gitlab.asyndicate.asyndicate.R;


public class BottomDialog extends BottomSheetDialogFragment {

    String title;
    String message;
    boolean responseButtons = true;

    public static BottomDialog newInstance(String title, String message) {
        return newInstance(title, message, true);
    }

    public static BottomDialog newInstance(String title, String message, boolean showResponseButtons) {
        BottomDialog f = new BottomDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putBoolean("buttons", showResponseButtons);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        message = getArguments().getString("message");
        responseButtons = getArguments().getBoolean("buttons");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_modal, container, false);
        Button button = (Button) v.findViewById(R.id.yes);
        Button dismis = (Button) v.findViewById(R.id.no);

        if (!responseButtons) {
            button.setVisibility(View.INVISIBLE);
            dismis.setText("Dismis");
            dismis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setText(title);
        tv = (TextView) v.findViewById(R.id.message);
        tv.setText(message);
        return v;
    }
}