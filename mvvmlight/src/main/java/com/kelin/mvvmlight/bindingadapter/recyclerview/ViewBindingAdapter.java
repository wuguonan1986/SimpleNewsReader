package com.kelin.mvvmlight.bindingadapter.recyclerview;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by kelin on 16-4-26.
 */
public class ViewBindingAdapter {


    @BindingAdapter({"itemView", "viewModels", "itemViewModelId"})
    public static void bindRecyclerView(RecyclerView recyclerView, int itemRes,
                                        final ObservableArrayList<ViewModel> viewModelList, int itemViewModelId) {
        if (viewModelList != null && !viewModelList.isEmpty()) {
            recyclerView.removeAllViews();
            recyclerView.setAdapter(new ViewBindingAdapter.RecyclerViewAdapter(viewModelList, itemViewModelId, itemRes));
        }
    }

    @BindingAdapter({"adapter"})
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.removeAllViews();
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({"divider"})
    public static void setDivider(RecyclerView recyclerView, Drawable divider) {
        DividerItemDecoration decoration = new DividerItemDecoration(
            recyclerView.getContext(),DividerItemDecoration.VERTICAL );
        decoration.setDrawable(divider);
        recyclerView.addItemDecoration(decoration);
    }

    @BindingAdapter({"layoutManager"})
    public static void setLayoutManager(RecyclerView recyclerView,
                                        LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    public static class RecyclerViewAdapter extends RecyclerView.Adapter {

        private int itemViewRes;

        private int itemViewModelId;

        private ObservableArrayList<ViewModel> viewModelList;

        public RecyclerViewAdapter(ObservableArrayList<ViewModel> viewModelList, int itemViewModelId,
                                   int itemViewRes) {
            this.itemViewRes = itemViewRes;
            this.itemViewModelId = itemViewModelId;
            this.viewModelList = viewModelList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                itemViewRes, null, false);
            return new ViewBindingAdapter.RecylerItemViewHolder(binding.getRoot(), binding);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position >= 0 && position < viewModelList.size()) {
                ViewModel model = viewModelList.get(position);
                if (model != null && holder != null && holder instanceof ViewBindingAdapter.RecylerItemViewHolder) {
                    ((ViewBindingAdapter.RecylerItemViewHolder) holder).bindView(model, itemViewModelId);
                }
            }

        }

        @Override
        public int getItemCount() {
            return viewModelList.size();
        }
    }

    public static class RecylerItemViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding binding;

        public RecylerItemViewHolder(View itemView, ViewDataBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public void bindView(ViewModel model, int viewModelId) {
            binding.setVariable(viewModelId, model);
        }
    }

    @BindingAdapter(value = {"onScrollChangeCommand", "onScrollStateChangedCommand"}, requireAll = false)
    public static void onScrollChangeCommand(final RecyclerView recyclerView,
                                             final ReplyCommand<ScrollDataWrapper> onScrollChangeCommand,
                                             final ReplyCommand<Integer> onScrollStateChangedCommand) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int state;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onScrollChangeCommand != null) {
                    onScrollChangeCommand.execute(new ScrollDataWrapper(dx, dy, state));
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state = newState;
                if (onScrollStateChangedCommand != null) {
                    onScrollChangeCommand.equals(newState);
                }
            }
        });

    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"onLoadMoreCommand"})
    public static void onLoadMoreCommand(final RecyclerView recyclerView, final ReplyCommand<Integer> onLoadMoreCommand) {
        RecyclerView.OnScrollListener listener = new OnScrollListener(onLoadMoreCommand);
        recyclerView.addOnScrollListener(listener);

    }

    public static class OnScrollListener extends RecyclerView.OnScrollListener {

        private PublishSubject<Integer> methodInvoke = PublishSubject.create();

        private ReplyCommand<Integer> onLoadMoreCommand;

        public OnScrollListener(ReplyCommand<Integer> onLoadMoreCommand) {
            this.onLoadMoreCommand = onLoadMoreCommand;
            methodInvoke.throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(
                        new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                OnScrollListener.this.onLoadMoreCommand.execute(integer);
                            }
                        }
                        );
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                if (onLoadMoreCommand != null) {
                    methodInvoke.onNext(recyclerView.getAdapter().getItemCount());
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }


    }

    public static class ScrollDataWrapper {
        public float scrollX;
        public float scrollY;
        public int state;

        public ScrollDataWrapper(float scrollX, float scrollY, int state) {
            this.scrollX = scrollX;
            this.scrollY = scrollY;
            this.state = state;
        }
    }
}
