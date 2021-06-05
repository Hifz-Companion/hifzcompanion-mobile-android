package com.nnems.hifzcompanion.adapters;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.nnems.hifzcompanion.models.SampleHolder;

public class NoRecyclerAdapter extends RecyclerView.Adapter<SampleHolder> {

    @Override
    public SampleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SampleHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
