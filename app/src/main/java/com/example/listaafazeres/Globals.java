package com.example.listaafazeres;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Globals {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public Globals(Context context){
        this.context=context;
        this.sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        this.editor=sharedPreferences.edit();
        this.gson = new Gson();
    }


    public void adicionarAFazer(AFazer aFazer)
    {
        try
        {
            List<AFazer> listaAFazer = consultarAFazer();
            listaAFazer.add(aFazer);
            atualizarAFazere(listaAFazer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void atualizarAFazere(List<AFazer> listaAFazer)
    {
        try
        {
            this.editor.putString("DadosAFazer", this.gson.toJson(listaAFazer));
            this.editor.apply();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<AFazer> consultarAFazer()
    {
        List<AFazer> listaAFazer = new ArrayList<>();
        try
        {
            String listaAFazerString = this.sharedPreferences.getString("DadosAFazer","");
            if(listaAFazerString.isEmpty())
            {
                return listaAFazer;
            }
            else
            {
                listaAFazer.addAll(Arrays.asList(this.gson.fromJson(listaAFazerString, AFazer[].class)));
                return listaAFazer;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return listaAFazer;
    }

    public void removerAFazer(AFazer aFazer)
    {
        try
        {
            List<AFazer> listaAFazer = consultarAFazer();

            for(int i=0; i<listaAFazer.size(); i++)
            {
                if(listaAFazer.get(i).getNome().equals(aFazer.getNome()))
                {
                    listaAFazer.remove(i);
                }
            }
            atualizarAFazere(listaAFazer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void salvarValorTotal(double valorTotal)
    {
        this.editor.putString("ValorTotal", String.valueOf(valorTotal));
        this.editor.apply();
    }

    public double consultarValorTotal()
    {
        double valorTotal = 0;
        try
        {
            valorTotal = Double.parseDouble(this.sharedPreferences.getString("ValorTotal",""));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return valorTotal;
    }
}
