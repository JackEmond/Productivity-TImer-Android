package com.jedesign.productivitytimer.StatsFragment.Stats;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.jedesign.productivitytimer.DataHelper;
import com.jedesign.productivitytimer.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerViewBasic extends RecyclerView.Adapter<RecyclerViewBasic.MyViewHolder> {

   private List<String> list;
   private String name;


   public RecyclerViewBasic(List<String> list, String name){
      this.list = list;
      this.name = name;
   }

   public class MyViewHolder extends  RecyclerView.ViewHolder{
      private TextView textView;
      private ImageView imageViewDeleteLocation;

      public MyViewHolder(final View view){
         super(view);
         textView = view.findViewById(R.id.tvIteminRV);
         imageViewDeleteLocation = view.findViewById(R.id.imageViewDeleteItem);

      }
   }

   @NonNull
   @Override
   public RecyclerViewBasic.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_basiclayout, parent, false);
      return new MyViewHolder(itemView);
   }

   @Override
   public void onBindViewHolder(@NonNull RecyclerViewBasic.MyViewHolder holder, int position) {
      String item = list.get(position);
      holder.textView.setText(item);
      int p = position;

      holder.imageViewDeleteLocation.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            //Delete Item from database
            DataHelper dh = new DataHelper(holder.textView.getContext()); //Grabbing the context from a random item in holder

            boolean itemDeleted = false;
            if(name.equals("Location"))
            {
               itemDeleted = dh.deleteLocation(item); //If locationIsInMeeting item was not deleted
            }
            if(name.equals("Task")){
               itemDeleted = dh.DeleteTask(item);
            }

            if(itemDeleted){
               removeLocationFromRV(p); //Delete from recycler view
            }
            else{
               alertThatLocationCantBeDeleted();
            }
         }



         private void alertThatLocationCantBeDeleted() {
            new AlertDialog.Builder(holder.textView.getContext())
                    .setTitle("Can't Delete " + name)
                    .setMessage("A timer has this "  + name + ". The timer must be deleted to delete this " + name +".")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                          dialog.cancel();
                       }
                    }).show();
         }
         private void removeLocationFromRV(int p) {
            list.remove(p);
            notifyItemRemoved(p);
            notifyItemRangeChanged(p, list.size());

         }
      });

   }

   @Override
   public int getItemCount() {
      return list.size();
   }
}
