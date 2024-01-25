package com.example.listaafazeres;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterItens extends RecyclerView.Adapter<AdapterItens.EventeViewHolder> {

    private List<AFazer> listaAFazeres;
    private Globals globals;
    private Context context;
    private InterfaceAtualizarValor interfaceAtualizarValor;

    public AdapterItens(List<AFazer> listaAFazeres, Context context, InterfaceAtualizarValor interfaceAtualizarValor) {
        this.listaAFazeres = listaAFazeres;
        this.globals = new Globals(context);
        this.context = context;
        this.interfaceAtualizarValor = interfaceAtualizarValor;
    }

    @NonNull
    @Override
    public AdapterItens.EventeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_a_fazer, parent, false);
        return new EventeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItens.EventeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AFazer aFazer = listaAFazeres.get(position);
        holder.textoAFazer.setText(aFazer.getNome());
        if(aFazer.isFinalizado()){
            holder.viewFinalizado.setVisibility(View.VISIBLE);
            holder.constraintBackgtroundItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_a_fazer_concluido));
            holder.checkBoxConcluido.setChecked(true);
        }
        holder.botaoDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globals.removerAFazer(aFazer);
                listaAFazeres = globals.consultarAFazer();
                notifyDataSetChanged();
            }
        });
        holder.checkBoxConcluido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBoxConcluido.isChecked()){
                    holder.viewFinalizado.setVisibility(View.VISIBLE);
                    holder.constraintBackgtroundItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_a_fazer_concluido));
                    listaAFazeres.get(position).setFinalizado(true);
                    globals.atualizarAFazere(listaAFazeres);
                }
                else{
                    holder.viewFinalizado.setVisibility(View.GONE);
                    holder.constraintBackgtroundItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_a_fazer_nao_concluido));
                    listaAFazeres.get(position).setFinalizado(false);
                    globals.atualizarAFazere(listaAFazeres);
                }

            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                holder.editPreco.clearFocus();
                return false;
            }
        });
        holder.editPreco.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    double valorNovo = Double.parseDouble(holder.editPreco.getText().toString());
                    interfaceAtualizarValor.atualizarValor(valorNovo);
//                    holder.editPreco.setFocusable(false);
//                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAFazeres.size();
    }

    public class EventeViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView textoAFazer;
        private CheckBox checkBoxConcluido;
        private AppCompatImageView botaoDeletar;
        private View viewFinalizado;
        private ConstraintLayout constraintBackgtroundItem;
        private AppCompatEditText editPreco;
        public EventeViewHolder(@NonNull View itemView) {
            super(itemView);
            textoAFazer = itemView.findViewById(R.id.textoAFazer);
            checkBoxConcluido = itemView.findViewById(R.id.checkBoxConcluido);
            botaoDeletar = itemView.findViewById(R.id.iconeDeletar);
            viewFinalizado = itemView.findViewById(R.id.viewFinalizado);
            constraintBackgtroundItem = itemView.findViewById(R.id.backgtroundItem);
            editPreco = itemView.findViewById(R.id.editPreco);
        }
    }
}
