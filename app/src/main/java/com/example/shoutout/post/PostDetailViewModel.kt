package com.example.shoutout.post

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.shoutout.ShoutRepository

class PostDetailViewModel  @ViewModelInject constructor(
    private val repository: ShoutRepository
) : ViewModel() {

}