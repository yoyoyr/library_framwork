

Glide.with(this)
      .load(R.mipmap.ic_launcher)
      .transform(RoundedCornersTransformation(200))
      .into(binding.img)

通过路径加载图片
<ImageView
     defaultImgUrl="url"
     imgUrl="url" />


文件缓存
建议每一个模块创建自己的缓存文件，如果内容过多，可创建多个文件
CacheStore.getCacheStoreWithKey(AccountLibConstants.ACCOUNT_FILE_CACHE_KEY)


