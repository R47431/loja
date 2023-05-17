import { Component } from '@angular/core';
import { ProdutosService } from '../services/produtos.service';
import { Produto } from '../model/Produto';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {

  produto = new Produto();
  produtos:Produto[] = [];
  pesquisa: string = "";
  produtoEncontrado: Produto | undefined;


  constructor(private service: ProdutosService) {}

  ngOnInit(): void {
    this.lista();
  }

  lista():void {
    this.service.lista()
    .subscribe(data => this.produtos = data);
  }

  cadastrar(): void {
    this.service.cadastrar(this.produto)
    .subscribe(data => {
      this.produtos.push(data);
      this.limpaFormulario;
      alert("Cadastrar");
    })
  }

  altera(): void {
    this.service.altera(this.produto)
    .subscribe(data => {
      let indice = this.produtos.findIndex(data => data.id === this.produto.id)
      this.produtos[indice] = data;
      this.limpaFormulario;
      alert("Alterado");
    })
  }

  deleta(): void {
    this.service.deleta(this.produto.id)
    .subscribe(() => {
      let indice = this.produtos.findIndex(data => data.id === this.produto.id)
      this.produtos.splice(indice,1);
      this.limpaFormulario;
      alert("Deleta");
    })
  }

  buscarProduto(): void {
    this.produtoEncontrado = this.produtos.find(produto =>
      produto.nome && produto.nome.toLowerCase().includes(this.pesquisa.toLowerCase())
    );
  }

  limpaFormulario(): void {
    this.produto = new Produto();
  }

}
