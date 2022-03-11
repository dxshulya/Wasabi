package com.dxshulya.wasabi.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.dxshulya.wasabi.data.Api
import com.dxshulya.wasabi.model.Favorites
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class FavoritePagingSource(private val api: Api) : RxPagingSource<Int, Favorites.Favorite>() {
    override fun getRefreshKey(state: PagingState<Int, Favorites.Favorite>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Favorites.Favorite>> {
        val page = params.key ?: 1
        return api
            .getFavorites(page, params.loadSize)
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, Favorites.Favorite>> {
                LoadResult.Page(
                    data = it.array,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (it.array.isEmpty()) null else page + 1
                )
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }
}