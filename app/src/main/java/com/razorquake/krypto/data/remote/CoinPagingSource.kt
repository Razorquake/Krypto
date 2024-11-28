package com.razorquake.krypto.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.razorquake.krypto.data.remote.dto.Coin

class CoinPagingSource(
    private val apiService: ApiService,

): PagingSource<Int, Coin>() {

    private var totalData = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
        val nextPageNumber = params.key ?: 1
        try {
            val response = apiService.getTopCoins(page = nextPageNumber)
            totalData += response.size
            val data = response.distinctBy { it.id }
            return LoadResult.Page(
                data = data,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (response.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Coin>): Int? {
        return  state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)
        }
    }
}