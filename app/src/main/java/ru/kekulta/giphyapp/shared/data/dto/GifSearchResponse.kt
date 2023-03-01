package ru.kekulta.giphyapp.shared.data.dto

import ru.kekulta.giphyapp.shared.data.dto.GifDto

data class GifSearchResponse(val data: List<GifDto>) : Response()