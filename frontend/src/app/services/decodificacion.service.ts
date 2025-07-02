import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DecodificacionService {

  corregirCodificacion(texto: string): string {
    if (!texto) return texto;
    
    let textoCorregido = texto;
    
    // Aplicar correcciones paso a paso
    textoCorregido = textoCorregido.replace(/Ã¡/g, 'á');
    textoCorregido = textoCorregido.replace(/Ã©/g, 'é');
    textoCorregido = textoCorregido.replace(/Ã­/g, 'í');
    textoCorregido = textoCorregido.replace(/Ã³/g, 'ó');
    textoCorregido = textoCorregido.replace(/Ãº/g, 'ú');
    textoCorregido = textoCorregido.replace(/Ã±/g, 'ñ');
    textoCorregido = textoCorregido.replace(/Ã'/g, 'Ñ');
    textoCorregido = textoCorregido.replace(/Ã&shy;/g, 'í');
    textoCorregido = textoCorregido.replace(/PÃ©rez/g, 'Pérez');
    textoCorregido = textoCorregido.replace(/GarcÃ­a/g, 'García');
    textoCorregido = textoCorregido.replace(/MarÃ­a/g, 'María');
    textoCorregido = textoCorregido.replace(/MartÃ­nez/g, 'Martínez');
    textoCorregido = textoCorregido.replace(/IngenierÃ­a/g, 'Ingeniería');
    textoCorregido = textoCorregido.replace(/GÃ³mez/g, 'Gómez');
    
    return textoCorregido;
  }
}
