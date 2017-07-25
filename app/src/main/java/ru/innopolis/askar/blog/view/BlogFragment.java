package ru.innopolis.askar.blog.view;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.innopolis.askar.blog.ArticleActivity;
import ru.innopolis.askar.blog.R;
import ru.innopolis.askar.blog.models.Account;
import ru.innopolis.askar.blog.models.Article;
import ru.innopolis.askar.blog.models.ArticleDB;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.presenters.BlogPresenter;
import ru.innopolis.askar.blog.presenters.IBlogPresenter;

public class BlogFragment extends Fragment implements IViewBlogFragment {
    private EditText etFillter;
    private TextView tvAddEntity;
    private RecyclerView recyclerView;
    private List<Article> listArticles;
    private List<Article> showArticles;
    private RecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView.ItemAnimator itemAnimator;
    private OpenConfirmDialog confirmDialog;
    private User user;
    private Bundle bundle;
    private Article checkedArticle;
    IBlogPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blog_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etFillter = getView().findViewById(R.id.etFillter);
        tvAddEntity = getView().findViewById(R.id.tvAddEntity);
        recyclerView = getView().findViewById(R.id.rvBlogs);
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        listArticles = user.getArticles();
        showArticles = new ArrayList<>();
        showArticles.addAll(listArticles);
        adapter = new RecyclerViewAdapter(showArticles);
        layoutManager = new LinearLayoutManager(getActivity());
        itemAnimator = new DefaultItemAnimator();
        if (presenter == null)
            presenter = new BlogPresenter();
        inflateRecycler(listArticles);

        etFillter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inflateRecycler(listArticles);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bindView(this, user);
    }

    private void sendToServer(Account account) {
        ArticleDB articleDB = new ArticleDB(checkedArticle.getTitle(), checkedArticle.getText(), account);
        presenter.deleteArticle(articleDB);
    }

    @Override
    public void inflateRecycler(List<Article> articles) {
        showArticles.removeAll(showArticles);
        String text = etFillter.getText().toString().toLowerCase();
        if (text.isEmpty()) {
            showArticles.addAll(articles);
        } else {
            for (Article article : articles)
                if (article.getTitle().startsWith(text))
                    showArticles.add(article);
            for (Article article : articles)
                if (!showArticles.contains(article) && article.getTitle().toLowerCase().contains(text))
                    showArticles.add(article);
            for (Article article : articles)
                if (article.getTitle().trim().isEmpty())
                    showArticles.add(article);
        }
        if (showArticles.size() > 0){
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(itemAnimator);
        }
    }

    @Override
    public void onStartActivity(String key, Object obj, Class<?> classActivity) {
        Intent intent = new Intent(getActivity(), classActivity);
        intent.putExtra(key, (Serializable) obj);
        startActivity(intent);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private List<Article> records;

        public RecyclerViewAdapter(List<Article> articles) {
            this.records = articles;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
            return new RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
            Article article = records.get(position);

            holder.title.setText(article.getTitle());
            holder.deleteBtnListener.setArticle(article);
            holder.editBtnListener.setArticle(article);
        }

        @Override
        public int getItemCount() {
            return records.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title;
            private Button btnDelete;
            private Button btnEdit;
            private RelativeLayout rlRecordRecycler;
            private EditBtnListener editBtnListener;
            private DeleteBtnListener deleteBtnListener;

            public ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.recyclerViewItemTitle);
                btnDelete = itemView.findViewById(R.id.recyclerViewItemDeleteButton);
                btnEdit = itemView.findViewById(R.id.recyclerViewItemEditButton);
                rlRecordRecycler = itemView.findViewById(R.id.rlRecordRecycler);
                deleteBtnListener = new DeleteBtnListener();
                editBtnListener = new EditBtnListener();

                btnDelete.setOnClickListener(deleteBtnListener);
                btnEdit.setOnClickListener(editBtnListener);
            }
        }

        class EditBtnListener implements View.OnClickListener{
            private Article article;
            @Override
            public void onClick(View view) {
                int position = records.indexOf(article);
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("position", position);
                startActivity(intent);
            }
            public void setArticle(Article article){
                this.article = article;
            }
        }

        class DeleteBtnListener implements View.OnClickListener{
            private Article article;
            @Override
            public void onClick(View view) {
                checkedArticle = article;
                confirmDialog = new OpenConfirmDialog(getActivity(), "  Подтвердите удаление статьи \"" + article.getTitle() + "\"", "Введите логин", "Введите пароль")
                        .setOpenDialogListener(new OpenConfirmDialog.OpenDialogListener() {
                            @Override
                            public void OnAddAccount(Account account) {
                                sendToServer(account);
                            }
                        });
                confirmDialog.show();
            }
            public void setArticle(Article article){
                this.article = article;
            }
        }
    }
}
