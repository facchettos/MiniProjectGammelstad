package com.example.jeremy.miniproject;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<GameDescription> mDataset;
    private onItemClickedListener monItemClickListener ;

    public RecyclerAdapter( onItemClickedListener itemClickListener) {
        monItemClickListener=itemClickListener;
        mDataset = new ArrayList<>();
        GameDescription gd = new GameDescription();
        gd.gameid="";
        gd.timeToHappen="";
        mDataset.add(gd);
        mDataset.add(gd);
        mDataset.add(gd);
        mDataset.add(gd);
    }

    public interface onItemClickedListener{
        void onItemClicked(String team1,String team2,String group,String id);
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Complex data items may need more than one view per item, and
        private TextView mGameID;
        // each data item is just a string in this case
        private TextView mGroupid;
        // you provide access to all the views for a data item in a view holder
        private TextView mTime;
        private TextView mTeam1;
        private TextView mTeam2;

        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mGameID=v.findViewById(R.id.gameid);
            mGroupid=v.findViewById(R.id.group_number);
            mTime=v.findViewById(R.id.timetoplay);
            mTeam1=v.findViewById(R.id.team1);
            mTeam2=v.findViewById(R.id.team2);
        }

    }



    // Provide a suitable constructor (depends on the kind of dataset)

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.textviewgame, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false;
            @Override
            public void onClick(View view) {
                monItemClickListener.onItemClicked(((TextView) view.findViewById(R.id.team1)).getText().toString()
                        ,((TextView) view.findViewById(R.id.team2)).getText().toString(),
                        ((TextView)view.findViewById(R.id.group_number)).getText().toString(),
                        ((TextView)view.findViewById(R.id.gameid)).getText().toString());

                isSelected=!isSelected;


            }
        });
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    public void updateSet(ArrayList<GameDescription> gameDescriptions){
        mDataset = gameDescriptions;
        notifyDataSetChanged();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mGameID.setText(mDataset.get(position).gameid);
        holder.mGroupid.setText(mDataset.get(position).group);
        holder.mTime.setText(mDataset.get(position).timeToHappen);
        holder.mTeam1.setText(mDataset.get(position).team1);
        holder.mTeam2.setText(mDataset.get(position).team2);
        if (position%2==0)
            holder.mView.setBackgroundColor(Color.LTGRAY);
        else
            holder.mView.setBackgroundColor(Color.WHITE);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}