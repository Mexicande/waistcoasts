package com.six.xinyidai.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * M 适配的数据model类型
 * Created by lihuabin on 2016/11/2.
 */
public abstract class BaseRecyclerViewAdapter<M> extends RecyclerView.Adapter {
    protected final int itemLayoutRId;
    protected Context mContext;
    protected View mHeaderView; //加上去的头
    protected View mFooterView;//加上去的脚
    protected List<M> data;
    protected onItemClickListen mOnItemClickListen;

    public static int TYPE_HEADER = 0;
    public static int TYPE_FOOTER = 2;
    public static int TYPE_NORMAL = 1;

    public BaseRecyclerViewAdapter(Context mContext, int itemLayoutRId) {
        this.itemLayoutRId = itemLayoutRId;
        this.mContext = mContext;
        this.data = new ArrayList<M>();
    }

    public BaseRecyclerViewAdapter(Context mContext, int itemLayoutRId, List<M> data) {
        this.itemLayoutRId = itemLayoutRId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new RecyclerView.ViewHolder(mHeaderView) {
            };
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new RecyclerView.ViewHolder(mFooterView) {
            };
        }
        View view = LayoutInflater.from(mContext).inflate(itemLayoutRId, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int type = getItemViewType(position);
        if (mOnItemClickListen != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListen.OnClick(position, type);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListen.OnLongClick(position, type);
                    return true;
                }
            });
        }
        if (type == TYPE_HEADER || type == TYPE_FOOTER) {
            return;
        }
        if (data == null) {
            return;
        }
        final int realPosition = getRealPosition(holder);
        fillData(holder.itemView, data.get(realPosition));
    }

    /**
     * 填充数据到itemView
     *
     * @param itemView
     * @param model
     */
    protected abstract void fillData(View itemView, M model);

    @Override
    public int getItemCount() {
        int size = data.size();
        if (mHeaderView != null) {
            size += 1;
        }
        if (mFooterView != null) {
            size += 1;
        }
        return size;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0 & mHeaderView != null) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1 && mFooterView != null) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    public List<M> getData() {
        return data;
    }

    public void setData(List<M> data) {
        if (this.data != null) {
            this.data.removeAll(this.data);
            this.data.addAll(data);
            notifyDataSetChanged();
        } else {
            this.data = data;
        }
        notifyDataSetChanged();
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        this.mFooterView = footerView;
        notifyItemInserted(getItemCount());
        notifyDataSetChanged();
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        notifyItemInserted(0);
        notifyDataSetChanged();
    }

    public boolean hasHeaderView() {
        if (mHeaderView == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasFooterView() {
        if (mFooterView == null) {
            return false;
        } else {
            return true;
        }
    }

    public onItemClickListen getOnItemClickListen() {
        return mOnItemClickListen;
    }

    public void setOnItemClickListen(onItemClickListen onItemClickListen) {
        this.mOnItemClickListen = onItemClickListen;
    }

    public interface onItemClickListen {
        void OnClick(int position, int type);

        void OnLongClick(int position, int type);
    }
}
