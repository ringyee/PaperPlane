package com.marktony.zhihudaily.refactor.timeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.refactor.data.GuokrHandpickNews;

import java.util.List;

/**
 * Created by lizhaotailang on 2017/5/24.
 */

public class GuokrHandpickFragment extends Fragment
        implements GuokrHandpickContract.View{

    private GuokrHandpickContract.Presenter mPresenter;

    // View reference.
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private View mEmptyView;

    private GuokrHandpickNewsAdapter mAdapter;

    private int mOffset = 0;

    public GuokrHandpickFragment() {
        // Requires an empty constructor.
    }

    public static GuokrHandpickFragment newInstance() {
        return new GuokrHandpickFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_page, container, false);

        initViews(view);

        mRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.load(true, 0, 20);
            mOffset = 0;
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取最后一个完全显示的item position
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部并且是向下滑动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        mPresenter.load(false, mOffset++, 20);
                    }
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        mPresenter.load(true, mOffset, 20);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(active));
    }

    @Override
    public void showResult(@NonNull List<GuokrHandpickNews.Result> list) {
        if (mAdapter == null) {
            mAdapter = new GuokrHandpickNewsAdapter(list, getContext());
            mAdapter.setItemClickListener((v, p) -> {

            });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(list);
        }
        mEmptyView.setVisibility(list.isEmpty() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setPresenter(GuokrHandpickContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mEmptyView = view.findViewById(R.id.empty_view);
    }
}
