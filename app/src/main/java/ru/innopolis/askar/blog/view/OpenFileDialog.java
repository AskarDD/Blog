package ru.innopolis.askar.blog.view;

import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by admin on 12.07.2017.
 */

public class OpenFileDialog extends AlertDialog.Builder {
    Context context = null;
    private String currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    List<File> files = new ArrayList<File>();
    TextView title;
    ListView listView;
    private Drawable folderIcon;
    private Drawable fileIcon;
    private String accessDeniedMessage;
    private int selectedIndex = -1;
    private OpenDialogListener listener;

    public OpenFileDialog setFolderIcon(Drawable drawable){
        this.folderIcon = drawable;
        return this;
    }

    public OpenFileDialog setFileIcon(Drawable drawable){
        this.fileIcon = drawable;
        return this;
    }

    public OpenFileDialog setAccessDeniedMessage(String message) {
        this.accessDeniedMessage = message;
        return this;
    }

    public OpenFileDialog(Context context) {
        super(context);
        title = createTitle(context);
        changeTitle(title);
        this.context = context;
        LinearLayout linearLayout = createMainLayout(context);
        linearLayout.addView(createBackItem(context));
        //files.addAll(getFiles(currentPath));
        listView = createListView(context);
        linearLayout.addView(listView);
        setCustomTitle(title)
                .setView(linearLayout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedIndex != -1 && listener != null)
                            listener.OnSelectFile(listView.getItemAtPosition(selectedIndex).toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
    }

    private View createBackItem(Context context) {
        TextView textView = createTextView(context, android.R.style.TextAppearance_DeviceDefault_Small);
        Drawable drawable = getContext().getResources().getDrawable(android.R.drawable.ic_menu_directions);
        drawable.setBounds(0,0,60,60);
        textView.setCompoundDrawables(drawable, null, null, null);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(currentPath);
                File parrentDir = file.getParentFile();
                if (parrentDir != null){
                    currentPath = parrentDir.getPath();
                    reBuildFiles((FileAdapter) listView.getAdapter());
                }
            }
        });
        textView.setBackgroundColor(Color.rgb(180,180,255));
        return textView;
    }

    private TextView createTextView(Context context, int style) {
        TextView textView = new TextView(context);
        textView.setTextAppearance(context, style);
        int itemHeight = getItemHeight(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight));
        textView.setMinHeight(itemHeight);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(15,0,0,0);
        return textView;
    }

    private TextView createTitle(Context context) {
        TextView textView = createTextView(context, android.R.style.TextAppearance_DeviceDefault_DialogWindowTitle);
        textView.setBackgroundColor(Color.rgb(150,150,255));
        return textView;
    }

    private int getItemHeight(Context context) {
        TypedValue typedValue = new TypedValue();
        DisplayMetrics metrics = new DisplayMetrics();
        context.getTheme().resolveAttribute(android.R.attr.rowHeight, typedValue, true);
        getDefaultDisplay(context).getMetrics(metrics);
        return 0;
    }

    private static Display getDefaultDisplay(Context context) {
        return ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    private static Point getScreenSize(Context context){
        Point screenSize = new Point();
        getDefaultDisplay(context).getSize(screenSize);
        return screenSize;
    }

    public int getTextTitleWidth(String text, Paint paint){
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.left + bounds.width() + 80;
    }

    private void changeTitle(TextView title){
        String titleText = currentPath;
        int screenWidth = getScreenSize(getContext()).x;
        int widthMax = (int) (screenWidth * 0.99);
        if(getTextTitleWidth(titleText, title.getPaint()) > widthMax){
            while (getTextTitleWidth("..." + titleText, title.getPaint()) > widthMax){
                int start = titleText.indexOf("/", 2);
                if (start > 0){
                    titleText = titleText.substring(start);
                }else{
                    titleText = titleText.substring(2);
                }
            }
            title.setText("..." + titleText);
        }else{
            title.setText(titleText);
        }
    }

    private List<File> getFiles(String directoryPath){
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        List<File> fileList = new ArrayList<>();
        if (directory.isDirectory() && files != null) {
            fileList.addAll(Arrays.asList(files));
        }
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                if (file.isDirectory() && file2.isFile())
                    return -1;
                else if (file.isFile() && file2.isDirectory())
                    return 1;
                else
                    return file.getPath().compareTo(file2.getPath());
            }
        });
        return fileList;
    }

    private class FileAdapter extends ArrayAdapter<File> {

        public FileAdapter(@NonNull Context context, @NonNull List<File> files) {
            super(context, android.R.layout.simple_list_item_1, files);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            File file = getItem(position);
            view.setText(file.getName());
            if (file.isDirectory()) {
                setDrawable(view, folderIcon);
                view.setText(file.getName());
            }else{
                setDrawable(view, fileIcon);
                view.setText(file.getName());
                if (selectedIndex == position)
                    view.setBackgroundColor(Color.rgb(200,200,255));
                else
                    view.setBackgroundColor(Color.rgb(230,230,255));
            }
            return view;
        }

        private void setDrawable(TextView view, Drawable drawable){
            if (view != null){
                if (drawable != null){
                    drawable.setBounds(0,0,60,60);
                    view.setCompoundDrawables(drawable, null, null, null);
                }else{
                    view.setCompoundDrawables(null, null, null, null);
                }
            }
        }
    }

    private void reBuildFiles(ArrayAdapter<File> adapter){
        try {
            if (currentPath.length() > 1) {
                selectedIndex = -1;
                files.clear();
                files.addAll(getFiles(currentPath));
                adapter.notifyDataSetChanged();
                changeTitle(title);
            } else {
                Toast.makeText(getContext(), android.R.string.unknownName, Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), android.R.string.unknownName, Toast.LENGTH_SHORT).show();
        }
    }

    private ListView createListView(final Context context){
        ListView listView = new ListView(context);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                final ArrayAdapter<File> adapter = (FileAdapter) adapterView.getAdapter();
                File file = adapter.getItem(index);
                if (file.isDirectory()){
                        currentPath = file.getPath();

                    reBuildFiles(adapter);
                }else{
                    if (index != selectedIndex)
                        selectedIndex = index;
                    else
                        selectedIndex = -1;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return listView;
    }

    private LinearLayout createMainLayout(Context context){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setMinimumHeight(750);
        return linearLayout;
    }

    private FilenameFilter filenameFilter;

    public OpenFileDialog setFilter(final String filter){
        filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                File tempFile = new File(String.format("%s/%s",file.getPath(), s));
                if (tempFile.isFile())
                    return tempFile.getName().matches(filter);
                return true;
            }
        };
        return this;
    }

    @Override
    public AlertDialog show() {
        currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        files.removeAll(files);
        files.addAll(getFiles(currentPath));
        listView.setAdapter(new FileAdapter(getContext(), files));
        return super.show();
    }

    public interface OpenDialogListener{
        public void OnSelectFile(String fileName);
    }
    public OpenFileDialog setOpenDialogListener(OpenDialogListener listener){
        this.listener = listener;
        return this;
    }
// List<File> fileList = Arrays.asList(directory.)
}
