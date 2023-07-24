package com.example.mywhatsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywhatsapp.GroupChatDetails;
import com.example.mywhatsapp.Models.GroupMessageModels;
import com.example.mywhatsapp.Models.MessageModels;
import com.example.mywhatsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class GroupMessageChatAdapter extends RecyclerView.Adapter {

    public int SENDER_VIEW_TYPE = 1;
    public int RECIEVER_VIEW_TYPE = 2;
    ArrayList<GroupMessageModels> list;
    Context context;

    public GroupMessageChatAdapter(ArrayList<GroupMessageModels> list, Context context) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupMessageModels groupMessageModels = list.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).sendermsg.setText(groupMessageModels.getMsg());
        } else {
            ((RecieverViewHolder) holder).recieverMsg.setText(groupMessageModels.getMsg());
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
    public int getItemCount() {
        return list.size();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView sendermsg, sendertime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            sendermsg = itemView.findViewById(R.id.sendertext);
            sendertime = itemView.findViewById(R.id.sendertime);
        }
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView recieverMsg, recieverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.recievertext);
            recieverTime = itemView.findViewById(R.id.recievertime);
        }
    }
}
