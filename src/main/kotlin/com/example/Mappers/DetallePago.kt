package com.example.Mappers

import com.example.DTOs.DetallePagoDTO
import com.example.models.DetallePago

fun DetallePago.toDetalleDTO() : DetallePagoDTO{
    return DetallePagoDTO(
        idDetalleDTO = idDetalle,
        idPagoServicioDTO = idPagoServicio,
        codInspeccionDTO = codInspeccion,
        subTotalDTO = subTotal,
    )
}

fun DetallePagoDTO.toDetalle() : DetallePago{

    return DetallePago(
        idDetalle = idDetalleDTO,
        idPagoServicio = idPagoServicioDTO,
        codInspeccion = codInspeccionDTO,
        subTotal = subTotalDTO,
    )

}