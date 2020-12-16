package com.e.expandablerecyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycAdapter extends RecyclerView.Adapter<RecycAdapter.ViewHolder> {
    private List<DataModel> dataModelList;
    private DataModel dataModel;

    public interface ItemCallback {
        void clck(String id, DataModel dataModel);
    }

    private ItemCallback mItemCallback;
    private MainActivity mainActivity;

    public RecycAdapter(MainActivity mainActivity, ItemCallback mItemCallback) {
        this.mItemCallback = mItemCallback;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mainActivity).inflate(R.layout.adapter_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DataModel model = dataModelList.get(position);
        holder.txt_name.setText(model.getName());

        holder.itemView.setTag(R.string.MODEL, model);
        holder.itemView.setTag(R.string.position, position);
        holder.radio_selection.setTag(R.string.MODEL, model);
        holder.radio_selection.setTag(R.string.position, position);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.rlContent.getLayoutParams();
        layoutParams.setMargins(((int) convertDpToPixel(5, mainActivity)) * model.getLevel(), layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);

        switch (model.state) {
            case CLOSED:
                holder.imgArrow.setRotation(0);
                break;
            case OPENED:
                holder.imgArrow.setRotation(180);
                break;
        }

        if (model.getModels().isEmpty()) {
            holder.imgArrow.setVisibility(View.INVISIBLE);
        } else {
            holder.imgArrow.setVisibility(View.VISIBLE);
        }

        if (!model.isShowradio()) {
            holder.radio_selection.setVisibility(View.INVISIBLE);
        } else {
            holder.radio_selection.setVisibility(View.VISIBLE);
        }

        holder.radio_selection.setChecked(model.isSelected());
        View.OnClickListener onClickListener = v -> {
            DataModel rootModel = (DataModel) v.getTag(R.string.MODEL);
            updateWithChange(rootModel);
        };
        holder.radio_selection.setOnClickListener(onClickListener);
        holder.itemView.setOnClickListener(v -> {
            int position1 = (int) v.getTag(R.string.position);
            DataModel rootModel = (DataModel) v.getTag(R.string.MODEL);
            updateWithChange(rootModel);
            if (rootModel.getModels().isEmpty()) {
                return;
            }
            switch (rootModel.state) {
                case CLOSED:
                    dataModelList.addAll(position1 + 1, rootModel.getModels());
                    notifyItemRangeInserted(position1 + 1, rootModel.getModels().size());
                    notifyItemRangeChanged(position1 + rootModel.getModels().size(), dataModelList.size() - (position1 + rootModel.getModels().size()));
                    notifyItemRangeChanged(position1, dataModelList.size() - position1);
                    rootModel.state = DataModel.STATE.OPENED;
                    break;

                case OPENED:
                    int start = position1 + 1;
                    int end = dataModelList.size();
                    for (int i = start; i < dataModelList.size(); i++) {
                        DataModel model1 = dataModelList.get(i);
                        if (model1.getLevel() <= rootModel.getLevel()) {
                            end = i;
                            break;
                        } else {
                            if (model1.state == DataModel.STATE.OPENED) {
                                model1.state = DataModel.STATE.CLOSED;
                            }
                        }
                    }

                    if (end != -1) {
                        dataModelList.subList(start, end).clear();
                        notifyItemRangeRemoved(start, end - start);
                        notifyItemRangeChanged(start, end - start);
                        notifyItemRangeChanged(position1, dataModelList.size() - position1);
                    }
                    rootModel.state = DataModel.STATE.CLOSED;
                    break;
            }
        });
    }

    private void updateWithChange(DataModel rootModel) {
        if (rootModel.getLevel() == 2) {
            for (int i = 0; i < dataModelList.size(); i++) {
                for (int j = 0; j < dataModelList.get(i).getModels().size(); j++) {
                    if (!dataModelList.get(i).getModels().get(j).getId().equals(rootModel.getId())) {
                        dataModelList.get(i).getModels().get(j).setSelected(false);
                    } else {
                        dataModelList.get(i).getModels().get(j).setSelected(true);
                    }

                }
            }
            notifyDataSetChanged();
            mItemCallback.clck(rootModel.getId(), rootModel);
        }
    }

    @Override
    public int getItemCount() {
        return dataModelList == null ? 0 : dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlContent;
        TextView txt_name;
        RadioButton radio_selection;
        ImageView imgArrow;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            radio_selection = itemView.findViewById(R.id.radio_selection);
            imgArrow = itemView.findViewById(R.id.imgArrow);
            rlContent = itemView.findViewById(R.id.lin_parent);
        }
    }

    public void setData(List<DataModel> list) {
        dataModelList = list;
        notifyDataSetChanged();
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
}
