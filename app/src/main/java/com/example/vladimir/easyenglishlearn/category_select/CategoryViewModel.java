package com.example.vladimir.easyenglishlearn.category_select;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.vladimir.easyenglishlearn.App;
import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.SingleLiveEvent;
import com.example.vladimir.easyenglishlearn.db.WordDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.vladimir.easyenglishlearn.Constants.EMPTY_STRING;

public class CategoryViewModel extends ViewModel {

    private WordDao mRepository;
    private LiveData<List<String>> mCategoriesLiveData;
    private SingleLiveEvent<String> mEditCategoryLiveData;
    private SingleLiveEvent<String> mRemoveDialogLiveData;
    private SingleLiveEvent<String> mOpenCategoryLiveData;
    private SingleLiveEvent<Void> mRemoveCategoryLiveData;
    private SingleLiveEvent<Integer> mMessageLiveData;
    private CompositeDisposable mDisposable;


    public CategoryViewModel() {
        mRepository = App.getInstance().getDatabase().wordDao();
        mCategoriesLiveData = mRepository.getAllCategories();
        mEditCategoryLiveData = new SingleLiveEvent<>();
        mRemoveDialogLiveData = new SingleLiveEvent<>();
        mOpenCategoryLiveData = new SingleLiveEvent<>();
        mRemoveCategoryLiveData = new SingleLiveEvent<>();
        mMessageLiveData = new SingleLiveEvent<>();
        mDisposable = new CompositeDisposable();
    }

    public void onFabClick() {
        mEditCategoryLiveData.setValue(EMPTY_STRING);
    }

    public void onRemoveIconClick(String categoryName) {
        mRemoveDialogLiveData.setValue(categoryName);
    }

    public void onEditIconClick(String categoryName) {
        mEditCategoryLiveData.setValue(categoryName);
    }

    public void onRvItemClick(String categoryName) {
        mOpenCategoryLiveData.setValue(categoryName);
    }

    public void removeCategory(String categoryName) {
        Disposable disposable = Completable
                .fromAction(() -> mRepository.removeCategory(categoryName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showMessage);
        mRemoveCategoryLiveData.call();
        mDisposable.add(disposable);
    }

    public void cancelRemoving() {
        mRemoveCategoryLiveData.call();
    }

    private void showMessage() {
        mMessageLiveData.setValue(R.string.category_removed);
    }

    @Override
    protected void onCleared() {
        mDisposable.dispose();
    }

    LiveData<String> getOpenCategoryLiveData() {
        return mOpenCategoryLiveData;
    }

    LiveData<String> getEditCategoryLiveData() {
        return mEditCategoryLiveData;
    }

    LiveData<String> getRemoveDialogLiveData() {
        return mRemoveDialogLiveData;
    }

    LiveData<Void> getRemoveCategoryLiveData() {
        return mRemoveCategoryLiveData;
    }

    LiveData<List<String>> getCategoriesLiveData() {
        return mCategoriesLiveData;
    }

    LiveData<Integer> getMessageLiveData() {
        return mMessageLiveData;
    }
}
