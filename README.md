# ShadowLayout

Android阴影布局

- 支持自定义边界形状
  - 自定义：ShadowLayout
  - 圆角：RoundedShadowLayout
  - 箭头：PopupShadowLayout

- 支持内阴影和外阴影
  - 内阴影：阴影会占据布局空间，即留出阴影的padding。
  - 外阴影：阴影会不会占据布局空间。



# 如何接入

1. 在根目录build.gradle添加jitpack仓库

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

2. 在module的build.gradle添加依赖

```groovy
dependencies {
	implementation 'com.github.goweii:ShadowLayout:Tag'
}
```



# 效果展示

可以下载[Demo](https://raw.githubusercontent.com/goweii/ShadowLayout/master/simple/release/simple-release.apk)体验

| ![rounded_shadow_layout](https://raw.githubusercontent.com/goweii/ShadowLayout/master/image/rounded_shadow_layout.gif) | ![popup_shadow_layout](https://raw.githubusercontent.com/goweii/ShadowLayout/master/image/popup_shadow_layout.gif) |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| RoundedShadowLayout                                          | PopupShadowLayout                                            |

