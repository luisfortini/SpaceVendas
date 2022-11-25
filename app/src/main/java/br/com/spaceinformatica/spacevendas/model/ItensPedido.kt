package br.com.spaceinformatica.spacevendas.model

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class ItensPedido(
    @PrimaryKey(autoGenerate = false) val numItem: Int,
    @ColumnInfo val codigoProduto: Int,
    @ColumnInfo val descProduto: String,
    @ColumnInfo val unidade: String,
    @ColumnInfo val qtdeUnidade: Int,
    @ColumnInfo val fatEstoque: Double,
    @ColumnInfo val fatVenda: Double,
    @ColumnInfo(defaultValue = 0.0.toString()) val quantidade: Double,
    @ColumnInfo val precoVenda: Double,
    @ColumnInfo val precoTabela: Double,
)