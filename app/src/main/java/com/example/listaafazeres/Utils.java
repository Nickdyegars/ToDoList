package com.example.listaafazeres;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.NumberFormat;

public class Utils {

    public String formatarTextoValor(String input, boolean hint) {
        if(hint){
            String cleanString = input.replaceAll("[^\\d]", "");

            // Converte para um valor numérico
            double parsed = Double.parseDouble(cleanString);

            // Formata como moeda brasileira (R$)
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            return currencyFormat.format(parsed/10);
        }
        else{
            // Remove caracteres não numéricos
            String cleanString = input.replaceAll("[^\\d]", "");

            // Converte para um valor numérico
            double parsed = Double.parseDouble(cleanString);

            // Formata como moeda brasileira (R$)
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            return currencyFormat.format(parsed / 100); // Divide por 100 para tratar centavos corretamente
        }
    }

    public double converterParaDouble(String valor){
        String novoValor = valor.replaceAll("[^\\d,]", "").replace(",",".");
        return Double.parseDouble(novoValor);
    }

    public void fecharTeclado(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
