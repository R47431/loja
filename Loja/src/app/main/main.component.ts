import { Component } from '@angular/core';
import { ProdutosService } from '../services/produtos.service';
import { Produto } from '../model/Produto';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {

  produtos:Produto[] = [];

  constructor(private service: ProdutosService) {}

  ngOnInit(): void {
    this.lista();
  }

  lista():void {
    this.service.lista()
    .subscribe(data => this.produtos = data);
  }
}
