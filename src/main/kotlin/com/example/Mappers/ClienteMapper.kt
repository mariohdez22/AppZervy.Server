package com.example.Mappers

import com.example.DTOs.ClienteDTO
import com.example.models.Cliente


//mapper de clienteDto hacia cliente (para metodos create, update y delete)
fun ClienteDTO.toCliente(): Cliente {

    return Cliente(
        idCliente = this.idClienteDto ?: "",
        nombres = this.nombresDto,
        celular = this.celularDto,
        correo = this.correoDto,
        contraseña = this.contraseñaDto,
        foto = this.fotoDto,
        dui = this.duiDto
    )
}

//mapper de cliente hacia clienteDto (para metodos read)
fun Cliente.toDto(): ClienteDTO {

    return ClienteDTO(
        idClienteDto = this.idCliente ?: "",
        nombresDto = this.nombres,
        celularDto = this.celular,
        correoDto = this.correo,
        contraseñaDto = this.contraseña,
        fotoDto = this.foto,
        duiDto = this.dui
    )
}