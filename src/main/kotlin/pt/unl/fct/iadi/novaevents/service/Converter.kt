package pt.unl.fct.iadi.novaevents.service

interface Converter<D, M> {

    fun convertDtoToModel(dto: D): M
    fun convertDtoToModel(dto: List<D>): List<M> {
        return dto.map { convertDtoToModel(it) }
    }

    fun convertModelToDto(model: List<M>): List<D> {
        return model.map { convertModelToDto(it) }
    }

    fun convertModelToDto(model: M): D
}