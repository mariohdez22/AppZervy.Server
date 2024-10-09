package com.example.Mappers

import com.example.DTOs.PersonalDTO
import com.example.models.Personal

//mapper de PersonalDto hacia Personal (para metodos create, update y delete)
fun PersonalDTO.toPersonal() : Personal {

    return Personal(
        idPersonal = this.idPersonal ?: "",
        nombres = this.nombres,
        celular = this.celular,
        correo = this.correo,
        contraseña = this.contraseña,
        foto = this.foto,
        tipoPersonal = this.tipoPersonal,
        estadoPersonal = this.estadoPersonal
    )
}

//mapper de Personal hacia personalDto (para metodos read)
fun Personal.toDto(): PersonalDTO {

    return PersonalDTO(
        idPersonal = this.idPersonal ?: "",
        nombres = this.nombres,
        celular = this.celular,
        correo = this.correo,
        contraseña = this.contraseña,
        foto= this.foto,
        tipoPersonal = this.tipoPersonal,
        estadoPersonal = this.estadoPersonal
    )
}