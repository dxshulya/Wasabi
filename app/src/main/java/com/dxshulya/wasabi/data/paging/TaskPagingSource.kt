package com.dxshulya.wasabi.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.dxshulya.wasabi.data.Api
import com.dxshulya.wasabi.domain.model.Task
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class TaskPagingSource(private val api: Api) : RxPagingSource<Int, Task>() {

    override fun getRefreshKey(state: PagingState<Int, Task>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Task>> {
        val page = params.key ?: 1
        return api
            .getTasks(page, params.loadSize)
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, Task>> {
                LoadResult.Page(
                    data = it,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

}