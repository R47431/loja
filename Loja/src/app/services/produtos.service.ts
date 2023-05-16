import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Produto } from '../model/Produto';

@Injectable({
  providedIn: 'root'
})
export class ProdutosService {

  private url:string = 'http://localhost:8080';

  constructor(private http:HttpClient) { }

  lista():Observable<Produto[]> {
    return this.http.get<Produto[]>(this.url);
  }

  cadastrar(produto:Produto):Observable<Produto> {
    return this.http.post<Produto>(`${this.url}`, produto)
  }

  altera(produto:Produto):Observable<Produto> {
    return this.http.put<Produto>(`${this.url}`, produto)
  }

  deleta(id:number):Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`)
  }

  busca(id:number):Observable<void> {
    return this.http.get<void>(`${this.url}/${id}`)
  }
}
