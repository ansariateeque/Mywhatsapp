package com.example.mywhatsapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywhatsapp.Models.MessageModels;
import com.example.mywhatsapp.R;
import com.example.mywhatsapp.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter {

    String reciverId;
    ArrayList<MessageModels> list;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECIEVER_VIEW_TYPE = 2;

    public ChatMessageAdapter(String reciverId, ArrayList<MessageModels> list, Context context) {
        this.reciverId = reciverId;
        this.list = list;
        this.context = context;
    }

    public ChatMessageAdapter(ArrayList<MessageModels> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever, parent, false);
            return new RecieverViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECIEVER_VIEW_TYPE;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModels messageModel = list.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).senderMsg.setText(messageModel.getMsg());

        } else {
            ((RecieverViewHolder) holder).recieverMsg.setText(messageModel.getMsg());

        }
//for message delete purpose
      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View view) {
              new AlertDialog.Builder(context)
                      .setTitle("Delete")
                      .setMessage("Are you sure you want to delete this message")
                      .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              String senderid = FirebaseAuth.getInstance().getUid();
                              String senderRoom=senderid+reciverId;
                              FirebaseDatabase database=FirebaseDatabase.getInstance();
                              database.getReference().child("chats").child(senderRoom)
                                      .child(messageModel.getMsgId()).setValue(null);
                          }
                      }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              dialogInterface.dismiss();
                          }
                      }).show();
              return true;
          }
      });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView recieverMsg, recieverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.recievertext);
            recieverTime = itemView.findViewById(R.id.recievertime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.sendertext);
            senderTime = itemView.findViewById(R.id.sendertime);
        }
    }

}
