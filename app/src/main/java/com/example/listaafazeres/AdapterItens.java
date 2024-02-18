package com.example.listaafazeres;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdapterItens extends RecyclerView.Adapter<AdapterItens.EventeViewHolder> {

    private List<AFazer> listaAFazeres;
    private final Globals globals;
    private final Context context;
    private final InterfaceAtualizarValor interfaceAtualizarValor;
    private final Utils utils;

    public AdapterItens(List<AFazer> listaAFazeres, Context context, InterfaceAtualizarValor interfaceAtualizarValor) {
        this.listaAFazeres = listaAFazeres;
        this.globals = new Globals(context);
        this.context = context;
        this.interfaceAtualizarValor = interfaceAtualizarValor;
        this.utils = new Utils();
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
        holder.textoAFazer.setText(" "+aFazer.getQuantidade()+" | "+aFazer.getNome());
        holder.editPreco.setHint(utils.formatarTextoValor(String.valueOf(aFazer.getValorItem()), true));
        if(aFazer.isFinalizado()){
//            holder.viewFinalizado.setVisibility(View.VISIBLE);
            holder.textoAFazer.setTextColor(context.getResources().getColor(R.color.black));
            holder.constraintBackgtroundItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_a_fazer_concluido));
            holder.checkBoxConcluido.setChecked(true);
        }
        else {
//            holder.viewFinalizado.setVisibility(View.GONE);
            holder.textoAFazer.setTextColor(context.getResources().getColor(R.color.white));
            holder.constraintBackgtroundItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_a_fazer_nao_concluido));
            holder.checkBoxConcluido.setChecked(false);
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
//                    holder.viewFinalizado.setVisibility(View.VISIBLE);
                    holder.textoAFazer.setTextColor(context.getResources().getColor(R.color.black));
                    holder.constraintBackgtroundItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_a_fazer_concluido));
                    aFazer.setFinalizado(true);
                    globals.atualizarAFazere(listaAFazeres);
                }
                else{
//                    holder.viewFinalizado.setVisibility(View.GONE);
                    holder.textoAFazer.setTextColor(context.getResources().getColor(R.color.white));
                    holder.constraintBackgtroundItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_a_fazer_nao_concluido));
                    aFazer.setFinalizado(false);
                    globals.atualizarAFazere(listaAFazeres);
                }

            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // Verifica se o toque não está dentro do EditText
                    if (!(view instanceof EditText)) {
                        holder.editPreco.clearFocus();  // Remove o foco do EditText
                        utils.fecharTeclado(context, view);  // Fecha o teclado
                    }
                }
                return false;
            }
        });

        holder.editPreco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Remove o TextWatcher temporariamente para evitar chamadas recursivas
                holder.editPreco.removeTextChangedListener(this);

                // Formata o texto como um valor monetário
                String formattedText = utils.formatarTextoValor(editable.toString(), false);

                // Atualiza o texto no EditText
                holder.editPreco.setText(formattedText);
                holder.editPreco.setSelection(formattedText.length()); // Coloca o cursor no final do texto

                // Re-adiciona o TextWatcher
                holder.editPreco.addTextChangedListener(this);
            }
        });
        holder.editPreco.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(!holder.editPreco.getText().toString().isEmpty() && position <= listaAFazeres.size() && listaAFazeres.size() != 0){
                        double valorNovo = utils.converterParaDouble(holder.editPreco.getText().toString());
                        listaAFazeres.get(position).setValorItem(valorNovo);
                        globals.atualizarAFazere(listaAFazeres);
                        interfaceAtualizarValor.atualizarValor(listaAFazeres.get(position));
                        if(position == listaAFazeres.size()-1){
                            utils.fecharTeclado(context, view);
                        }
                    }

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

    public static class EventeViewHolder extends RecyclerView.ViewHolder {
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
