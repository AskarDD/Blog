package ru.innopolis.askar.blog.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.innopolis.askar.blog.models.Account;

/**
 * Created by admin on 17.07.2017.
 */

public class OpenConfirmDialog extends AlertDialog.Builder {
    TextView tvMessage;
    EditText login;
    EditText password;
    private OpenDialogListener listener;
    Account account;

    public OpenConfirmDialog(@NonNull Context context, String title, String text1, String text2) {
        super(context);
        LinearLayout linearLayout = createMainLayout(context);
        tvMessage = createTextView(context, title, android.R.style.TextAppearance_DeviceDefault_DialogWindowTitle);
        login = createEditTextView(context, text1, android.R.style.TextAppearance_DeviceDefault_Small);
        password = createEditTextView(context, text2, android.R.style.TextAppearance_DeviceDefault_Small);
        password.setInputType(129);
        linearLayout.addView(tvMessage);
        linearLayout.addView(login);
        linearLayout.addView(password);

        setView(linearLayout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        account = new Account(login.getText().toString(), password.getText().toString());
                        listener.OnAddAccount(account);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
    }

    private EditText createEditTextView(Context context, String text, int style) {
        EditText editText = new EditText(context);
        editText.setTextAppearance(context, style);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
        editText.setMinHeight(80);
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setHint(text);
        editText.setPadding(30,0,0,0);
        editText.setTextColor(Color.BLACK);
        editText.setTextSize(18);
        return editText;
    }

    private TextView createTextView(Context context, String text, int style){
        TextView textView = new TextView(context);
        textView.setTextAppearance(context, style);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setMinHeight(80);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setHint(text);
        textView.setPadding(25,0,0,0);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(18);
        return textView;
    }

    private static Display getDefaultDisplay(Context context) {
        return ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    private LinearLayout createMainLayout(Context context){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //linearLayout.setMinimumHeight(750);
        return linearLayout;
    }

    public interface OpenDialogListener{
        public void OnAddAccount(Account account);
    }
    public OpenConfirmDialog setOpenDialogListener(OpenDialogListener listener){
        this.listener = listener;
        return this;
    }
}
