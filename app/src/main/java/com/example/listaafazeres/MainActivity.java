package com.example.listaafazeres;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.listaafazeres.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements InterfaceAtualizarValor
{

    private ActivityMainBinding binding;
    private Globals globals;
    private AdapterItens adapterItens;
    private List<AFazer> listaAFazer;
    private double valorTotal;
    private Utils utils;
    private int quantidadeProdutos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        globals = new Globals(this);
        listaAFazer = globals.consultarAFazer();
        valorTotal = globals.consultarValorTotal();
        utils = new Utils();
        configurarLayout();
    }

    private void configurarLayout()
    {
        try {
            binding.botaoAdicionarAFazer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    configurarModal();
                }
            });

            binding.imageBotaoDeletarTudo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletarTodos();
                }
            });

            if(!globals.consultarVisualicaoModal()){
                configurarModalPrimeiroUso();
            }

            configurarAd();
            atualizarValorTela();
            configurarAdapter();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void configurarAd()
    {
        try
        {
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            AdRequest adRequest = new AdRequest.Builder().build();
            binding.adView.loadAd(adRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void configurarAdapter()
    {
        adapterItens = new AdapterItens(listaAFazer, getBaseContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.reyclerItens.setLayoutManager(layoutManager);
        binding.reyclerItens.setHasFixedSize(true);
        binding.reyclerItens.setAdapter(adapterItens);
        adapterItens.notifyDataSetChanged();
    }

    private void configurarModal()
    {
        // Inflar o layout do conteúdo do modal
        try {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.modal_adcionar_a_fazer, null);

            // Acessar elementos do layout personalizado (por exemplo, TextView)
            AppCompatEditText editAFazer = view.findViewById(R.id.editAdicionar);
            AppCompatTextView textQuantidade = view.findViewById(R.id.textoQuantidade);
            CardView botaoAumentar = view.findViewById(R.id.botaoAumentar);
            CardView botaoDiminuir = view.findViewById(R.id.botaoDiminuir);

            botaoAumentar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantidadeProdutos++;
                    textQuantidade.setText(String.valueOf(quantidadeProdutos));
                }
            });

            botaoDiminuir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(quantidadeProdutos > 1){
                        quantidadeProdutos--;
                        textQuantidade.setText(String.valueOf(quantidadeProdutos));
                    }
                }
            });


            // Configurar o AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view)
                    .setTitle("Adicionar produto")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(quantidadeProdutos >= 1)
                            {
                                if(!editAFazer.getText().toString().isEmpty())
                                {
                                    AFazer aFazerNovo = new AFazer();
                                    aFazerNovo.setQuantidade(Integer.parseInt(textQuantidade.getText().toString()));
                                    aFazerNovo.setNome(editAFazer.getText().toString());
                                    aFazerNovo.setFinalizado(false);
                                    globals.adicionarAFazer(aFazerNovo);
                                    listaAFazer = globals.consultarAFazer();
                                    configurarAdapter();
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"Preencha com algum nome", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Não é possivel cadastra 0 produtos", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            // Exibir o AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarModalPrimeiroUso()
    {
        try
        {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.texto_boas_vindas, null);

            AppCompatTextView textoBoasVindas = view.findViewById(R.id.textoBoasVindas);
            String texto = "Seja muito bem-vindo ao nosso aplicativo de lista de compras em sua versão MVP (Produto Mínimo Viável)! Estamos empolgados por tê-lo experimentando esta versão inicial que visa simplificar suas compras de maneira eficiente.\n" +
                    "\n" +
                    "Nossa equipe está dedicada a aprimorar continuamente a experiência do usuário. Durante esta fase inicial, valorizamos imensamente seu feedback para aprimorar funcionalidades, corrigir possíveis problemas e, assim, construir um aplicativo cada vez mais útil e intuitivo.\n" +
                    "\n" +
                    "Este aplicativo foi desenvolvido com fins de estudo das tecnologias.\n"+
                    "\n" +
                    "Qualquer duvida entre em contato com pedrohcoliveir@gmail.com\n";


            textoBoasVindas.setText(texto);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view)
                    .setTitle("Boas-Vindas")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            globals.salvarVisualisacaoModalBoasVindas(true);
                            dialog.dismiss();
                        }
                    });

            // Exibir o AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void atualizarValorTela()
    {
        try
        {
            double valorTotal = 0;
            for(AFazer aFazer : listaAFazer)
            {
                if(aFazer.getQuantidade() == 1)
                {
                    valorTotal += aFazer.getValorItem();
                }
                else
                {
                    valorTotal += aFazer.getValorItem()*aFazer.getQuantidade();
                }
            }
            binding.textoValorTotalVariavel.setText(utils.formatarTextoValor(String.valueOf(valorTotal), true));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void deletarTodos()
    {
        try
        {
            if(listaAFazer.size() > 0)
            {
                for(AFazer aFazer: listaAFazer)
                {
                    globals.removerAFazer(aFazer);
                }
                listaAFazer = globals.consultarAFazer();
                atualizarValorTela();
                configurarAdapter();
            }
            else
            {
                Toast.makeText(this, "Não existe nehum item adicionado", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizarValor(AFazer aFazer)
    {
        valorTotal+=aFazer.getValorItem();
        listaAFazer = globals.consultarAFazer();
        atualizarValorTela();
    }
}