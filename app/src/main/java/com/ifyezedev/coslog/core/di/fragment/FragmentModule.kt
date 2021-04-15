package com.ifyezedev.coslog.core.di.fragment

import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import dagger.Module
import dagger.Provides

@Module
class FragmentModule