package com.dxshulya.wasabi.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.dxshulya.wasabi.data.Api
import com.dxshulya.wasabi.model.Task
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class TaskPagingSource(private val api: Api) : PagingSource<Int, Task>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Task> {
        val page = params.key ?: 1
        return try {
            val response = api.getTasks(page, 10)
            LoadResult.Page(
                response, prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}