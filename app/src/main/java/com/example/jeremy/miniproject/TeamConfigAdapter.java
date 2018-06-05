package com.example.jeremy.miniproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.*;
import com.example.jeremy.miniproject.TeamConfiguration.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class TeamConfigAdapter extends RecyclerView.Adapter<TeamConfigAdapter.ViewHolder> {

    static ArrayList<Player> players;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private SparseBooleanArray itemStateArray= new SparseBooleanArray();
    private static getInfosAboutSelection getInfosAboutSelection;
    private int numberOfplayers=0;
    private int nbofoverage=0;
    private int overageIfBefore;
    ArrayList<Player> selectedPlayers=new ArrayList<>();


    public String getSelectedPlayersAsJson(){
        String res = gson.toJson(selectedPlayers);
        Log.d(TAG, "getSelectedPlayersAsJson: "+res);
        return res;
    }


    public interface getInfosAboutSelection{
        void getNumberOfPlayers(int nb);
        void getNumberOfOveragedPlayers(int nb);
    }

    public TeamConfigAdapter(getInfosAboutSelection getInfosAboutSelection,int yearBirth){
        if (yearBirth>50){
            overageIfBefore=1900+yearBirth;
        }else{
            overageIfBefore=2000+yearBirth;
        }
        this.getInfosAboutSelection=getInfosAboutSelection;
        players=new ArrayList<>();
        players.add(new Player());
        players.add(new Player());
        players.add(new Player());
        players.add(new Player());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Complex data items may need more than one view per item, and
        // each data item is just a string in this case
        private TextView mPlayerName;
        // you provide access to all the views for a data item in a view holder
        private TextView mPlayerBirth;
        public CheckBox mCheckBox;
        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mPlayerBirth= v.findViewById(R.id.playerBirth);
            mPlayerName= v.findViewById(R.id.playerName);
            mCheckBox=v.findViewById(R.id.checkBox);
            mCheckBox.setSelected(false);
            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    if (!itemStateArray.get(adapterPosition, false)) {
                        mCheckBox.setChecked(true);
                        itemStateArray.put(adapterPosition, true);
                        numberOfplayers++;
                        selectedPlayers.add(players.get(adapterPosition));
                        String yearofbirth = players.get(adapterPosition).yearOfBirth.substring(0,2);
                        int yearOfbirthint;
                        if (Integer.parseInt(yearofbirth)>50){
                            yearOfbirthint=1900+Integer.parseInt(yearofbirth);
                        }else{
                            yearOfbirthint=2000+Integer.parseInt(yearofbirth);
                        }
                        if (yearOfbirthint<overageIfBefore){
                            nbofoverage++;
                        }
                    }
                    else  {
                        mCheckBox.setChecked(false);
                        itemStateArray.put(adapterPosition, false);
                        numberOfplayers--;
                        selectedPlayers.remove(players.get(adapterPosition));
                        String yearofbirth = players.get(adapterPosition).yearOfBirth.substring(0,2);
                        int yearOfbirthint;
                        if (Integer.parseInt(yearofbirth)>50){
                            yearOfbirthint=1900+Integer.parseInt(yearofbirth);
                        }else{
                            yearOfbirthint=2000+Integer.parseInt(yearofbirth);
                        }
                        if (yearOfbirthint<overageIfBefore){
                            Log.d(TAG, "onClick: "+nbofoverage);
                            nbofoverage--;
                            Log.d(TAG, "onClick: "+nbofoverage);
                        }
                    }
                    getInfosAboutSelection.getNumberOfPlayers(numberOfplayers);
                    getInfosAboutSelection.getNumberOfOveragedPlayers(nbofoverage);
                }

            });

        }

    }

    public void updateSet(ArrayList<Player> players){
        this.players=players;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_view, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mPlayerName.setText(players.get(position).playerName);
        holder.mPlayerBirth.setText(players.get(position).yearOfBirth);
        if (!itemStateArray.get(position, false)) {
            holder.mCheckBox.setChecked(false);
        } else {
            holder.mCheckBox.setChecked(true);
        }


    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
